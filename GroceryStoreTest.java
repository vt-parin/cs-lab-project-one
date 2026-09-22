import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class GroceryStoreTest {
    @Test void constructorAndInventory() {
        Inventory inv = new Inventory(5);
        GroceryStore store = new GroceryStore(inv);
        assertSame(inv, store.getInventory());
        assertTrue(store.getUsers().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> new GroceryStore(null));
    }

    @Test void addUserAndLogin() {
        GroceryStore store = new GroceryStore();
        User user = new User("Alice", "pass");
        assertTrue(store.addUser(user));
        assertFalse(store.addUser(new User("alice", "other")));
        assertSame(user, store.login("ALICE", "pass"));
        assertNull(store.login("Alice", "wrong"));
        assertNull(store.login("nobody", "pass"));
        assertNull(store.login(null, "pass"));
    }

    @Test void purchaseListUpdatesAllItems() {
        GroceryStore store = new GroceryStore();
        Item milk = new Item("Milk", 3, 5);
        Item bread = new Item("Bread", 2, 4);
        store.getInventory().addItem(milk);
        store.getInventory().addItem(bread);
        GroceryList list = new GroceryList();
        list.addItem(milk, 2); list.addItem(bread, 1);
        assertTrue(store.purchaseList(list));
        assertEquals(3, milk.getStockCount());
        assertEquals(3, bread.getStockCount());
    }

    @Test void failedPurchaseIsAtomic() {
        GroceryStore store = new GroceryStore();
        Item milk = new Item("Milk", 3, 5);
        Item bread = new Item("Bread", 2, 1);
        store.getInventory().addItem(milk); store.getInventory().addItem(bread);
        GroceryList list = new GroceryList();
        list.addItem(milk, 2); list.addItem(bread, 2);
        assertFalse(store.purchaseList(list));
        assertEquals(5, milk.getStockCount());
        assertEquals(1, bread.getStockCount());
    }

    @Test void emptyOrNullPurchaseRejected() {
        GroceryStore store = new GroceryStore();
        assertFalse(store.purchaseList(null));
        assertFalse(store.purchaseList(new GroceryList()));
    }
}
