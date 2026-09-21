import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventory {
    private final ArrayList<Item> items;
    private final int maxCapacity;

    public Inventory() {
        this(100);
    }

    public Inventory(int maxCapacity) {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Maximum capacity must be positive.");
        }
        this.items = new ArrayList<>();
        this.maxCapacity = maxCapacity;
    }

    public boolean addItem(Item item) {
        if (item == null || items.size() >= maxCapacity) {
            return false;
        }

        if (findItem(item.getName()) != null) {
            return false;
        }

        items.add(item);
        return true;
    }

    public boolean removeItem(String name) {
        Item item = findItem(name);
        if (item == null) {
            return false;
        }
        return items.remove(item);
    }

    public Item findItem(String name) {
        if (name == null) {
            return null;
        }

        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name.trim())) {
                return item;
            }
        }
        return null;
    }

    public boolean isInStock(String name, int quantity) {
        if (quantity <= 0) {
            return false;
        }

        Item item = findItem(name);
        return item != null && item.getStockCount() >= quantity;
    }

    public int getTotalStock() {
        int total = 0;
        for (Item item : items) {
            total += item.getStockCount();
        }
        return total;
    }

    public int getItemCount() {
        return items.size();
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
}
