package co.com.bancolombia.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLoanAppReq {
    @NotNull(message = "El id de la solicitud de préstamo es obligatorio")
    private Long id;

    @NotNull(message = "El estado de la solicitud es obligatorio")
    private LoanAppStatus name;
}