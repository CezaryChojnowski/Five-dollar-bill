package com.homeBudget.configuration.error;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@SuppressWarnings({"unchecked","rawtypes"})
@ControllerAdvice
@PropertySource("classpath:messages.properties")
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${validationFailed}")
    private String validationFailed;

    @Value("${adaptationFailed}")
    private String adaptationFailed;

    @Value("${foundRecordFailed}")
    private String foundRecordFailed;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ErrorValidatingResponse error = new ErrorValidatingResponse(validationFailed , details, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity handleConstraintViolation(
            ConstraintViolationException ex) {
        List<String> details = new ArrayList<>();
        for (ConstraintViolation<?> error : ex.getConstraintViolations()) {
            details.add(error.getMessage());
        }
        ConstraintViolationResponse constraintViolationResponse =
                new ConstraintViolationResponse(validationFailed ,details, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<Object>(constraintViolationResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException exception) {
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        ErrorRecordNotFoundResponse error = new ErrorRecordNotFoundResponse(foundRecordFailed, details, HttpStatus.NOT_FOUND.value());
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecordExistsException.class)
    public ResponseEntity<Object> handleExistException(RecordExistsException exception) {
        ErrorExistsResponse error = new ErrorExistsResponse(adaptationFailed, exception.getMessage(), HttpStatus.CONFLICT.value());
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

}