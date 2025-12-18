package com.project.Life_Flow.donor_service.services.impl;

import com.project.Life_Flow.donor_service.dto.DonorProfileDto;
import com.project.Life_Flow.donor_service.dto.DonorProfileRequestDto;
import com.project.Life_Flow.donor_service.entities.DonorProfile;
import com.project.Life_Flow.donor_service.repositories.DonorProfileRepository;
import com.project.Life_Flow.donor_service.services.DonorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DonorServiceImpl implements DonorService {

    private final DonorProfileRepository donorProfileRepository;
    private final ModelMapper modelMapper;

    @Override
    public DonorProfileDto getMyProfile(UUID donorId) {
        return modelMapper.map(donorProfileRepository.findById(donorId),DonorProfileDto.class);
    }

    @Override
    public DonorProfileDto createDonorProfile(DonorProfileRequestDto donorProfileRequestDto) {
        DonorProfile donorProfile = modelMapper.map(donorProfileRequestDto, DonorProfile.class);
        DonorProfile savedDonorProfile = donorProfileRepository.save(donorProfile);
        return modelMapper.map(savedDonorProfile, DonorProfileDto.class);
    }
}
