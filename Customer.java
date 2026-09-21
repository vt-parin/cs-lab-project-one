public class Customer extends User {
    private final GroceryList groceryList;

    public Customer(String username, String password) {
        super(username, password);
        groceryList = new GroceryList();
    }

    public GroceryList getGroceryList() {
        return groceryList;
    }

    public boolean checkout(GroceryStore store) {
        if (store == null || groceryList.isEmpty()) {
            return false;
        }

        boolean success = store.purchaseList(groceryList);
        if (success) {
            groceryList.clearList();
        }
        return success;
    }

    public boolean removeItem(String name) {
        return groceryList.removeItem(name);
    }

    public boolean addItem(Item item, int quantity) {
        return groceryList.addItem(item, quantity);
    }
}
