package com.project.inventory_service.service.impl;

import com.project.inventory_service.dto.TransactionRequestDto;
import com.project.inventory_service.dto.TransactionResponseDto;
import com.project.inventory_service.entities.BloodInventoryEntity;
import com.project.inventory_service.entities.InventoryTransactionsEntity;
import com.project.inventory_service.entities.enums.TransactionType;
import com.project.inventory_service.exceptions.ExceptionTypes.ResourceNotFoundException;
import com.project.inventory_service.exceptions.ExceptionTypes.RuntimeConflictException;
import com.project.inventory_service.repositories.BloodInventoryRepository;

import com.project.inventory_service.repositories.InventoryTransactionsRepository;
import com.project.inventory_service.service.InventoryTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryTransactionServiceImpl implements InventoryTransactionService {

    private final InventoryTransactionsRepository transactionsRepository;
    private final BloodInventoryRepository bloodInventoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public TransactionResponseDto createTransaction(TransactionRequestDto requestDto) {
        try {
            // Find the blood bag
//            BloodInventoryEntity bloodBag = bloodInventoryRepository.findById(requestDto.getBagId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Blood bag not found with id: " + requestDto.getBagId()));

            // Create and save transaction
            InventoryTransactionsEntity transaction = InventoryTransactionsEntity.builder()
                    .bagId(requestDto.getBagId())
                    .transactionType(requestDto.getTransactionType())
                    .quantity(requestDto.getQuantity())
                    .fromLocation(requestDto.getFromLocation())
                    .toLocation(requestDto.getToLocation())
                    .notes(requestDto.getNotes())
                    .hospitalId(requestDto.getHospitalId())
                    .requestId(requestDto.getRequestId())
                    .build();

            InventoryTransactionsEntity savedTransaction = transactionsRepository.save(transaction);
            log.info("Created transaction with id: {}", savedTransaction.getTransactionId());
            
            return modelMapper.map(savedTransaction,TransactionResponseDto.class);
            
        } catch (Exception e) {
            log.error("Error creating transaction: {}", e.getMessage());
            throw new RuntimeConflictException("Error creating transaction: " + e.getMessage());
        }
    }

    @Override
    public TransactionResponseDto getTransactionById(UUID transactionId) {
        InventoryTransactionsEntity transaction = transactionsRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + transactionId));
        return modelMapper.map(transaction,TransactionResponseDto.class);
    }

    @Override
    public List<TransactionResponseDto> getTransactionsByBagId(UUID bagId) {
        List<InventoryTransactionsEntity> transactions = transactionsRepository.findByBagId(bagId);
         return Collections.singletonList(modelMapper.map(transactions, TransactionResponseDto.class));

    }

    @Override
    public List<TransactionResponseDto> getTransactionsByType(UUID hospitalId,TransactionType transactionType) {
        try {
//            TransactionType type = TransactionType.valueOf(transactionType.toUpperCase());
            if(transactionType == null){
                return transactionsRepository.findAllByHospitalId(hospitalId).stream()
                        .map(transaction -> modelMapper.map(transaction, TransactionResponseDto.class))
                        .collect(Collectors.toList());
            }
            List<InventoryTransactionsEntity> transactions = transactionsRepository.findByTransactionType(hospitalId,transactionType);
            return Collections.singletonList(modelMapper.map(transactions, TransactionResponseDto.class));
        } catch (IllegalArgumentException e) {
            throw new RuntimeConflictException("Invalid transaction type: " + transactionType);
        }
    }

    @Override
    public List<TransactionResponseDto> getTransactionsByRequestId(UUID requestId) {
        List<InventoryTransactionsEntity> transactions = transactionsRepository.findByRequestId(requestId);
        return Collections.singletonList(modelMapper.map(transactions, TransactionResponseDto.class));
    }

}
