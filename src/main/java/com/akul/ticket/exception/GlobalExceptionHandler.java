package com.akul.ticket.exception;

import com.akul.ticket.dto.ErrorDTO;
import com.akul.ticket.util.Date;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(@NonNull MethodArgumentNotValidException ex, @NonNull WebRequest req) {
        List<String> fields = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String fieldMessage = String.format("%s - %s", error.getField(), error.getDefaultMessage());
            fields.add(fieldMessage);
        }
        ErrorDTO errorDTO = ErrorDTO.builder()
                .timestamp(Date.timestamp())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(req.getDescription(false).substring(4))
                .fields(fields)
                .build();
        return ResponseEntity.badRequest().body(errorDTO);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDTO> handleDataIntegrityViolationException(
            @NonNull DataIntegrityViolationException ex, @NonNull WebRequest req) {
        if (ex.getRootCause() != null && ex.getRootCause() instanceof PSQLException psqlEx)  {
            if (PSQLState.UNIQUE_VIOLATION.getState().equals(psqlEx.getSQLState())) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .timestamp(Date.timestamp())
                        .status(HttpStatus.CONFLICT.value())
                        .error(HttpStatus.CONFLICT.getReasonPhrase())
                        .path(req.getDescription(false).substring(4))
                        .build();
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
            }
        }

        ErrorDTO errorDTO = ErrorDTO.builder()
                .timestamp(Date.timestamp())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .path(req.getDescription(false).substring(4))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }
}
