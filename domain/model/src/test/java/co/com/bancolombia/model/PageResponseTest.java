package co.com.bancolombia.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PageResponseTest {

    @Test
    void testPageResponseBuilder() {
        List<String> content = List.of("item1", "item2", "item3");
        PageResponse<String> pageResponse = PageResponse.<String>builder()
                .content(content)
                .totalElements(100L)
                .page(2)
                .size(10)
                .build();

        assertEquals(content, pageResponse.getContent());
        assertEquals(100L, pageResponse.getTotalElements());
        assertEquals(2, pageResponse.getPage());
        assertEquals(10, pageResponse.getSize());
    }

    @Test
    void testPageResponseSettersAndGetters() {
        PageResponse<String> pageResponse = new PageResponse<>();
        List<String> content = List.of("test");

        pageResponse.setContent(content);
        pageResponse.setTotalElements(50L);
        pageResponse.setPage(1);
        pageResponse.setSize(20);

        assertEquals(content, pageResponse.getContent());
        assertEquals(50L, pageResponse.getTotalElements());
        assertEquals(1, pageResponse.getPage());
        assertEquals(20, pageResponse.getSize());
    }

    @Test
    void testPageResponseNoArgsConstructor() {
        PageResponse<String> pageResponse = new PageResponse<>();
        assertNull(pageResponse.getContent());
        assertEquals(0L, pageResponse.getTotalElements());
        assertEquals(0, pageResponse.getPage());
        assertEquals(0, pageResponse.getSize());
    }

    @Test
    void testPageResponseAllArgsConstructor() {
        List<String> content = List.of("data");
        PageResponse<String> pageResponse = new PageResponse<>(content, 25L, 3, 5);

        assertEquals(content, pageResponse.getContent());
        assertEquals(25L, pageResponse.getTotalElements());
        assertEquals(3, pageResponse.getPage());
        assertEquals(5, pageResponse.getSize());
    }

    @Test
    void testPageResponseEmptyContent() {
        List<String> emptyContent = List.of();
        PageResponse<String> pageResponse = new PageResponse<>(emptyContent, 0L, 0, 10);

        assertTrue(pageResponse.getContent().isEmpty());
        assertEquals(0L, pageResponse.getTotalElements());
        assertEquals(0, pageResponse.getPage());
        assertEquals(10, pageResponse.getSize());
    }
}
