package com.api.spring_marketplace_api.exception;

public record ApiError(
        String code, String message
) {
}
