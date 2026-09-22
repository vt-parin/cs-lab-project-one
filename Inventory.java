import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** PLACEHOLDER for Ian's Inventory, written to the spec. Replace with the real one. */
public class Inventory {
    public static final int DEFAULT_CAPACITY = 100;
    private final ArrayList<Item> items = new ArrayList<>();
    private final int capacity;

    public Inventory() { this(DEFAULT_CAPACITY); }
    public Inventory(int capacity) { this.capacity = capacity; }

    public boolean addItem(Item item) {
        if (item == null || items.size() >= capacity || findItem(item.getName()) != null) return false;
        return items.add(item);
    }

    public boolean removeItem(String name) {
        Item found = findItem(name);
        return found != null && items.remove(found);
    }

    public Item findItem(String name) {
        if (name == null) return null;
        for (Item i : items) if (i.getName().equalsIgnoreCase(name.trim())) return i;
        return null;
    }

    public boolean isInStock(String name, int quantity) {
        Item i = findItem(name);
        return i != null && quantity > 0 && i.getStockCount() >= quantity;
    }

    public int getTotalStock() {
        int total = 0;
        for (Item i : items) total += i.getStockCount();
        return total;
    }

    public int getItemCount() { return items.size(); }

    /** Extra helper (not in the spec table): lets Main display the inventory. */
    public List<Item> getItems() { return Collections.unmodifiableList(items); }
}
