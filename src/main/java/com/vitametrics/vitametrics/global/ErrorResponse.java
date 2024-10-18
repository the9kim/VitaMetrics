package com.vitametrics.vitametrics.global;

public record ErrorResponse(
        String error,
        String message
) {
}
