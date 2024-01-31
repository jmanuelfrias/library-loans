package com.unir.loans.controller.handler;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
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
        if (e instanceof BadRequestException ||
                e.getCause() instanceof JsonParseException || //Debido a que usamos el @Valid, si metemos mal la fecha, el servidor pega una excepción.
                e.getCause() instanceof InvalidFormatException) { //Y el error realmente es del formato, no de algo del server
            status = HttpStatus.BAD_REQUEST; // 400
        } else if (e instanceof NotFoundException ) {
            status = HttpStatus.NOT_FOUND; // 404
        } else if (e instanceof HttpClientErrorException.Conflict) {
            status = HttpStatus.CONFLICT; //409
        }

        return ResponseEntity.status(status).build();
    }
}



