import static org.junit.Assert.*;
import org.junit.Test;

public class ItemTest {

    @Test
    public void getNameNormal() {
        Item milk = new Item("Milk", 3.50, 10);
        assertEquals("Milk", milk.getName());
    }

    @Test
    public void getPriceNormal() {
        Item milk = new Item("Milk", 3.50, 10);
        assertEquals(3.50, milk.getPrice(), 0.0001);
    }

    @Test
    public void getStockCountNormal() {
        Item milk = new Item("Milk", 3.50, 10);
        assertEquals(10, milk.getStockCount());
    }

    @Test
    public void setPriceNormal() {
        Item milk = new Item("Milk", 3.50, 10);
        assertTrue(milk.setPrice(4.00));
        assertEquals(4.00, milk.getPrice(), 0.0001);
    }

    @Test
    public void setPriceNegativeRejected() {
        Item milk = new Item("Milk", 3.50, 10);
        assertFalse(milk.setPrice(-1.00));
        assertEquals(3.50, milk.getPrice(), 0.0001); // unchanged
    }

    @Test
    public void updateStockNormal() {
        Item milk = new Item("Milk", 3.50, 10);
        assertTrue(milk.updateStock(5));
        assertEquals(15, milk.getStockCount());
    }

    @Test
    public void updateStockBelowZeroRejected() {
        Item milk = new Item("Milk", 3.50, 10);
        assertFalse(milk.updateStock(-20));
        assertEquals(10, milk.getStockCount()); // unchanged
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyNameRejected() {
        new Item("", 1.00, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativePriceAtCreationRejected() {
        new Item("Milk", -1.00, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeStockAtCreationRejected() {
        new Item("Milk", 1.00, -5);
    }
}
