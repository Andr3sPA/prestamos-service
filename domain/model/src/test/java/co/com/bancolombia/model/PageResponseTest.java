package co.com.bancolombia.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PageResponseTest {
    @Test
    void testPageResponseFields() {
        PageResponse<String> page = new PageResponse<>();
        page.setContent(List.of("item1", "item2"));
        page.setTotalElements(2);
        page.setPage(1);
        page.setSize(2);
        assertEquals(2, page.getContent().size());
        assertEquals(2, page.getTotalElements());
        assertEquals(1, page.getPage());
        assertEquals(2, page.getSize());
    }
}
