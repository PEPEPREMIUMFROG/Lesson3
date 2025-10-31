package models;

import org.example.model.Reader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReaderTest {

    @Test
    void testReaderCreationAndSetters() {
        Reader reader = new Reader();
        reader.setReaderId(1);
        reader.setName("John Doe");
        reader.setEmail("john@example.com");
        reader.setPhone("123456789");
        assertEquals(1, reader.getReaderId());
        assertEquals("John Doe", reader.getName());
        assertEquals("john@example.com", reader.getEmail());
        assertEquals("123456789", reader.getPhone());
    }

    @Test
    void testReaderConstructor() {
        Reader reader = new Reader("Jane Doe", "jane@example.com", "987654321");
        assertEquals("Jane Doe", reader.getName());
        assertEquals("jane@example.com", reader.getEmail());
        assertEquals("987654321", reader.getPhone());
        assertNull(reader.getReaderId());
    }
}