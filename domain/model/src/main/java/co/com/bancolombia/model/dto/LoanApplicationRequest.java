package co.com.bancolombia.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationRequest {

    private BigDecimal amount;

    private Integer term;

    private String email;

    private Long stateId;

    private Long loanTypeId;
}
