package com.vitametrics.vitametrics.global;

import com.vitametrics.vitametrics.sleep.exception.SleepRecordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {SleepRecordException.TemporalOrderException.class})
    public ResponseEntity<ErrorResponse> handleCustomBadRequestException(final RuntimeException exception) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {SleepRecordException.NonExistSleepRecordException.class})
    public ResponseEntity<ErrorResponse> handleCustomNotFoundException(final RuntimeException exception) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(final String message, final HttpStatus status) {
        return ResponseEntity.status(status).body(new ErrorResponse(status.getReasonPhrase(), message));
    }
}
