import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroceryStore {
    private final Inventory inventory;
    private final ArrayList<User> users;

    public GroceryStore() {
        this(new Inventory());
    }

    public GroceryStore(Inventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory cannot be null.");
        }

        this.inventory = inventory;
        this.users = new ArrayList<>();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean addUser(User user) {
        if (user == null || findUser(user.getUsername()) != null) {
            return false;
        }

        users.add(user);
        return true;
    }

    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username.trim())
                    && user.checkPassword(password)) {
                return user;
            }
        }

        return null;
    }

    public boolean purchaseList(GroceryList list) {
        if (list == null || list.isEmpty()) {
            return false;
        }

        // First validate the entire transaction.
        // Nothing is changed unless every item is available.
        if (!list.canPurchase(inventory)) {
            return false;
        }

        // Then perform the changes.
        for (ListItem listItem : list.getItems()) {
            Item storeItem = inventory.findItem(listItem.getItem().getName());

            // canPurchase() already guaranteed these are valid.
            storeItem.updateStock(-listItem.getQuantity());
        }

        return true;
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    private User findUser(String username) {
        if (username == null) {
            return null;
        }

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username.trim())) {
                return user;
            }
        }

        return null;
    }
}
