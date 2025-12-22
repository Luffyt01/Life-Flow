package com.project.inventory_service.controllers;

import com.project.inventory_service.dto.TransactionRequestDto;
import com.project.inventory_service.dto.TransactionResponseDto;
import com.project.inventory_service.entities.enums.TransactionType;
import com.project.inventory_service.service.InventoryTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory/transactions")
@RequiredArgsConstructor
public class InventoryTransactionController {

    private final InventoryTransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(
            @Valid @RequestBody TransactionRequestDto requestDto) {
        TransactionResponseDto response = transactionService.createTransaction(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(
            @PathVariable UUID transactionId) {
        TransactionResponseDto response = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bag/{bagId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByBagId(
            @PathVariable UUID bagId) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByBagId(bagId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByType(
            @RequestParam(required = false) TransactionType type) {

            return ResponseEntity.ok(transactionService.getTransactionsByType(type));
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByRequestId(
            @PathVariable UUID requestId) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByRequestId(requestId);
        return ResponseEntity.ok(transactions);
    }
}
