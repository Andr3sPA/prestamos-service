package co.com.bancolombia.exception;

import java.util.List;

public class MissingEmailHeaderException extends BaseException {
    public MissingEmailHeaderException() {
        super(
                "MISSING_EMAIL_HEADER",
                "Falta header de email",
                "El header 'X-User-Email' es obligatorio en la solicitud",
                List.of("No se encontró el header 'X-User-Email' en la petición"),
                400
        );
    }
}
