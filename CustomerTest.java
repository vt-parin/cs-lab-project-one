import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CustomerTest {
    @Test void addAndRemoveItems() {
        Customer customer = new Customer("customer", "1234");
        Item apple = new Item("Apple", 1, 10);
        assertTrue(customer.addItem(apple, 2));
        assertFalse(customer.addItem(apple, -1));
        assertTrue(customer.removeItem("apple"));
        assertTrue(customer.getGroceryList().isEmpty());
    }

    @Test void checkoutSuccessClearsListAndUpdatesStock() {
        GroceryStore store = new GroceryStore();
        Item apple = new Item("Apple", 1, 10);
        store.getInventory().addItem(apple);
        Customer customer = new Customer("customer", "1234");
        customer.addItem(apple, 3);
        assertTrue(customer.checkout(store));
        assertEquals(7, apple.getStockCount());
        assertTrue(customer.getGroceryList().isEmpty());
    }

    @Test void failedCheckoutDoesNotClearList() {
        GroceryStore store = new GroceryStore();
        Item apple = new Item("Apple", 1, 2);
        store.getInventory().addItem(apple);
        Customer customer = new Customer("customer", "1234");
        customer.addItem(apple, 3);
        assertFalse(customer.checkout(store));
        assertFalse(customer.getGroceryList().isEmpty());
        assertEquals(2, apple.getStockCount());
    }

    @Test void checkoutRejectsNullOrEmpty() {
        Customer customer = new Customer("customer", "1234");
        assertFalse(customer.checkout(null));
        assertFalse(customer.checkout(new GroceryStore()));
    }
}
