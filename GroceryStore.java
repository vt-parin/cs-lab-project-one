import java.util.ArrayList;

/**
 * Coordinates store-wide operations: the shared inventory, the registered
 * users, and customer purchases. Every user works against this one store,
 * so inventory changes by workers and purchases by customers are shared.
 */
public class GroceryStore {
    private final Inventory inventory;
    private final ArrayList<User> users;

    public GroceryStore() {
        this(new Inventory());
    }

    public GroceryStore(Inventory inventory) {
        this.inventory = inventory;
        this.users = new ArrayList<>();
    }

    /** @return the store's shared inventory (used by Main and by Worker construction) */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Registers a user.
     * @return false if user is null, has an empty username, or the username is already taken
     */
    public boolean addUser(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return false;
        }
        if (findUser(user.getUsername()) != null) {
            return false;
        }
        return users.add(user);
    }

    /**
     * Finds the matching user and returns them.
     * Usernames are matched case-insensitively; passwords must match exactly.
     * @return the logged-in User, or null if authentication fails
     */
    public User login(String username, String password) {
        User user = findUser(username);
        if (user != null && user.checkPassword(password)) {
            return user;
        }
        return null;
    }

    /**
     * Completes the purchase only if every item is available, then decreases
     * inventory. All-or-nothing: if anything fails, inventory is left unchanged.
     *
     * @return true if the purchase went through; false if it was canceled
     */
    public boolean purchaseList(GroceryList list) {
        if (list == null || !list.canPurchase(inventory)) {
            return false;
        }

        // canPurchase already verified stock, so this should always succeed.
        // The rollback below is a safety net so we can never partially modify inventory.
        ArrayList<ListItem> applied = new ArrayList<>();
        for (ListItem li : list.getItems()) {
            Item stocked = inventory.findItem(li.getItem().getName());
            if (stocked == null || !stocked.updateStock(-li.getQuantity())) {
                for (ListItem done : applied) {
                    inventory.findItem(done.getItem().getName()).updateStock(done.getQuantity());
                }
                return false;
            }
            applied.add(li);
        }
        return true;
    }

    private User findUser(String username) {
        if (username == null) {
            return null;
        }
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username.trim())) {
                return u;
            }
        }
        return null;
    }
}
