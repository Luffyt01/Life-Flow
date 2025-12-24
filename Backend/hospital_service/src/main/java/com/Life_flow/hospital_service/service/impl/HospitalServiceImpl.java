package com.Life_flow.hospital_service.service.impl;

import com.Life_flow.hospital_service.dto.HospitalRequestDTO;
import com.Life_flow.hospital_service.dto.HospitalResponseDTO;
import com.Life_flow.hospital_service.entity.HospitalEntity;
import com.Life_flow.hospital_service.exception.ExceptionTypes.ResourceNotFoundException;
import com.Life_flow.hospital_service.repository.HospitalRepository;
import com.Life_flow.hospital_service.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public HospitalResponseDTO createHospital(HospitalRequestDTO hospitalRequestDTO, UUID userId) {
        if (hospitalRepository.existsByHospitalNameAndCity(
                hospitalRequestDTO.getHospitalName(),
                hospitalRequestDTO.getCity())) {
            throw new RuntimeException("Hospital already exists in this city");
        }

        HospitalEntity hospital = modelMapper.map(hospitalRequestDTO, HospitalEntity.class);
        hospital.setUserId(userId);
        hospital.setCreateAt(LocalDateTime.now());
        HospitalEntity savedHospital = hospitalRepository.save(hospital);
        return modelMapper.map(savedHospital, HospitalResponseDTO.class);
    }

    @Override
    public HospitalResponseDTO getHospitalById(UUID hospitalId) {
        HospitalEntity hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + hospitalId));
        return modelMapper.map(hospital, HospitalResponseDTO.class);
    }

    @Override
    public List<HospitalResponseDTO> getAllHospitals() {
        return hospitalRepository.findAll().stream()
                .map(hospital -> modelMapper.map(hospital, HospitalResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<HospitalResponseDTO> getHospitalsByCity(String city) {
        return hospitalRepository.findByCity(city).stream()
                .map(hospital -> modelMapper.map(hospital, HospitalResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<HospitalResponseDTO> getHospitalsByState(String state) {
        return hospitalRepository.findByState(state).stream()
                .map(hospital -> modelMapper.map(hospital, HospitalResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HospitalResponseDTO updateHospital(UUID hospitalId, HospitalRequestDTO hospitalRequestDTO) {
        HospitalEntity existingHospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + hospitalId));

        modelMapper.map(hospitalRequestDTO, existingHospital);
        existingHospital.setUpdateAt(LocalDateTime.now());
        HospitalEntity updatedHospital = hospitalRepository.save(existingHospital);
        return modelMapper.map(updatedHospital, HospitalResponseDTO.class);
    }

    @Override
    @Transactional
    public void deleteHospital(UUID hospitalId) {
        HospitalEntity hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + hospitalId));
        hospitalRepository.delete(hospital);
    }

    @Override
    public boolean existsByHospitalNameAndCity(String hospitalName, String city) {
        return hospitalRepository.existsByHospitalNameAndCity(hospitalName, city);
    }

    @Override
    public HospitalResponseDTO getHospitalByTokenId(UUID userId) {
        HospitalEntity hospital = hospitalRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + userId));
        return modelMapper.map(hospital, HospitalResponseDTO.class);
    }
}