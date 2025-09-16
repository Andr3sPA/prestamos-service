package co.com.bancolombia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CalculateCapacityReq {
    @NotNull(message = "El campo loanId es obligatorio")
    private Long loanId;
}
