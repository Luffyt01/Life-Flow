package com.Life_flow.hospital_service.controller;

import com.Life_flow.hospital_service.dto.HospitalRequestDTO;
import com.Life_flow.hospital_service.dto.HospitalResponseDTO;
import com.Life_flow.hospital_service.service.HospitalService;
import com.Life_flow.hospital_service.util.JwtParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;
    private final JwtParser jwtParser;

    @PostMapping("/create")
    public ResponseEntity<HospitalResponseDTO> createHospital(
            @Valid @RequestBody HospitalRequestDTO hospitalRequestDTO, HttpServletRequest request) {
        UUID userId = jwtParser.getUserId(request);
        HospitalResponseDTO createdHospital = hospitalService.createHospital(hospitalRequestDTO, userId);
        return new ResponseEntity<>(createdHospital, HttpStatus.CREATED);
    }

    @GetMapping("getHospitalByToken")
    public ResponseEntity<HospitalResponseDTO> getHospitalByToken(HttpServletRequest request){
        UUID userId = jwtParser.getUserId(request);
        HospitalResponseDTO hospital = hospitalService.getHospitalByTokenId(userId);
        return ResponseEntity.ok(hospital);
    }

    @GetMapping("/{hospitalId}")
    public ResponseEntity<HospitalResponseDTO> getHospitalById(@PathVariable UUID hospitalId) {
        HospitalResponseDTO hospital = hospitalService.getHospitalById(hospitalId);
        return ResponseEntity.ok(hospital);
    }

    @GetMapping("/getAllHospitals")
    public ResponseEntity<List<HospitalResponseDTO>> getAllHospitals() {
        return ResponseEntity.ok(hospitalService.getAllHospitals());
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<HospitalResponseDTO>> getHospitalsByCity(@PathVariable String city) {
        return ResponseEntity.ok(hospitalService.getHospitalsByCity(city));
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<HospitalResponseDTO>> getHospitalsByState(@PathVariable String state) {
        return ResponseEntity.ok(hospitalService.getHospitalsByState(state));
    }

    @PutMapping("/{hospitalId}")
    public ResponseEntity<HospitalResponseDTO> updateHospital(
            @PathVariable UUID hospitalId,
            @Valid @RequestBody HospitalRequestDTO hospitalRequestDTO) {
        HospitalResponseDTO updatedHospital = hospitalService.updateHospital(hospitalId, hospitalRequestDTO);
        return ResponseEntity.ok(updatedHospital);
    }

    @DeleteMapping("/{hospitalId}")
    public ResponseEntity<Void> deleteHospital(@PathVariable UUID hospitalId) {
        hospitalService.deleteHospital(hospitalId);
        return ResponseEntity.noContent().build();
    }
}