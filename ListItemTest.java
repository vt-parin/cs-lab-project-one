import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ListItemTest {
    @Test void constructorAndGetters() {
        Item item = new Item("Milk", 3, 5);
        ListItem li = new ListItem(item, 2);
        assertSame(item, li.getItem());
        assertEquals(2, li.getQuantity());
    }

    @Test void invalidConstructorValuesRejected() {
        Item item = new Item("Milk", 3, 5);
        assertThrows(IllegalArgumentException.class, () -> new ListItem(null, 1));
        assertThrows(IllegalArgumentException.class, () -> new ListItem(item, 0));
    }

    @Test void setQuantity() {
        ListItem li = new ListItem(new Item("Milk", 3, 5), 2);
        assertTrue(li.setQuantity(4));
        assertEquals(4, li.getQuantity());
        assertFalse(li.setQuantity(-1));
        assertEquals(4, li.getQuantity());
    }

    @Test void toString() {
        assertEquals("Milk x2", new ListItem(new Item("Milk", 3, 5), 2).toString());
    }
}
