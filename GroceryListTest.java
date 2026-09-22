import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GroceryListTest {
    private Inventory inventory;
    private Item milk;
    private Item bread;
    private GroceryList list;

    @Before
    public void setUp() {
        inventory = new Inventory();
        milk = new Item("Milk", 3.00, 5);
        bread = new Item("Bread", 2.00, 10);
        inventory.addItem(milk);
        inventory.addItem(bread);
        list = new GroceryList();
    }

    @Test
    public void emptyListTotalIsZero() {
        assertEquals(0.00, list.calculateTotal(), 0.0001);
    }

    @Test
    public void addItemNormal() {
        assertTrue(list.addItem(milk, 3));
        assertEquals(1, list.getItems().size());
        assertEquals(3, list.getItems().get(0).getQuantity());
    }

    @Test
    public void addItemExistingMergesQuantity() {
        list.addItem(milk, 3);
        assertTrue(list.addItem(milk, 1));
        assertEquals(1, list.getItems().size()); // still one entry
        assertEquals(4, list.getItems().get(0).getQuantity());
    }

    @Test
    public void addItemNegativeQuantityRejected() {
        assertFalse(list.addItem(bread, -2));
    }

    @Test
    public void addItemNullRejected() {
        assertFalse(list.addItem(null, 1));
    }

    @Test
    public void calculateTotalNormal() {
        list.addItem(milk, 4);   // 12.00
        list.addItem(bread, 2);  // 4.00
        assertEquals(16.00, list.calculateTotal(), 0.0001);
    }

    @Test
    public void canPurchaseNormal() {
        list.addItem(milk, 2);
        list.addItem(bread, 3);
        assertTrue(list.canPurchase(inventory));
    }

    @Test
    public void canPurchaseInsufficientStockRejected() {
        list.addItem(milk, 999);
        assertFalse(list.canPurchase(inventory));
    }

    @Test
    public void removeItemMissingRejected() {
        assertFalse(list.removeItem("Eggs"));
    }

    @Test
    public void removeItemNormal() {
        list.addItem(bread, 1);
        assertTrue(list.removeItem("Bread"));
    }

    @Test
    public void clearListEmptiesIt() {
        list.addItem(milk, 1);
        list.clearList();
        assertTrue(list.isEmpty());
    }
}
