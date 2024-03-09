package com.akul.ticket.exception;

import com.akul.ticket.dto.ErrorDTO;
import com.akul.ticket.util.DateUtil;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(@NonNull MethodArgumentNotValidException ex, @NonNull WebRequest req) {
        var fields = new ArrayList<String>();
        for (var error : ex.getBindingResult().getFieldErrors()) {
            var fieldMessage = String.format("%s - %s", camelToSnake(error.getField()), error.getDefaultMessage());
            fields.add(fieldMessage);
        }
        var errorDTO = ErrorDTO.builder()
                .timestamp(DateUtil.timestamp())
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
        if (ex.getRootCause() != null && ex.getRootCause() instanceof PSQLException psqlEx) {
            if (PSQLState.UNIQUE_VIOLATION.getState().equals(psqlEx.getSQLState())) {
                var errorDTO = ErrorDTO.builder()
                        .timestamp(DateUtil.timestamp())
                        .status(HttpStatus.CONFLICT.value())
                        .error(HttpStatus.CONFLICT.getReasonPhrase())
                        .path(req.getDescription(false).substring(4))
                        .build();
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
            }
        }

        var errorDTO = ErrorDTO.builder()
                .timestamp(DateUtil.timestamp())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .path(req.getDescription(false).substring(4))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseBody
    public ResponseEntity<ErrorDTO> handleInvalidEnumValueException(@NonNull InvalidFormatException ex, @NonNull WebRequest req) {
        var field = extractField(ex.getMessage());

        if (ex.getTargetType().isEnum()) {
            var values = extractEnumValues(ex.getMessage());

            var errorDTO = ErrorDTO.builder()
                    .timestamp(DateUtil.timestamp())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .path(req.getDescription(false).substring(4))
                    .fields(Collections.singletonList(String.format("%s - must be value of %s", field, values)))
                    .build();
            return ResponseEntity.badRequest().body(errorDTO);

        }

        if (ex.getMessage().contains("Integer") || ex.getMessage().contains("BigDecimal")) {
            var errorDTO = ErrorDTO.builder()
                    .timestamp(DateUtil.timestamp())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .path(req.getDescription(false).substring(4))
                    .fields(Collections.singletonList(String.format("%s - must be number", field)))
                    .build();
            return ResponseEntity.badRequest().body(errorDTO);
        }

        var errorDTO = ErrorDTO.builder()
                .timestamp(DateUtil.timestamp())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(req.getDescription(false).substring(4))
                .build();
        return ResponseEntity.badRequest().body(errorDTO);
    }

    private String extractEnumValues(String errorMessage) {
        var joiner = new StringJoiner(", ", "[", "]");
        var pattern = Pattern.compile("\\[([^]]+)]");
        var matcher = pattern.matcher(errorMessage);
        if (matcher.find()) {
            var enumValuesStr = matcher.group(1);
            var enumValuesArray = enumValuesStr.split(",\\s*");
            for (var value : enumValuesArray) {
                joiner.add(value.trim());
            }
        }
        return joiner.toString();
    }

    private String extractField(String errorMessage) {
        Pattern pattern = Pattern.compile("\\[\"(\\w+)\"]");
        Matcher matcher = pattern.matcher(errorMessage);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "field_enum";
    }

    private String camelToSnake(String camelCase) {
        return camelCase.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase();
    }
}
