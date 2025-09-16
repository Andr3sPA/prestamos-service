package co.com.bancolombia.model.request;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalcularCapacidadRequest {
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoanTypeDTO {
        private Long loanTypeId;
        private String name;
        private BigDecimal minimumAmount;
        private BigDecimal maximumAmount;
        private BigDecimal interestRateAnnual;
        private Boolean automaticValidation;
    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicationDTO {
        private Long applicationId;
        private BigDecimal amount;
        private Integer termMonths;
        private String email;
        private Long stateId;
        private LoanTypeDTO loanType;


    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExistingLoanDTO {
        private Double saldoRestante;
        private Integer plazoRestanteMeses;
        private Double tasaInteresAnual;

    }

    private ApplicationDTO application;
    private UserDTO user;
    private List<ExistingLoanDTO> existingLoans;



    // DTO interno para el usuario
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDTO {
        private Long userId;
        private Double baseSalary;


    }
}
