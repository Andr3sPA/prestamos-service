package co.com.bancolombia.api.response;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ErrorResponse {
    private String errorCode;
    private String tittle;
    private String message;
    private List<String> errors;
    private Instant timestamp;
}
