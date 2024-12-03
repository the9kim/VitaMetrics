package com.vitametrics.vitametrics.global;

import com.vitametrics.vitametrics.sleep.exception.SleepRecordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {SleepRecordException.TemporalOrderException.class,
            SleepRecordException.InvalidDateFormatException.class})
    public ResponseEntity<ErrorResponse> handleCustomBadRequestException(final RuntimeException exception) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {SleepRecordException.NonExistSleepRecordException.class})
    public ResponseEntity<ErrorResponse> handleCustomNotFoundException(final RuntimeException exception) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {SleepRecordException.WrongSleepDurationFormatException.class,
            SleepRecordException.InvalidTimeFormatException.class})
    public ResponseEntity<ErrorResponse> handleCustomBadGateException(final RuntimeException exception) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<ValidationErrorResponse>>> handleValidationException(final MethodArgumentNotValidException exception) {
        List<ValidationErrorResponse> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationErrorResponse(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()))
                .toList();

        Map<String, List<ValidationErrorResponse>> response = new HashMap<>();
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(final String message, final HttpStatus status) {
        return ResponseEntity.status(status).body(new ErrorResponse(status.getReasonPhrase(), message));
    }

}
