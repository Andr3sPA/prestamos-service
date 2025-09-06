package co.com.bancolombia.exception;

import java.util.List;

public class LoanApplicationNotFoundException extends BaseException {
    public LoanApplicationNotFoundException(Long id) {
        super(
                "LOAN_APPLICATION_NOT_FOUND",
                "Solicitud de préstamo no encontrada",
                "No existe la solicitud con id: " + id,
                List.of("No existe la solicitud con id: " + id),
                404
        );
    }
}

