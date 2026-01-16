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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @CacheEvict(value = "transactions", allEntries = true)
    public TransactionResponseDto createTransaction(TransactionRequestDto requestDto) {
        log.info("Creating transaction for bag ID: {}", requestDto.getBagId());
        try {
            // Find the blood bag
//            BloodInventoryEntity bloodBag = bloodInventoryRepository.findById(requestDto.getBagId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Blood bag not found with id: " + requestDto.getBagId()));

            // Create and save transaction
            InventoryTransactionsEntity transaction = InventoryTransactionsEntity.builder()
                    .bagId(requestDto.getBagId())
                    .transactionType(requestDto.getTransactionType())
                    .fromCenterId(requestDto.getFromCenterId())
                    .toCenterId(requestDto.getToCenterId())
                    .notes(requestDto.getNotes())
                    .hospitalId(requestDto.getHospitalId()) // Assuming this is passed or derived
                    .requestId(requestDto.getRequestId()) // Assuming this is passed
                    .previousStatus(requestDto.getPreviousStatus())
                    .newStatus(requestDto.getNewStatus())
                    .performedBy(requestDto.getPerformedBy())
                    .build();

            InventoryTransactionsEntity savedTransaction = transactionsRepository.save(transaction);
            log.info("Created transaction with id: {}", savedTransaction.getTransactionId());
            
            return modelMapper.map(savedTransaction,TransactionResponseDto.class);
            
        } catch (Exception e) {
            log.error("Error creating transaction: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error creating transaction: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "transactions", key = "#transactionId")
    public TransactionResponseDto getTransactionById(UUID transactionId) {
        log.info("Fetching transaction with ID: {}", transactionId);
        try {
            InventoryTransactionsEntity transaction = transactionsRepository.findById(transactionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + transactionId));
            return modelMapper.map(transaction,TransactionResponseDto.class);
        } catch (ResourceNotFoundException e) {
            log.warn("Transaction not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error fetching transaction: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error fetching transaction: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "transactions", key = "#bagId")
    public List<TransactionResponseDto> getTransactionsByBagId(UUID bagId) {
        log.info("Fetching transactions for bag ID: {}", bagId);
        try {
            List<InventoryTransactionsEntity> transactions = transactionsRepository.findByBagId(bagId);
             return transactions.stream()
                     .map(t -> modelMapper.map(t, TransactionResponseDto.class))
                     .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching transactions by bag ID: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error fetching transactions by bag ID: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "transactions", key = "{#hospitalId, #transactionType}")
    public List<TransactionResponseDto> getTransactionsByType(UUID hospitalId,TransactionType transactionType) {
        log.info("Fetching transactions by type: {} for hospital: {}", transactionType, hospitalId);
        try {
//            TransactionType type = TransactionType.valueOf(transactionType.toUpperCase());
            if(transactionType == null){
                return transactionsRepository.findAllByHospitalId(hospitalId).stream()
                        .map(transaction -> modelMapper.map(transaction, TransactionResponseDto.class))
                        .collect(Collectors.toList());
            }
            List<InventoryTransactionsEntity> transactions = transactionsRepository.findByTransactionTypeAndHospitalId(hospitalId,transactionType);
            return transactions.stream()
                    .map(t -> modelMapper.map(t, TransactionResponseDto.class))
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            log.error("Invalid transaction type: {}", transactionType);
            throw new RuntimeConflictException("Invalid transaction type: " + transactionType);
        } catch (Exception e) {
            log.error("Error fetching transactions by type: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error fetching transactions by type: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "transactions", key = "#requestId")
    public List<TransactionResponseDto> getTransactionsByRequestId(UUID requestId) {
        log.info("Fetching transactions for request ID: {}", requestId);
        try {
            List<InventoryTransactionsEntity> transactions = transactionsRepository.findByRequestId(requestId);
            return transactions.stream()
                    .map(t -> modelMapper.map(t, TransactionResponseDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching transactions by request ID: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error fetching transactions by request ID: " + e.getMessage());
        }
    }

}
