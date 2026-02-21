package org.itqProj.util;


import jakarta.validation.ConstraintViolationException;
import org.itqProj.dto.exception.ExceptionDetailsDto;
import org.itqProj.dto.exception.ExceptionDto;
import org.itqProj.dto.exception.ValidationExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;


import java.time.OffsetDateTime;
import java.util.Objects;

import static java.time.ZoneOffset.UTC;

public class GenerateExceptionDtoUtil {
    private static final String VALUE_ERROR_TYPE = "value_error";

    public static ValidationExceptionDto generateValidationExceptionDto(BindException exception, HttpStatus httpStatus) {
        return new ValidationExceptionDto(
                String.valueOf(httpStatus),
                exception
                        .getFieldErrors()
                        .stream()
                        .map(fieldError ->
                                new ExceptionDetailsDto(
                                        VALUE_ERROR_TYPE,
                                        fieldError.getField(),
                                        fieldError.getDefaultMessage(),
                                        (Objects.requireNonNull(fieldError.getRejectedValue())).toString()))
                        .toList(),
                OffsetDateTime.now(UTC));
    }

    public static ExceptionDto generateExceptionDto(RuntimeException ex, HttpStatus httpStatus) {
        String exceptionMessage = ex.getMessage();
        if (ex instanceof ConstraintViolationException) {
            String[] message = ex.getMessage().split(": ");
            exceptionMessage = message[1];
        }
        return new ExceptionDto(String.valueOf(httpStatus.value()), httpStatus.getReasonPhrase(), exceptionMessage, OffsetDateTime.now(UTC));
    }
}
