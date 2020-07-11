package com.hyagohenrique.ferias.resource;

import com.hyagohenrique.ferias.exception.FeriasNaoDisponivelException;
import com.hyagohenrique.ferias.exception.NotFoundException;
import com.hyagohenrique.ferias.response.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Response> handleHttpMediaTypeNotSupportedException(MultipartException e, RedirectAttributes redirectAttributes) {

        Response response = new Response<>();
        response.getErrors().add("Formato de arquivo não suportado!");
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);

    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Response> handleMaxUploadSizeExceededException(MultipartException e, RedirectAttributes redirectAttributes) {

        Response response = new Response<>();
        response.getErrors().add("O tamanho máximo de upload de arquivo é de 2MB!");
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);

    }
    
    @ExceptionHandler(FeriasNaoDisponivelException.class)
    public ResponseEntity<Response> handleFeriasNaoDisponivelException(FeriasNaoDisponivelException ex) {
        Response response = new Response<>();
        response.getErrors().add(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}