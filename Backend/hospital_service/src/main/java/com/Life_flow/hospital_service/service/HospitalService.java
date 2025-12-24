package com.Life_flow.hospital_service.service;

import com.Life_flow.hospital_service.dto.HospitalRequestDTO;
import com.Life_flow.hospital_service.dto.HospitalResponseDTO;
import java.util.List;
import java.util.UUID;

public interface HospitalService {
    HospitalResponseDTO createHospital(HospitalRequestDTO hospitalRequestDTO, UUID userId);
    HospitalResponseDTO getHospitalById(UUID hospitalId);
    List<HospitalResponseDTO> getAllHospitals();
    List<HospitalResponseDTO> getHospitalsByCity(String city);
    List<HospitalResponseDTO> getHospitalsByState(String state);
    HospitalResponseDTO updateHospital(UUID hospitalId, HospitalRequestDTO hospitalRequestDTO);
    void deleteHospital(UUID hospitalId);
    boolean existsByHospitalNameAndCity(String hospitalName, String city);

    HospitalResponseDTO getHospitalByTokenId(UUID userId);
}