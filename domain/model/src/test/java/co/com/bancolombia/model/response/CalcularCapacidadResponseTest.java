package co.com.bancolombia.model.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalcularCapacidadResponseTest {
    @Test
    void testSettersAndGetters() {
        CalcularCapacidadResponse response = new CalcularCapacidadResponse();
        response.setDecision("APROBADO");
        response.setApplicationId(800);
        assertEquals("APROBADO", response.getDecision());
        assertEquals(800, response.getApplicationId());
    }
}
