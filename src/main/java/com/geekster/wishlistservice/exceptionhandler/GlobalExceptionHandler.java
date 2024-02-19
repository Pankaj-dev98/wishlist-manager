package com.geekster.wishlistservice.exceptionhandler;

import com.geekster.wishlistservice.utils.dto.GenericSuccessResponse;
import com.geekster.wishlistservice.utils.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * All pathways that may throw irreconcilable exceptions are intercepted by this class.
 * This class return structured JSON objects to the client giving information about the failed request.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            UserAlreadyExistsException.class,
            NoSuchElementException.class
    })
    public ResponseEntity<GenericErrorResponse> exceptionHandler(Exception e) {
        GenericErrorResponse error = GenericErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(
        MethodArgumentNotValidException.class
    )
    public ResponseEntity<Map<String, String>> validationExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            response.put("INVALID FIELD ERROR " + field, errorMessage);
        });

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
