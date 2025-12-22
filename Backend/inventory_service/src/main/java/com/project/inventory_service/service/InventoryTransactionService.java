package com.project.inventory_service.service;

import com.project.inventory_service.dto.TransactionRequestDto;
import com.project.inventory_service.dto.TransactionResponseDto;
import com.project.inventory_service.entities.enums.TransactionType;

import java.util.List;
import java.util.UUID;

public interface InventoryTransactionService {
    TransactionResponseDto createTransaction(TransactionRequestDto requestDto);
    TransactionResponseDto getTransactionById(UUID transactionId);
    List<TransactionResponseDto> getTransactionsByBagId(UUID bagId);
    List<TransactionResponseDto> getTransactionsByType(TransactionType transactionType);
    List<TransactionResponseDto> getTransactionsByRequestId(UUID requestId);
}
