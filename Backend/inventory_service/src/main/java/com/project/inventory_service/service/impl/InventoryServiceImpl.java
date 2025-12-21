package com.project.inventory_service.service.impl;

import com.project.inventory_service.dto.BagUpdateDto;
import com.project.inventory_service.dto.BloodBagDto;
import com.project.inventory_service.dto.GetBagResponseDto;
import com.project.inventory_service.entities.BloodInventoryEntity;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.entities.enums.StatusType;
import com.project.inventory_service.exceptions.ExceptionTypes.ErrorInSavingDataInDatabase;
import com.project.inventory_service.exceptions.ExceptionTypes.RuntimeConflictException;
import com.project.inventory_service.repositories.BloodInventoryRepository;
import com.project.inventory_service.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final ModelMapper modelMapper;
    private final BloodInventoryRepository bloodInventoryRepository;

    @Override
    @Transactional
    public BloodBagDto createBloodInventory(BloodBagDto bloodBagDto) {
        try{
            BloodInventoryEntity bloodInventoryEntity = modelMapper.map(bloodBagDto,BloodInventoryEntity.class);
//            log.warn("BAG ID"+bloodInventoryEntity.getBagId());
            bloodInventoryEntity.setBagId(null);
            BloodInventoryEntity saveBloodInventory = bloodInventoryRepository.save(bloodInventoryEntity);
            return modelMapper.map(saveBloodInventory,BloodBagDto.class);
        }catch(Exception e){
            log.error("Error in saving blood inventory"+e.getMessage());
            throw new ErrorInSavingDataInDatabase("Error in saving blood inventory");
        }
    }

    @Override
    public GetBagResponseDto getBagById(UUID id) {
        try{
          log.info("Getting bag by id : ",id);
          BloodInventoryEntity getBag = bloodInventoryRepository.findByBagId(id).orElse(null);
          if(getBag == null){
              throw new RuntimeConflictException("beg not found with this id: "+id);
          }
          return modelMapper.map(getBag,GetBagResponseDto.class);
        }catch(Exception e){
            log.error("Error in getting bag by id "+e.getMessage());
            throw new RuntimeConflictException("Error in getting bag by id "+id);
        }
    }

    @Override
    public GetBagResponseDto updateBag(UUID id, BagUpdateDto bloodBagDto) {
        try{
            BloodInventoryEntity getBag = bloodInventoryRepository.findByBagId(id).orElse(null);
            if(getBag == null){
                throw new RuntimeConflictException("beg not found with this id: "+id);
            }
            BloodInventoryEntity updateBag = bloodInventoryRepository.updateBag(id,bloodBagDto.getStatus(),bloodBagDto.getUnitsAvailable());

            return modelMapper.map(updateBag,GetBagResponseDto.class);
        }
        catch(Exception e){
            log.error("something error in update bag id: "+id);
            throw new RuntimeConflictException("something error in update bag id: "+id);
        }
    }

    @Override
    public List<GetBagResponseDto> searchBags(BloodType bloodType, StatusType statusType) {
        try{
            List<GetBagResponseDto> getBags = bloodInventoryRepository.findByBloodTypeAndStatusType(bloodType,statusType);
            return getBags;
        }catch(Exception e){
            log.error("Error in searching bags "+e.getMessage());
            throw new RuntimeConflictException("Error in searching bags "+bloodType+" and "+statusType);
        }
    }

    @Override
    public GetBagResponseDto releaseBag(UUID id) {
        try{
            BloodInventoryEntity getBag = bloodInventoryRepository.findByBagId(id).orElse(null);
            if(getBag == null){
                throw new RuntimeConflictException("beg not found with this id: "+id);
            }
            BloodInventoryEntity updateBag = bloodInventoryRepository.updateBagRelease(id,StatusType.AVAILABLE);

            return modelMapper.map(updateBag,GetBagResponseDto.class);
        }
        catch(Exception e){
            log.error("something error in update bag id: "+id);
            throw new RuntimeConflictException("something error in updating bag id: "+id);
        }
    }

}
