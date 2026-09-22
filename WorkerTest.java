import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class WorkerTest {
    @Test void addAndRemoveStockItem() {
        GroceryStore store = new GroceryStore();
        Worker worker = new Worker("worker", "1234", store);
        Item milk = new Item("Milk", 3, 5);
        assertTrue(worker.addStock(milk));
        assertSame(milk, store.getInventory().findItem("milk"));
        assertTrue(worker.removeStock("MILK"));
        assertNull(store.getInventory().findItem("Milk"));
    }

    @Test void updateStock() {
        GroceryStore store = new GroceryStore();
        Worker worker = new Worker("worker", "1234", store);
        Item milk = new Item("Milk", 3, 5);
        worker.addStock(milk);
        assertTrue(worker.updateStock("milk", 3));
        assertEquals(8, milk.getStockCount());
        assertFalse(worker.updateStock("milk", -20));
        assertEquals(8, milk.getStockCount());
        assertFalse(worker.updateStock("Bread", 1));
    }

    @Test void updatePrice() {
        GroceryStore store = new GroceryStore();
        Worker worker = new Worker("worker", "1234", store);
        Item milk = new Item("Milk", 3, 5);
        worker.addStock(milk);
        assertTrue(worker.updatePrice("MILK", 4.25));
        assertEquals(4.25, milk.getPrice());
        assertFalse(worker.updatePrice("Milk", -1));
        assertFalse(worker.updatePrice("Bread", 2));
    }

    @Test void nullStoreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Worker("worker", "1234", null));
    }
}
