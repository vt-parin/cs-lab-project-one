import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class InventoryTest {
    @Test void addFindAndRemove() {
        Inventory inv = new Inventory(3);
        Item milk = new Item("Milk", 3, 5);
        assertTrue(inv.addItem(milk));
        assertSame(milk, inv.findItem("mIlK"));
        assertTrue(inv.removeItem("MILK"));
        assertNull(inv.findItem("milk"));
    }

    @Test void rejectsDuplicatesAndNulls() {
        Inventory inv = new Inventory();
        Item milk = new Item("Milk", 3, 5);
        assertFalse(inv.addItem(null));
        assertTrue(inv.addItem(milk));
        assertFalse(inv.addItem(new Item("mIlK", 4, 2)));
    }

    @Test void capacityIsEnforced() {
        Inventory inv = new Inventory(2);
        assertTrue(inv.addItem(new Item("A", 1, 1)));
        assertTrue(inv.addItem(new Item("B", 1, 1)));
        assertFalse(inv.addItem(new Item("C", 1, 1)));
        assertEquals(2, inv.getItemCount());
    }

    @Test void stockChecksAndTotals() {
        Inventory inv = new Inventory();
        inv.addItem(new Item("Milk", 3, 5));
        inv.addItem(new Item("Bread", 2, 4));
        assertTrue(inv.isInStock("milk", 5));
        assertFalse(inv.isInStock("milk", 6));
        assertFalse(inv.isInStock("milk", 0));
        assertEquals(9, inv.getTotalStock());
    }

    @Test void invalidCapacityRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Inventory(0));
    }
}
