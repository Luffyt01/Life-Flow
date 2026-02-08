package com.life_flow.geolocation_service.service;

import com.life_flow.geolocation_service.client.UserServiceClient;
import com.life_flow.geolocation_service.dto.*;
import com.life_flow.geolocation_service.dto.user_service.DonorProfileResponseDto;
import com.life_flow.geolocation_service.dto.user_service.HospitalProfileResponseDto;
import com.life_flow.geolocation_service.dto.user_service.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeoService {

    private final UserServiceClient userServiceClient;

    private final DistanceService distanceService;

    private final  GeometryFactory geometryFactory;

    public NearbyDonorsResponse findNearbyDonors(double latitude, double longitude, double radius_km, String bloodType, String urgency_level, Integer required_units, String time_slot_start, String time_slot_end) {
        log.info("Finding nearby donors for lat: {}, lon: {}, radius: {}, blood_type: {}", latitude, longitude, radius_km, bloodType);
        NearbyDonorsResponse response = new NearbyDonorsResponse();
        Point userLocation = createPoint(longitude, latitude);

        try {
            List<DonorProfileResponseDto> donorsPage = userServiceClient.findNearbyDonors(latitude, longitude, radius_km, bloodType);
            log.info("size ,{}",donorsPage.size());
            log.info("{}",donorsPage.toString());
            List<DonorDto> donorDtos = new ArrayList<>();

            if (donorsPage != null && !donorsPage.isEmpty()) {
                for (DonorProfileResponseDto donor : donorsPage) {
                    Double donorLat = donor.getLatitude();
                    Double donorLon = donor.getLongitude();
                    log.info("{} ,{}",donorLat,donorLon);

                    if (donorLat != null && donorLon != null) {
                        Point donorLocation = createPoint(donorLon, donorLat);
                        double distance = distanceService.calculateDistance(userLocation, donorLocation);
                       log.info("distance {}",distance);
                        if (distance <= radius_km) {
                            DonorDto dto = new DonorDto();
                            dto.setDonorId(donor.getDonorId() != null ? donor.getDonorId().toString() : "");
                            dto.setBloodType(donor.getBloodType() != null ? donor.getBloodType().toString() : "");
                            dto.setLatitude(donorLat);
                            dto.setLongitude(donorLon);
                            dto.setDistanceKm(distance);
                            donorDtos.add(dto);
                        }
                    }
                }
            }

            response.setDonors(donorDtos);
            response.setTotal_count(donorDtos.size());
            log.info("Found {} nearby donors", donorDtos.size());
        } catch (Exception e) {
            log.error("Error finding nearby donors", e);
            response.setDonors(new ArrayList<>());
            response.setTotal_count(0);
        }

        response.setEstimated_completion_time("30 mins");
        response.setCoverage_map("url_to_map");
        return response;
    }

    public CalculateSlotsResponse calculateOptimalSlots(CalculateSlotsRequest request) {
        log.info("Calculating optimal slots for center: {}", request.getCenter_id());
        CalculateSlotsResponse response = new CalculateSlotsResponse();
        response.setOptimal_slots(new ArrayList<>());
        response.setEfficiency_score(0.0);
        response.setDonor_assignments(new HashMap<>());
        response.setGap_analysis(new HashMap<>());
        return response;
    }

    public TravelTimeResponse calculateTravelTime(String donorId, String center_id, String departure_time, String travel_mode) {
        log.info("Calculating travel time for donor: {} to center: {}", donorId, center_id);
        TravelTimeResponse response = new TravelTimeResponse();
        try {
            DonorProfileResponseDto donor = userServiceClient.getDonorProfile(UUID.fromString(donorId));
            HospitalProfileResponseDto center = userServiceClient.getHospitalProfile(UUID.fromString(center_id));

            if (donor != null && center != null && donor.getLatitude() != null && donor.getLongitude() != null && center.getLatitude() != null && center.getLongitude() != null) {
                Point donorLocation = createPoint(donor.getLongitude(), donor.getLatitude());
                Point centerLocation = createPoint(center.getLongitude(), center.getLatitude());
                double distance = distanceService.calculateDistance(donorLocation, centerLocation);
                
                // Mocking travel time based on distance
                double averageSpeedKmph = 40.0;
                double travelTimeHours = distance / averageSpeedKmph;
                int travelTimeMinutes = (int) (travelTimeHours * 60);

                response.setTravel_time_minutes(travelTimeMinutes);
                response.setDistance_km(distance);
                response.setOptimal_route("Route data from OSRM can be added here if needed.");
                response.setEta("ETA can be calculated based on departure time and travel time.");
                response.setTraffic_conditions("normal");
                log.info("Calculated travel time: {} mins, distance: {} km", travelTimeMinutes, distance);
            }
        } catch (Exception e) {
            log.error("Error calculating travel time", e);
        }
        return response;
    }

    public NearbyCentersResponse findNearbyCenters(double latitude, double longitude, double radius_km, String blood_type_needed, String urgency, Integer capacity_required) {
        log.info("Finding nearby centers for lat: {}, lon: {}, radius: {}", latitude, longitude, radius_km);
        NearbyCentersResponse response = new NearbyCentersResponse();
        response.setCenters(new ArrayList<>());
        response.setDistances(new HashMap<>());
        response.setAvailability(new HashMap<>());
        Point userLocation = createPoint(longitude, latitude);

        try {
//            List<HospitalProfileResponseDto> hospitals = userServiceClient.searchHospitals(null);
            List<HospitalProfileResponseDto> hospitals = userServiceClient.findNearbyHospitals(latitude, longitude, radius_km);

            if (hospitals != null) {
                for (HospitalProfileResponseDto hospital : hospitals) {
                    Double hospLat = hospital.getLatitude();
                    Double hospLon = hospital.getLongitude();

                    if (hospLat != null && hospLon != null) {
                        Point hospitalLocation = createPoint(hospLon, hospLat);
                        double distance = distanceService.calculateDistance(userLocation, hospitalLocation);

                        if (distance <= radius_km) {
                            CenterDto centerDto = new CenterDto();
                            centerDto.setCenterId(hospital.getHospitalId() != null ? hospital.getHospitalId().toString() : "");
                            centerDto.setName(hospital.getName());
                            centerDto.setLatitude(hospLat);
                            centerDto.setLongitude(hospLon);

                            response.getCenters().add(centerDto);
                            response.getDistances().put(centerDto.getCenterId(), distance);
                            response.getAvailability().put(centerDto.getCenterId(), true);
                        }
                    }
                }
            }

            if (!response.getCenters().isEmpty()) {
                response.setRecommended_center(response.getCenters().get(0).getCenterId());
            }
            log.info("Found {} nearby centers", response.getCenters().size());

        } catch (Exception e) {
            log.error("Error finding nearby centers", e);
        }

        return response;
    }

    public CoverageAnalysisResponse analyzeCoverage(CoverageAnalysisRequest request) {
        log.info("Analyzing coverage for center: {}", request.getCenter_id());
        CoverageAnalysisResponse response = new CoverageAnalysisResponse();
        response.setCoverage_map("map_url");
        response.setHotspots(new ArrayList<>());
        response.setGaps(new ArrayList<>());
        response.setRecommendations(new ArrayList<>());
        return response;
    }

    public DonorLocationResponse getDonorLocation(String donorId, String accuracy) {
        log.info("Getting location for donor: {}", donorId);
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
            log.error("Error getting donor location", e);
        }
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
        log.info("Updating location for donor: {}", donorId);
        UpdateLocationResponse response = new UpdateLocationResponse();
        response.setLocation_updated(true);
        response.setGeofence_check(true);
        response.setNearby_requests(new ArrayList<>());
        return response;
    }

    public RequestCoverageResponse getRequestCoverage(String requestId, boolean include_donors) {
        log.info("Getting request coverage for request: {}", requestId);
        RequestCoverageResponse response = new RequestCoverageResponse();
        response.setCoverage_area("area_data");
        response.setDonor_density(0.0);
        response.setResponse_heatmap("heatmap_url");
        response.setSuccess_probability(0.0);
        return response;
    }

    public OptimizeRoutesResponse optimizeRoutes(OptimizeRoutesRequest request) {
        log.info("Optimizing routes for center: {}", request.getCenter_id());
        OptimizeRoutesResponse response = new OptimizeRoutesResponse();
        response.setOptimized_routes(new ArrayList<>());
        response.setTotal_distance(0.0);
        response.setTotal_time(0.0);
        response.setRoute_map_url("map_url");
        return response;
    }

    public ResponseTimeStatsResponse getResponseTimeStats(String center_id, String time_period, double radius_km, String blood_type) {
        log.info("Getting response time stats for center: {}", center_id);
        ResponseTimeStatsResponse response = new ResponseTimeStatsResponse();
        response.setAverage_response_time(0.0);
        response.setPercentile_90(0.0);
        response.setBest_time_of_day("10:00 AM");
        response.setSeasonal_trends(new HashMap<>());
        return response;
    }

    public ShortagePredictionResponse predictShortage(ShortagePredictionRequest request) {
        log.info("Predicting shortage for center: {}", request.getCenter_id());
        ShortagePredictionResponse response = new ShortagePredictionResponse();
        response.setPredicted_shortage_date("2023-12-31");
        response.setShortage_units(0);
        response.setConfidence_interval("95%");
        response.setMitigation_suggestions(new ArrayList<>());
        return response;
    }

    public HotspotsResponse identifyHotspots(String city, String time_period, String blood_type) {
        log.info("Identifying hotspots for city: {}", city);
        HotspotsResponse response = new HotspotsResponse();
        response.setHotspots(new ArrayList<>());
        response.setDonor_concentration(0.0);
        response.setUtilization_rate(0.0);
        response.setExpansion_recommendations(new ArrayList<>());
        return response;
    }

    public EmergencySimulationResponse simulateEmergency(EmergencySimulationRequest request) {
        log.info("Simulating emergency for center: {}", request.getCenter_id());
        EmergencySimulationResponse response = new EmergencySimulationResponse();
        response.setSimulation_result("Success");
        response.setEstimated_time("1 hour");
        response.setSuccess_probability(0.9);
        response.setBottlenecks(new ArrayList<>());
        return response;
    }

    public CoverageReportResponse getCoverageReport(String region, String report_type, String format) {
        log.info("Getting coverage report for region: {}", region);
        CoverageReportResponse response = new CoverageReportResponse();
        response.setReport_url("report_url");
        response.setCoverage_score(0.0);
        response.setRecommendations(new ArrayList<>());
        response.setGenerated_at("now");
        return response;
    }

    private Point createPoint(double longitude, double latitude) {
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }
}
