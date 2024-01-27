package com.unir.loans.controller;

import com.unir.loans.model.db.Loan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.unir.loans.service.LoansService;

import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Loans Controller", description = "Microservicio encargado del sistema de préstamos de una biblioteca.")
public class LoansController {

    private final LoansService service; //Inyeccion por constructor mediante @RequiredArgsConstructor. Y, también es inyección por interfaz.

    /*
    @PostMapping("/loans")
    public ResponseEntity<Loan> createLoan(@RequestBody @Valid LoanRequest request) { //Se valida con Jakarta Validation API

        log.info("Creating loan...");
        Loan created = service.createLoan(request);

        if (created != null) {
            return ResponseEntity.ok(created);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }*/

    @GetMapping("/loans")
    @Operation(
            operationId = "Obtener préstamos",
            description = "Operacion de lectura",
            summary = "Se devuelve una lista de todos los préstamos almacenados en la base de datos.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Loan.class))),
                    @ApiResponse(
                    responseCode = "400",
                    content = @Content(mediaType = "application/json"),
                    description = "Parametros incorrectos introducidos."),
            })
    public ResponseEntity<List<Loan>> getLoans(
            @RequestHeader Map<String, String> headers,
            @Parameter(name = "user", description = "Identificador del usuario.  Tiene que ser exacto", example = "012345")
            @RequestParam(required = false) Long user,
            @Parameter(name = "book", description = "Identificador del libro.  Tiene que ser exacto", example = "012345")
            @RequestParam(required = false) Long book,
            @Parameter(name = "minInitialDate", description = "Fecha mínima de inicio del préstamo", example = "012345")
            @RequestParam(required = false) Date minInitialDate,
            @Parameter(name = "maxInitialDate", description = "Fecha máxima de inicio del préstamo", example = "012345")
            @RequestParam(required = false) Date maxInitialDate,
            @Parameter(name = "minLoanedDate", description = "Fecha mínima de fecha fijada para fin del préstamo", example = "012345")
            @RequestParam(required = false) Date minLoanedDate,
            @Parameter(name = "maxLoanedDate", description = "Fecha máxima de fecha fijada para fin del préstamo", example = "012345")
            @RequestParam(required = false) Date maxLoanedDate,
            @Parameter(name = "minEndDate", description = "Fecha mínima de fin del préstamo", example = "012345")
            @RequestParam(required = false) Date minEndDate,
            @Parameter(name = "maxEndDate", description = "Fecha máxima de fin del préstamo", example = "012345")
            @RequestParam(required = false) Date maxEndDate,
            @Parameter(name = "returned", description = "Marca si el libro ha sido devuelto", example = "true")
            @RequestParam(required = false) Boolean returned
          ) {

        List<Loan> loans = service.getLoans(user,book,minInitialDate,maxInitialDate,minLoanedDate,maxLoanedDate,minEndDate,maxEndDate,returned);
        return ResponseEntity.ok(Objects.requireNonNullElse(loans, Collections.emptyList()));
    }

    @GetMapping("/loans/{id}")
    @Operation(
            operationId = "Obtener un préstamo",
            description = "Operacion de lectura",
            summary = "Se devuelve una préstamo a partir de su identificador.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Loan.class))),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(mediaType = "application/json"),
                            description = "No se ha encontrado un préstamo con el identificador indicado"),
            })
    public ResponseEntity<Loan> getLoan(@PathVariable String id) {
        Loan loan = service.getLoan(id);
        if (loan != null) {
            return ResponseEntity.ok(loan);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
