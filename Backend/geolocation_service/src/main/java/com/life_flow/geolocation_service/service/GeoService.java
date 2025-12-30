package com.life_flow.geolocation_service.service;

import com.life_flow.geolocation_service.client.UserServiceClient;
import com.life_flow.geolocation_service.dto.*;
import com.life_flow.geolocation_service.dto.user_service.DonorProfileResponseDto;
import com.life_flow.geolocation_service.dto.user_service.HospitalProfileResponseDto;
import com.life_flow.geolocation_service.dto.user_service.PointDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service

public class GeoService {


    private final UserServiceClient userServiceClient;

    public NearbyDonorsResponse findNearbyDonors(double latitude, double longitude, double radius_km, String blood_type, String urgency_level, Integer required_units, String time_slot_start, String time_slot_end) {
        NearbyDonorsResponse response = new NearbyDonorsResponse();
        
        try {
            List<DonorProfileResponseDto> nearbyDonors = userServiceClient.findNearbyDonors(latitude, longitude, radius_km, blood_type);
            List<DonorDto> donorDtos = nearbyDonors.stream()
                    .map(donor -> {
                        DonorDto dto = new DonorDto();
                        dto.setDonorId(donor.getDonorId().toString());
                        dto.setBloodType(donor.getBloodType().toString());
                        dto.setLatitude(donor.getLatitude());
                        dto.setLongitude(donor.getLongitude());
                        dto.setDistanceKm(calculateDistance(latitude, longitude, donor.getLatitude(), donor.getLongitude()));
                        return dto;
                    })
                    .collect(Collectors.toList());
            
            response.setDonors(donorDtos);
            response.setTotal_count(donorDtos.size());
        } catch (Exception e) {
            // Fallback or error handling
            e.printStackTrace();
            response.setDonors(new ArrayList<>());
            response.setTotal_count(0);
        }

        response.setEstimated_completion_time("30 mins");
        response.setCoverage_map("url_to_map");
        return response;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public CalculateSlotsResponse calculateOptimalSlots(CalculateSlotsRequest request) {
        CalculateSlotsResponse response = new CalculateSlotsResponse();
        response.setOptimal_slots(new ArrayList<>());
        response.setEfficiency_score(0.0);
        response.setDonor_assignments(new HashMap<>());
        response.setGap_analysis(new HashMap<>());
        return response;
    }

    public TravelTimeResponse calculateTravelTime(String donorId, String center_id, String departure_time, String travel_mode) {
        TravelTimeResponse response = new TravelTimeResponse();
        try {
            DonorProfileResponseDto donor = userServiceClient.getDonorProfile(UUID.fromString(donorId));
            // Mock calculation based on donor location
            response.setTravel_time_minutes(15);
            response.setDistance_km(5.0);
            response.setOptimal_route("Main St -> 2nd Ave");
            response.setEta("10:15 AM");
            response.setTraffic_conditions("Normal");
        } catch (Exception e) {
            // Handle case where donor is not found or ID is invalid
        }
        return response;
    }

    public NearbyCentersResponse findNearbyCenters(double latitude, double longitude, double radius_km, String blood_type_needed, String urgency, Integer capacity_required) {
        NearbyCentersResponse response = new NearbyCentersResponse();
        response.setCenters(new ArrayList<>());
        response.setDistances(new HashMap<>());
        response.setAvailability(new HashMap<>());
        
        try {
            List<HospitalProfileResponseDto> nearbyHospitals = userServiceClient.findNearbyHospitals(latitude, longitude, radius_km);
            
            if (nearbyHospitals != null) {
                for (HospitalProfileResponseDto hospital : nearbyHospitals) {
                    CenterDto centerDto = new CenterDto();
                    centerDto.setCenterId(hospital.getHospitalId().toString());
                    centerDto.setName(hospital.getName());
                    centerDto.setLatitude(hospital.getLatitude());
                    centerDto.setLongitude(hospital.getLongitude());
                    
                    response.getCenters().add(centerDto);
                    response.getDistances().put(centerDto.getCenterId(), calculateDistance(latitude, longitude, hospital.getLatitude(), hospital.getLongitude()));
                    response.getAvailability().put(centerDto.getCenterId(), true); // Mock availability
                }
            }
            
            if (!response.getCenters().isEmpty()) {
                response.setRecommended_center(response.getCenters().get(0).getCenterId());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public CoverageAnalysisResponse analyzeCoverage(CoverageAnalysisRequest request) {
        CoverageAnalysisResponse response = new CoverageAnalysisResponse();
        response.setCoverage_map("map_url");
        response.setHotspots(new ArrayList<>());
        response.setGaps(new ArrayList<>());
        response.setRecommendations(new ArrayList<>());
        return response;
    }

    public DonorLocationResponse getDonorLocation(String donorId, String accuracy) {
        DonorLocationResponse response = new DonorLocationResponse();
        try {
            DonorProfileResponseDto donor = userServiceClient.getDonorProfile(UUID.fromString(donorId));
            Double lat = donor.getLatitude();
            Double lon = donor.getLongitude();
            
            if (lat != null && lon != null) {
                response.setLatitude(lat);
                response.setLongitude(lon);
            }
        } catch (Exception e) {
            // Handle error
        }
        // If latitude/longitude are not set (0.0), it means we couldn't get them or they were null
        if (response.getLatitude() == 0.0 && response.getLongitude() == 0.0) {
             response.setLatitude(0.0);
             response.setLongitude(0.0);
        }
        response.setAccuracy_meters(10.0);
        response.setLast_updated("now");
        response.setLocation_source("GPS");
        return response;
    }

    public UpdateLocationResponse updateDonorLocation(String donorId, UpdateLocationRequest request) {
        UpdateLocationResponse response = new UpdateLocationResponse();
        try {
            PointDTO pointDTO = new PointDTO(new double[]{request.getLongitude(), request.getLatitude()}, "Point");
            userServiceClient.updateDonorLocation(UUID.fromString(donorId), pointDTO);
            response.setLocation_updated(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setLocation_updated(false);
        }
        response.setGeofence_check(true);
        response.setNearby_requests(new ArrayList<>());
        return response;
    }

    public RequestCoverageResponse getRequestCoverage(String requestId, boolean include_donors) {
        RequestCoverageResponse response = new RequestCoverageResponse();
        response.setCoverage_area("area_data");
        response.setDonor_density(0.0);
        response.setResponse_heatmap("heatmap_url");
        response.setSuccess_probability(0.0);
        return response;
    }

    public OptimizeRoutesResponse optimizeRoutes(OptimizeRoutesRequest request) {
        OptimizeRoutesResponse response = new OptimizeRoutesResponse();
        response.setOptimized_routes(new ArrayList<>());
        response.setTotal_distance(0.0);
        response.setTotal_time(0.0);
        response.setRoute_map_url("map_url");
        return response;
    }

    public ResponseTimeStatsResponse getResponseTimeStats(String center_id, String time_period, double radius_km, String blood_type) {
        ResponseTimeStatsResponse response = new ResponseTimeStatsResponse();
        response.setAverage_response_time(0.0);
        response.setPercentile_90(0.0);
        response.setBest_time_of_day("10:00 AM");
        response.setSeasonal_trends(new HashMap<>());
        return response;
    }

    public ShortagePredictionResponse predictShortage(ShortagePredictionRequest request) {
        ShortagePredictionResponse response = new ShortagePredictionResponse();
        response.setPredicted_shortage_date("2023-12-31");
        response.setShortage_units(0);
        response.setConfidence_interval("95%");
        response.setMitigation_suggestions(new ArrayList<>());
        return response;
    }

    public HotspotsResponse identifyHotspots(String city, String time_period, String blood_type) {
        HotspotsResponse response = new HotspotsResponse();
        response.setHotspots(new ArrayList<>());
        response.setDonor_concentration(0.0);
        response.setUtilization_rate(0.0);
        response.setExpansion_recommendations(new ArrayList<>());
        return response;
    }

    public EmergencySimulationResponse simulateEmergency(EmergencySimulationRequest request) {
        EmergencySimulationResponse response = new EmergencySimulationResponse();
        response.setSimulation_result("Success");
        response.setEstimated_time("1 hour");
        response.setSuccess_probability(0.9);
        response.setBottlenecks(new ArrayList<>());
        return response;
    }

    public CoverageReportResponse getCoverageReport(String region, String report_type, String format) {
        CoverageReportResponse response = new CoverageReportResponse();
        response.setReport_url("report_url");
        response.setCoverage_score(0.0);
        response.setRecommendations(new ArrayList<>());
        response.setGenerated_at("now");
        return response;
    }
}
