package com.example.technical.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotFoundException extends RuntimeException {
    private Map<String, String> errors;

    public NotFoundException(Map<String, String> errors) {
        super();
        this.errors = errors;
    }
}
