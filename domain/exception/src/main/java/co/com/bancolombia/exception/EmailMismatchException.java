package co.com.bancolombia.exception;
import java.util.List;
public class EmailMismatchException extends BaseException {
    public EmailMismatchException(String requestEmail, String sessionEmail) {
        super(
                "EMAIL_MISMATCH",
                "Correo electrónico inválido",
                "El correo del request (" + requestEmail + ") no coincide con el de la sesión (" + sessionEmail + ")",
                List.of("Verifica que el correo enviado en la solicitud sea el mismo que el usuario autenticado"),
                400
        );
    }
}