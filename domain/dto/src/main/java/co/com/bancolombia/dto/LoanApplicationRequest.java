package co.com.bancolombia.dto;

import jakarta.validation.constraints.*;
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

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal amount;

    @NotNull(message = "El plazo es obligatorio")
    @Positive(message = "El plazo debe ser positivo")
    private Integer term;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Formato de correo electrónico inválido")
    private String email;

    @NotNull(message = "El estado es obligatorio")
    private Long stateId;

    @NotNull(message = "El tipo de préstamo es obligatorio")
    private Long loanTypeId;
}

