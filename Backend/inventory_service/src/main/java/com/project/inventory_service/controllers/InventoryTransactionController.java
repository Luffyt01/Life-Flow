package com.project.inventory_service.controllers;

import com.project.inventory_service.dto.TransactionRequestDto;
import com.project.inventory_service.dto.TransactionResponseDto;
import com.project.inventory_service.entities.enums.TransactionType;
import com.project.inventory_service.service.InventoryTransactionService;
import com.project.inventory_service.utils.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@PreAuthorize("hasRole('HOSPITAL')")
public class InventoryTransactionController {

    private final InventoryTransactionService transactionService;
    private final JwtService jwtService;

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDto> transferBloodUnits(
            HttpServletRequest request,
            @Valid @RequestBody TransactionRequestDto requestDto) {
        
//        UUID hospitalId = jwtParser.getUserId(request);
        // requestDto.setHospitalId(hospitalId); // If DTO has hospitalId

        TransactionResponseDto response = transactionService.createTransaction(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/request-fulfillment")
    public ResponseEntity<TransactionResponseDto> fulfillRequest(
            @RequestBody TransactionRequestDto requestDto) {
        
        TransactionResponseDto response = transactionService.createTransaction(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(
            @PathVariable UUID transactionId) {
        TransactionResponseDto response = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions/bag/{bagId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByBagId(
            @PathVariable UUID bagId) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByBagId(bagId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByType(
            HttpServletRequest request,
            @RequestParam(required = false) TransactionType type) {
        UUID hospitalId = jwtService.getUserId(request);

        return ResponseEntity.ok(transactionService.getTransactionsByType(hospitalId, type));
    }

    @GetMapping("/transactions/request/{requestId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByRequestId(
            @PathVariable UUID requestId) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByRequestId(requestId);
        return ResponseEntity.ok(transactions);
    }
}
