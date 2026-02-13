package com.life_flow.geolocation_service.controller;

import com.life_flow.geolocation_service.dto.*;
import com.life_flow.geolocation_service.service.GeoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/geo")
@RequiredArgsConstructor
public class GeoController {


    private final GeoService geoService;

    @GetMapping("/donors/nearby")
    public ResponseEntity<NearbyDonorsResponse> findNearbyDonors(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius_km,
            @RequestParam String bloodType,
            @RequestParam(required = false) String urgency_level,
            @RequestParam(required = false) Integer required_units,
            @RequestParam(required = false) String time_slot_start,
            @RequestParam(required = false) String time_slot_end) {
        return ResponseEntity.ok(geoService.findNearbyDonors(latitude, longitude, radius_km, bloodType, urgency_level, required_units, time_slot_start, time_slot_end));
    }

    @PostMapping("/appointments/calculate-slots")
    public ResponseEntity<CalculateSlotsResponse> calculateOptimalSlots(@RequestBody CalculateSlotsRequest request) {
        return ResponseEntity.ok(geoService.calculateOptimalSlots(request));
    }

    @GetMapping("/donors/{donor_id}/travel-time")
    public ResponseEntity<TravelTimeResponse> calculateTravelTime(
            @PathVariable("donor_id") String donorId,
            @RequestParam String center_id,
            @RequestParam String departure_time,
            @RequestParam String travel_mode) {
        return ResponseEntity.ok(geoService.calculateTravelTime(donorId, center_id, departure_time, travel_mode));
    }

    @GetMapping("/centers/nearby")
    public ResponseEntity<NearbyCentersResponse> findNearbyCenters(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius_km,
            @RequestParam(required = false) String blood_type_needed,
            @RequestParam(required = false) String urgency,
            @RequestParam(required = false) Integer capacity_required) {
        return ResponseEntity.ok(geoService.findNearbyCenters(latitude, longitude, radius_km, blood_type_needed, urgency, capacity_required));
    }





    @GetMapping("/requests/{request_id}/coverage")
    public ResponseEntity<RequestCoverageResponse> getRequestCoverage(
            @PathVariable("request_id") String requestId,
            @RequestParam(defaultValue = "false") boolean include_donors) {
        return ResponseEntity.ok(geoService.getRequestCoverage(requestId, include_donors));
    }

    @PostMapping("/optimize/routes")
    public ResponseEntity<OptimizeRoutesResponse> optimizeRoutes(@RequestBody OptimizeRoutesRequest request) {
        return ResponseEntity.ok(geoService.optimizeRoutes(request));
    }

    @GetMapping("/stats/response-times")
    public ResponseEntity<ResponseTimeStatsResponse> getResponseTimeStats(
            @RequestParam String center_id,
            @RequestParam String time_period,
            @RequestParam double radius_km,
            @RequestParam(required = false) String blood_type) {
        return ResponseEntity.ok(geoService.getResponseTimeStats(center_id, time_period, radius_km, blood_type));
    }

    @PostMapping("/predict/shortage")
    public ResponseEntity<ShortagePredictionResponse> predictShortage(@RequestBody ShortagePredictionRequest request) {
        return ResponseEntity.ok(geoService.predictShortage(request));
    }

    @GetMapping("/hotspots")
    public ResponseEntity<HotspotsResponse> identifyHotspots(
            @RequestParam String city,
            @RequestParam String time_period,
            @RequestParam(required = false) String blood_type) {
        return ResponseEntity.ok(geoService.identifyHotspots(city, time_period, blood_type));
    }

    @PostMapping("/simulate/emergency")
    public ResponseEntity<EmergencySimulationResponse> simulateEmergency(@RequestBody EmergencySimulationRequest request) {
        return ResponseEntity.ok(geoService.simulateEmergency(request));
    }

    @GetMapping("/admin/coverage-report")
    public ResponseEntity<CoverageReportResponse> getCoverageReport(
            @RequestParam String region,
            @RequestParam String report_type,
            @RequestParam String format) {
        return ResponseEntity.ok(geoService.getCoverageReport(region, report_type, format));
    }
}
