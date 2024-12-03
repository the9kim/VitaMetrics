package com.vitametrics.vitametrics.global;

public record ValidationErrorResponse(
        String field,
        String message,
        Object rejectedValue
) {
}
