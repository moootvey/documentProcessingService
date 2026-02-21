package org.itqProj.exception;

import jakarta.validation.ConstraintViolationException;
import org.itqProj.util.GenerateExceptionDtoUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<?> handleDocumentNotFoundException(DocumentNotFoundException e) {
        return new ResponseEntity<>(GenerateExceptionDtoUtil.generateExceptionDto(e, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<?> handlerInternalServer(InternalServerException e) {
        return new ResponseEntity<>(GenerateExceptionDtoUtil.generateExceptionDto(e, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValid(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(GenerateExceptionDtoUtil.generateValidationExceptionDto(e, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handlerConstraintViolation(ConstraintViolationException e) {
        return new ResponseEntity<>(GenerateExceptionDtoUtil.generateExceptionDto(e, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handlerMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        return new ResponseEntity<>(GenerateExceptionDtoUtil.generateExceptionDto(e, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }
}
