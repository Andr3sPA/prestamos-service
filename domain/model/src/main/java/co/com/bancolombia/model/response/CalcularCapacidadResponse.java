package co.com.bancolombia.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalcularCapacidadResponse {
    @Setter
    @Getter
    public static class PagoDTO {
        private Integer cuotaNum;
        private Double pagoTotal;
        private Double interes;
        private Double abonoCapital;
        private Double saldoRestante;

    }

    private Integer applicationId;
    private String decision;        // "APROBADO" / "RECHAZADO" / "REVISION_MANUAL"
    private String reason;
    private Double capacidadMaxima;
    private Double deudaMensualActual;
    private Double capacidadDisponible;
    private Double cuotaNueva;
    private List<PagoDTO> planPago;
    private String emailBody;       // opcional: cuerpo del email preformateado

}
