/** PLACEHOLDER for Isaac's Customer, written to the spec. Replace with the real one. */
public class Customer extends User {
    private final GroceryList groceryList = new GroceryList();

    public Customer(String username, String password) { super(username, password); }

    public GroceryList getGroceryList() { return groceryList; }
    public boolean addItem(Item item, int quantity) { return groceryList.addItem(item, quantity); }
    public boolean removeItem(String name) { return groceryList.removeItem(name); }

    /** Buys the list. The list is only cleared if the purchase succeeds. */
    public boolean checkout(GroceryStore store) {
        if (store == null || !store.purchaseList(groceryList)) return false;
        groceryList.clearList();
        return true;
    }
}
