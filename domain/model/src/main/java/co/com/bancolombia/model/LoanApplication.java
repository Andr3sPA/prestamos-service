package co.com.bancolombia.model;

import lombok.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoanApplication {
    private Long id;
    private BigDecimal amount;
    private Integer term;
    private String email;
    private State state;
    private LoanType loanType;
}
