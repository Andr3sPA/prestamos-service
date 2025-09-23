package co.com.bancolombia.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculateCapacityReqTest {
    @Test
    void testSettersAndGetters() {
        CalculateCapacityReq req = new CalculateCapacityReq();
        req.setLoanId(1L);
        assertEquals(1L, req.getLoanId());
    }
}
