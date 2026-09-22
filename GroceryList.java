import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A customer's list of requested items and quantities.
 * Each distinct item appears once; requesting the same item again
 * increases its quantity instead of creating a duplicate entry.
 */
public class GroceryList {
    private final ArrayList<ListItem> items;

    public GroceryList() {
        items = new ArrayList<>();
    }

    /**
     * Adds an item to the list. If the item is already on the list
     * (case-insensitive name match), the quantity is added to the
     * existing quantity.
     *
     * @return true if the list was updated; false if item is null or
     *         quantity is less than 1 (list is left unchanged)
     */
    public boolean addItem(Item item, int quantity) {
        if (item == null || quantity < 1) {
            return false;
        }
        ListItem existing = findListItem(item.getName());
        if (existing != null) {
            return existing.setQuantity(existing.getQuantity() + quantity);
        }
        items.add(new ListItem(item, quantity));
        return true;
    }

    /**
     * Removes the matching item (case-insensitive) from the list.
     *
     * @return true if removed; false if not on the list
     */
    public boolean removeItem(String name) {
        ListItem existing = findListItem(name);
        if (existing == null) {
            return false;
        }
        return items.remove(existing);
    }

    /** @return sum of price x quantity for every item; 0 for an empty list */
    public double calculateTotal() {
        double total = 0.0;
        for (ListItem li : items) {
            total += li.getSubtotal();
        }
        return total;
    }

    /**
     * Checks whether every requested item has enough stock.
     * Does NOT change inventory. An empty list cannot be purchased.
     *
     * @return true only if the list is non-empty and all items are in stock
     */
    public boolean canPurchase(Inventory inventory) {
        if (inventory == null || items.isEmpty()) {
            return false;
        }
        for (ListItem li : items) {
            if (!inventory.isInStock(li.getItem().getName(), li.getQuantity())) {
                return false;
            }
        }
        return true;
    }

    /** Removes all items, leaving the list empty. */
    public void clearList() {
        items.clear();
    }

    // ---- Helpers (not in the spec table; needed by GroceryStore / Main) ----

    /** @return read-only view of the list's entries, e.g. for purchaseList() and display */
    public List<ListItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    /** @return number of different items on the list */
    public int size() {
        return items.size();
    }

    /** Finds an entry by case-insensitive name; null if not found. */
    private ListItem findListItem(String name) {
        if (name == null) {
            return null;
        }
        for (ListItem li : items) {
            if (li.getItem().getName().equalsIgnoreCase(name.trim())) {
                return li;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if (items.isEmpty()) {
            return "(grocery list is empty)";
        }
        StringBuilder sb = new StringBuilder();
        for (ListItem li : items) {
            sb.append(li).append("\n");
        }
        sb.append(String.format("Total: $%.2f", calculateTotal()));
        return sb.toString();
    }
}
