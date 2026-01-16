package com.project.inventory_service.service;

import com.project.inventory_service.dto.CollectionCenterDto;
import java.util.List;
import java.util.UUID;

public interface CollectionCenterService {
    CollectionCenterDto createCollectionCenter(CollectionCenterDto collectionCenterDto);
    CollectionCenterDto updateCollectionCenter(UUID centerId, CollectionCenterDto collectionCenterDto);
    CollectionCenterDto getCollectionCenterById(UUID centerId);
    List<CollectionCenterDto> getAllCollectionCenters();
    List<CollectionCenterDto> getCollectionCentersByHospitalId(UUID hospitalId);
    void deleteCollectionCenter(UUID centerId);
}
