package co.com.bancolombia.model.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalcularCapacidadRequestTest {
    @Test
    void testUserDTOSettersAndGetters() {
        CalcularCapacidadRequest.UserDTO user = new CalcularCapacidadRequest.UserDTO();
        user.setUserId(1L);
        user.setBaseSalary(10000.0);
        assertEquals(1L, user.getUserId());
        assertEquals(10000.0, user.getBaseSalary());
    }
}
