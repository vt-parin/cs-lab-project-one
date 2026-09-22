import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class GroceryListTest {
    @Test void addAndUpdateQuantity() {
        GroceryList list = new GroceryList();
        Item milk = new Item("Milk", 3, 5);
        assertTrue(list.addItem(milk, 2));
        assertTrue(list.addItem(milk, 3));
        assertEquals(1, list.getItems().size());
        assertEquals(5, list.getItems().get(0).getQuantity());
    }

    @Test void invalidAddRejected() {
        GroceryList list = new GroceryList();
        Item milk = new Item("Milk", 3, 5);
        assertFalse(list.addItem(null, 1));
        assertFalse(list.addItem(milk, 0));
        assertFalse(list.addItem(milk, -1));
    }

    @Test void removeAndClear() {
        GroceryList list = new GroceryList();
        list.addItem(new Item("Milk", 3, 5), 2);
        assertFalse(list.removeItem("Bread"));
        assertTrue(list.removeItem("mIlK"));
        assertTrue(list.isEmpty());
    }

    @Test void totalAndPurchaseCheck() {
        Inventory inv = new Inventory();
        Item milk = new Item("Milk", 3, 5);
        Item bread = new Item("Bread", 2, 4);
        inv.addItem(milk); inv.addItem(bread);
        GroceryList list = new GroceryList();
        list.addItem(milk, 2); list.addItem(bread, 1);
        assertEquals(8, list.calculateTotal(), 0.0001);
        assertTrue(list.canPurchase(inv));
        list.addItem(milk, 10);
        assertFalse(list.canPurchase(inv));
    }

    @Test void emptyAndString() {
        GroceryList list = new GroceryList();
        assertTrue(list.isEmpty());
        assertEquals(0, list.calculateTotal(), 0.0001);
        assertEquals("Grocery list is empty.", list.toString());
    }
}
