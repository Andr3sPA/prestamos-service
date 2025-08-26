package co.com.bancolombia.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Table("loan_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanTypeEntity {

    @Id
    private Long id;

    @NotBlank(message = "Loan type name cannot be empty")
    @Column("name")
    private String name;

    @NotNull(message = "Minimum amount is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "Minimum amount must be greater than 0.00")
    @Column("minimum_amount")
    private BigDecimal minimumAmount;

    @NotNull(message = "Maximum amount is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "Maximum amount must be greater than 0.00")
    @Column("maximum_amount")
    private BigDecimal maximumAmount;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.00", message = "Interest rate must be >= 0.00")
    @DecimalMax(value = "100.00", message = "Interest rate must be <= 100.00")
    @Column("interest_rate")
    private BigDecimal interestRate;

    @Column("automatic_validation")
    private Boolean automaticValidation;
}

