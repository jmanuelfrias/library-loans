package com.unir.loans.controller;

import com.unir.loans.model.db.Loan;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.unir.loans.model.request.LoanRequest;
import com.unir.loans.service.LoansService;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoansController {

    private final LoansService service; //Inyeccion por constructor mediante @RequiredArgsConstructor. Y, también es inyección por interfaz.

    @PostMapping("/loans")
    public ResponseEntity<Loan> createLoan(@RequestBody @Valid LoanRequest request) { //Se valida con Jakarta Validation API

        log.info("Creating loan...");
        Loan created = service.createLoan(request);

        if (created != null) {
            return ResponseEntity.ok(created);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/loans")
    public ResponseEntity<List<Loan>> getLoans() {

        List<Loan> loans = service.getLoans();
        if (loans != null) {
            return ResponseEntity.ok(loans);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/loans/{id}")
    public ResponseEntity<Loan> getLoan(@PathVariable String id) {

        Loan loan = service.getLoan(id);
        if (loan != null) {
            return ResponseEntity.ok(loan);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
