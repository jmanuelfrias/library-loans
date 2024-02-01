package com.unir.loans.controller;

import com.unir.loans.model.db.Loan;
import com.unir.loans.model.request.LoanRequest;
import com.unir.loans.model.request.RequestResult;
import jakarta.validation.Valid;
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

    private final LoansService service;


    @PostMapping("/loans")
    @Operation(
            operationId = "Insertar un préstamo",
            description = "Operacion de escritura",
            summary = "Se crea un nuevo préstamo tras comprobar que existe el libro en el catálogo.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del préstamo a crear.",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoanRequest.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Loan.class))),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(mediaType = "application/json"),
                            description = "Datos incorrectos introducidos.")
            })
    public ResponseEntity<Loan> createLoan(@RequestBody @Valid LoanRequest request) {
        ResponseEntity<Loan> result;
        log.info("Creating loan...");
        RequestResult created = service.createLoan(request);
        result = created.getResult()==201 ? ResponseEntity.ok(created.getCreated()) : ResponseEntity.badRequest().build();
        return result;
    }

    @GetMapping("/loans")
    @Operation(
            operationId = "Obtener préstamos",
            description = "Operacion de lectura",
            summary = "Se encuentra una lista de todos los préstamos almacenados en la base de datos.",
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
            @Parameter(name = "book", description = "Identificador del libro.  Tiene que ser exacto", example = "11")
            @RequestParam(required = false) Long book,
            @Parameter(name = "minInitialDate", description = "Fecha mínima de inicio del préstamo", example = "2023-01-25")
            @RequestParam(required = false) Date minInitialDate,
            @Parameter(name = "maxInitialDate", description = "Fecha máxima de inicio del préstamo", example = "2025-05-12")
            @RequestParam(required = false) Date maxInitialDate,
            @Parameter(name = "minDueDate", description = "Fecha mínima de fecha fijada para fin del préstamo", example = "2024-02-02")
            @RequestParam(required = false) Date mindueDate,
            @Parameter(name = "maxDueDate", description = "Fecha máxima de fecha fijada para fin del préstamo", example = "2024-06-09")
            @RequestParam(required = false) Date maxdueDate,
            @Parameter(name = "minEndDate", description = "Fecha mínima de fin del préstamo", example = "2023-12-12")
            @RequestParam(required = false) Date minEndDate,
            @Parameter(name = "maxEndDate", description = "Fecha máxima de fin del préstamo", example = "2024-01-31")
            @RequestParam(required = false) Date maxEndDate,
            @Parameter(name = "returned", description = "Marca si el libro ha sido devuelto", example = "true")
            @RequestParam(required = false) Boolean returned
          ) {

        List<Loan> loans = service.getLoans(user,book,minInitialDate,maxInitialDate,mindueDate,maxdueDate,minEndDate,maxEndDate,returned);
        return ResponseEntity.ok(Objects.requireNonNullElse(loans, Collections.emptyList()));
    }

    @GetMapping("/loans/{id}")
    @Operation(
            operationId = "Obtener un préstamo",
            description = "Operacion de lectura",
            summary = "Se encuentra una préstamo a partir de su identificador.",
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

    @PatchMapping("/loans/{loanId}")
    @Operation(
            operationId = "Devolver un libro",
            description = "Operacion de escritura",
            summary = "Se devuelve un libro por lo que se registra la fecha de devolucion y se marca como devuelto",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Modificaciones que se van a hacer al prestamo",
                    required = true,
                    content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = String.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Loan.class))),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(mediaType = "application/json"),
                            description = "Datos incorrectos introducidos."),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(mediaType = "application/json"),
                            description = "No se ha encontrado el prestamo con el identificador indicado."),
                    @ApiResponse(
                            responseCode = "500",
                            content = @Content(mediaType = "application/json"),
                            description = "Problemas con el servidor.")
            })
    public ResponseEntity<String> patchLoan(@PathVariable String loanId, @RequestBody String patchBody) {
        ResponseEntity<String> result;
        RequestResult patched = service.updateLoan(loanId, patchBody);
        result = switch (patched.getResult()) {
            case 200 -> ResponseEntity.ok("Préstamo devuelto");
            case 404 -> ResponseEntity.notFound().build();
            case 500 -> ResponseEntity.internalServerError().build();
            default -> ResponseEntity.badRequest().build();
        };
        return result;

    }

}
