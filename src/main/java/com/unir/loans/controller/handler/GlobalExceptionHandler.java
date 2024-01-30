package com.unir.loans.controller.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;


@ControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handle(Exception e) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // Usamos como default el 500

            // Mirar si la excepción recibida corresponde a alguna de las otras posibilidades
            if (e instanceof BadRequestException) {
                status = HttpStatus.BAD_REQUEST; // 400
            } else if (e instanceof NotFoundException) {
                status = HttpStatus.NOT_FOUND; // 404
            } else if (e instanceof HttpClientErrorException.Conflict) {
                status = HttpStatus.CONFLICT; //409
            }

            return ResponseEntity.status(status).build();
        }
    }



