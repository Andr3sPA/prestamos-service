package co.com.bancolombia.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
@AllArgsConstructor
@Getter
public class BaseException extends RuntimeException {
    private final String errorCode;
    private final String title;
    private final String message;
    private final List<String> errors;
    private final int status;
    private final Instant timestamp;

    public BaseException(String errorCode, String tittle, String message, List<String> errors, int status) {
        this(errorCode, tittle, message, errors, status, Instant.now());
    }
}