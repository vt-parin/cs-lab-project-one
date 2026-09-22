import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WorkerTest {
    private GroceryStore store;
    private Worker worker;
    private Item milk;
    private Item bread;
    private Item eggs;

    @Before
    public void setUp() {
        store = new GroceryStore(new Inventory(2)); // small capacity to test "inventory full"
        worker = new Worker("worker", "1234", store);
        milk = new Item("Milk", 3.00, 5);
        bread = new Item("Bread", 2.00, 4);
        eggs = new Item("Eggs", 4.00, 3);
    }

    @Test
    public void addStockNormal() {
        assertTrue(worker.addStock(milk));
        assertSame(milk, store.getInventory().findItem("Milk"));
    }

    @Test
    public void addStockInventoryFullRejected() {
        worker.addStock(milk);
        worker.addStock(bread);
        assertFalse(worker.addStock(eggs)); // capacity is 2
    }

    @Test
    public void updateStockNormal() {
        worker.addStock(milk);
        assertTrue(worker.updateStock("Milk", 5));
        assertEquals(10, milk.getStockCount());
    }

    @Test
    public void updateStockBelowZeroRejected() {
        worker.addStock(milk);
        assertFalse(worker.updateStock("Milk", -20));
        assertEquals(5, milk.getStockCount()); // unchanged
    }

    @Test
    public void updatePriceNormal() {
        worker.addStock(milk);
        assertTrue(worker.updatePrice("Milk", 3.75));
        assertEquals(3.75, milk.getPrice(), 0.0001);
    }

    @Test
    public void updatePriceNegativeRejected() {
        worker.addStock(milk);
        assertFalse(worker.updatePrice("Milk", -2.00));
        assertEquals(3.00, milk.getPrice(), 0.0001); // unchanged
    }

    @Test
    public void removeStockNormal() {
        worker.addStock(bread);
        assertTrue(worker.removeStock("Bread"));
        assertNull(store.getInventory().findItem("Bread"));
    }

    @Test
    public void removeStockMissingRejected() {
        assertFalse(worker.removeStock("Bread"));
    }
}
