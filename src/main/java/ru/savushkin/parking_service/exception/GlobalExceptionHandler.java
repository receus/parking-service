package ru.savushkin.parking_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        var message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation Error");
        return buildError(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<?> handleNotFound(VehicleNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        log.error("Unhandled error occurred", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,  "Unexpected error: " + ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<?> handleDeserialization(HttpMessageNotReadableException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(AlreadyExitedException.class)
    private ResponseEntity<?> handleAlreadyExited(AlreadyExitedException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(AlreadyParkedException.class)
    private ResponseEntity<?> handleAlreadyParked(AlreadyParkedException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<?> buildError(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                new ErrorResponse(message, LocalDateTime.now(), httpStatus.value()),
                httpStatus
        );
    }
}
