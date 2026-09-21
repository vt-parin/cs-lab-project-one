public class Worker extends User {
    private final GroceryStore store;

    public Worker(String username, String password, GroceryStore store) {
        super(username, password);

        if (store == null) {
            throw new IllegalArgumentException("Store cannot be null.");
        }

        this.store = store;
    }

    public boolean addStock(Item item) {
        return store.getInventory().addItem(item);
    }

    public boolean removeStock(String name) {
        return store.getInventory().removeItem(name);
    }

    public boolean updateStock(String name, int amount) {
        Item item = store.getInventory().findItem(name);
        return item != null && item.updateStock(amount);
    }

    public boolean updatePrice(String name, double price) {
        Item item = store.getInventory().findItem(name);
        return item != null && item.setPrice(price);
    }
}
