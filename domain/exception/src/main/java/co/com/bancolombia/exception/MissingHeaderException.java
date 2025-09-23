package co.com.bancolombia.exception;

import java.util.List;

public class MissingHeaderException extends BaseException {
    public MissingHeaderException(String headerName) {
        super(
                "MISSING_HEADER",
                "Falta header requerido",
                "El header '" + headerName + "' es obligatorio en la solicitud",
                List.of("No se encontró el header '" + headerName + "' en la petición"),
                400
        );
    }
}