import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ItemTest {
    @Test void constructorAndGetters() {
        Item item = new Item(" Milk ", 3.50, 10);
        assertEquals("Milk", item.getName());
        assertEquals(3.50, item.getPrice());
        assertEquals(10, item.getStockCount());
    }

    @Test void invalidConstructorValuesRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Item("", 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Item("Milk", -1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Item("Milk", 1, -1));
    }

    @Test void setPriceValidAndInvalid() {
        Item item = new Item("Milk", 3, 5);
        assertTrue(item.setPrice(4));
        assertEquals(4, item.getPrice());
        assertFalse(item.setPrice(-1));
        assertEquals(4, item.getPrice());
    }

    @Test void updateStockCannotGoBelowZero() {
        Item item = new Item("Milk", 3, 5);
        assertTrue(item.updateStock(3));
        assertEquals(8, item.getStockCount());
        assertFalse(item.updateStock(-9));
        assertEquals(8, item.getStockCount());
    }

    @Test void toStringContainsItemInformation() {
        String text = new Item("Milk", 3.5, 5).toString();
        assertTrue(text.contains("Milk"));
        assertTrue(text.contains("3.50"));
        assertTrue(text.contains("5"));
    }
}
