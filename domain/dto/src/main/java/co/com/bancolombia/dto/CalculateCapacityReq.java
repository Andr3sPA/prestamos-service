package co.com.bancolombia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateCapacityReq {
    @NotNull(message = "El campo loanId es obligatorio")
    private Long loanId;
}
