import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InventoryTest {
    private Inventory inventory;
    private Item milk;
    private Item bread;
    private Item eggs;

    @Before
    public void setUp() {
        inventory = new Inventory(2); // small capacity to test "inventory full"
        milk = new Item("Milk", 3.50, 5);
        bread = new Item("Bread", 2.50, 4);
        eggs = new Item("Eggs", 4.00, 3);
    }

    @Test
    public void addItemNormal() {
        assertTrue(inventory.addItem(milk));
        assertEquals(1, inventory.getItemCount());
    }

    @Test
    public void addItemFullRejected() {
        inventory.addItem(milk);
        inventory.addItem(bread);
        assertFalse(inventory.addItem(eggs)); // capacity is 2
        assertEquals(2, inventory.getItemCount());
    }

    @Test
    public void addItemDuplicateRejected() {
        inventory.addItem(milk);
        assertFalse(inventory.addItem(new Item("milk", 1.00, 1))); // same name, different case
    }

    @Test
    public void findItemNormal() {
        inventory.addItem(milk);
        assertSame(milk, inventory.findItem("Milk"));
    }

    @Test
    public void findItemCaseInsensitive() {
        inventory.addItem(milk);
        assertSame(milk, inventory.findItem("mIlK"));
    }

    @Test
    public void findItemMissingReturnsNull() {
        assertNull(inventory.findItem("Dragon Fruit"));
    }

    @Test
    public void isInStockNormal() {
        inventory.addItem(milk);
        assertTrue(inventory.isInStock("Milk", 2));
    }

    @Test
    public void isInStockInsufficientRejected() {
        inventory.addItem(milk);
        assertFalse(inventory.isInStock("Milk", 8));
    }

    @Test
    public void isInStockInvalidQuantityRejected() {
        inventory.addItem(milk);
        assertFalse(inventory.isInStock("Milk", -1));
    }

    @Test
    public void getTotalStockNormal() {
        inventory.addItem(milk); // 5
        inventory.addItem(bread); // 4
        assertEquals(9, inventory.getTotalStock());
    }

    @Test
    public void removeItemMissingRejected() {
        assertFalse(inventory.removeItem("Eggs"));
    }

    @Test
    public void removeItemNormal() {
        inventory.addItem(bread);
        assertTrue(inventory.removeItem("Bread"));
        assertNull(inventory.findItem("Bread"));
    }
}
