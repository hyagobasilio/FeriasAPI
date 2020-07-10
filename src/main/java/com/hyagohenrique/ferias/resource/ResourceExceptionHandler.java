package com.hyagohenrique.ferias.resource;

import com.hyagohenrique.ferias.exception.FeriasNaoDisponivelException;
import com.hyagohenrique.ferias.exception.NotFoundException;
import com.hyagohenrique.ferias.response.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> handleNotFoundException(NotFoundException ex) {
        Response response = new Response<>();
        response.getErrors().add(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Response> handleFeriasNaoDisponivelException(RuntimeException ex) {
        Response response = new Response<>();
        response.getErrors().add("Um erro inesperado aconteceu!");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    
    @ExceptionHandler(FeriasNaoDisponivelException.class)
    public ResponseEntity<Response> handleFeriasNaoDisponivelException(FeriasNaoDisponivelException ex) {
        Response response = new Response<>();
        response.getErrors().add(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}