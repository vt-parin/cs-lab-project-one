/** PLACEHOLDER for Isaac's Worker, written to the spec. Replace with the real one. */
public class Worker extends User {
    private final Inventory inventory;

    public Worker(String username, String password, Inventory inventory) {
        super(username, password);
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory cannot be null.");
        }
        this.inventory = inventory;
    }

    public Worker(String username, String password, GroceryStore store) {
        this(username, password, store == null ? null : store.getInventory());
    }

    /** Adds a new item, or, if the item already exists, adds the given item's stock to it. */
    public boolean addStock(Item item) {
        if (item == null) return false;
        Item existing = inventory.findItem(item.getName());
        if (existing == null) return inventory.addItem(item);
        if (existing == item) return false;
        return existing.updateStock(item.getStockCount());
    }

    public boolean updateStock(String name, int amount) {
        Item item = inventory.findItem(name);
        if (item == null) return false;
        return item.updateStock(amount);
    }

    public boolean removeStock(String name) { return inventory.removeItem(name); }
}
