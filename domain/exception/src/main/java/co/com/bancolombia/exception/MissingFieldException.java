package co.com.bancolombia.exception;

import java.util.List;

public class MissingFieldException extends BaseException {
    public MissingFieldException(String fieldName) {
        super(
                "MISSING_FIELD",
                "Campo requerido faltante",
                "El campo '" + fieldName + "' es obligatorio",
                List.of("Falta el campo: " + fieldName),
                400
        );
    }
}