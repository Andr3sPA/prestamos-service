package co.com.bancolombia.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Table("loan_application")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationEntity {

    @Id
    private Long id;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "Amount must be greater than 0.00")
    @Column("amount")
    private BigDecimal amount;

    @NotNull(message = "Term is required")
    @Min(value = 1, message = "Term must be at least 1")
    @Column("term")
    private Integer term;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Column("email")
    private String email;

    @NotNull(message = "State id is required")
    @Column("state_id")
    private Long stateId;

    @NotNull(message = "Loan type id is required")
    @Column("loan_type_id")
    private Long loanTypeId;
}
