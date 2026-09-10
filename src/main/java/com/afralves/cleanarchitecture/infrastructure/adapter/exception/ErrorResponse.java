package com.afralves.cleanarchitecture.infrastructure.adapter.exception;

import java.time.Instant;

public record ErrorResponse(Instant timestamp, int status, String code, String message) {}
