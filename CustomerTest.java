import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CustomerTest {
    private GroceryStore store;
    private Customer customer;
    private Item apple;

    @Before
    public void setUp() {
        store = new GroceryStore();
        apple = new Item("Apple", 1.00, 10);
        store.getInventory().addItem(apple);
        customer = new Customer("customer", "1234");
    }

    @Test
    public void addItemNormal() {
        assertTrue(customer.addItem(apple, 3));
        assertEquals(1, customer.getGroceryList().getItems().size());
    }

    @Test
    public void addItemBadQuantityRejected() {
        assertFalse(customer.addItem(apple, -2));
    }

    @Test
    public void removeItemMissingRejected() {
        assertFalse(customer.removeItem("Milk"));
    }

    @Test
    public void checkoutNormal() {
        customer.addItem(apple, 3);
        assertTrue(customer.checkout(store));
        assertEquals(7, apple.getStockCount());
        assertTrue(customer.getGroceryList().isEmpty());
    }

    @Test
    public void checkoutUnavailableStockRejected() {
        customer.addItem(apple, 20); // more than the 10 in stock
        int stockBefore = apple.getStockCount();

        assertFalse(customer.checkout(store));
        assertEquals(stockBefore, apple.getStockCount()); // unchanged
        assertFalse(customer.getGroceryList().isEmpty());  // list preserved, not cleared
    }
}
