import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GroceryStoreTest {
    private GroceryStore store;
    private Customer customer;
    private Item milk;
    private Item bread;

    @Before
    public void setUp() {
        store = new GroceryStore();
        customer = new Customer("ian", "abc123");
        store.addUser(customer);

        milk = new Item("Milk", 3.00, 5);
        bread = new Item("Bread", 2.00, 4);
        store.getInventory().addItem(milk);
        store.getInventory().addItem(bread);
    }

    // ---- login ----

    @Test
    public void loginNormal() {
        assertSame(customer, store.login("ian", "abc123"));
    }

    @Test
    public void loginUsernameCaseInsensitive() {
        assertSame(customer, store.login("IAN", "abc123"));
    }

    @Test
    public void loginWrongPasswordRejected() {
        assertNull(store.login("ian", "wrong"));
    }

    @Test
    public void loginNonexistentUserRejected() {
        assertNull(store.login("nobody", "abc123"));
    }

    // ---- purchaseList ----

    @Test
    public void purchaseListNormal() {
        GroceryList list = new GroceryList();
        list.addItem(milk, 2);
        list.addItem(bread, 1);

        assertTrue(store.purchaseList(list));
        assertEquals(3, milk.getStockCount());
        assertEquals(3, bread.getStockCount());
    }

    @Test
    public void purchaseListAtomicOnFailure() {
        GroceryList list = new GroceryList();
        list.addItem(milk, 2);
        list.addItem(bread, 10); // more than available

        int milkBefore = milk.getStockCount();
        int breadBefore = bread.getStockCount();

        assertFalse(store.purchaseList(list));
        assertEquals(milkBefore, milk.getStockCount()); // unchanged
        assertEquals(breadBefore, bread.getStockCount()); // unchanged
    }

    @Test
    public void purchaseListEmptyRejected() {
        assertFalse(store.purchaseList(new GroceryList()));
    }

    @Test
    public void addUserDuplicateRejected() {
        assertFalse(store.addUser(new Customer("Ian", "different-password")));
    }
}
