package co.com.bancolombia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanUpdateMessage {
    private Long loanId;
    private String newState;
    private String email;
    private Double amount;
}
