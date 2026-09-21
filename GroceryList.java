import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroceryList {
    private final ArrayList<ListItem> items;

    public GroceryList() {
        items = new ArrayList<>();
    }

    public boolean addItem(Item item, int quantity) {
        if (item == null || quantity <= 0) {
            return false;
        }

        ListItem existing = findListItem(item.getName());
        if (existing != null) {
            return existing.setQuantity(existing.getQuantity() + quantity);
        }

        items.add(new ListItem(item, quantity));
        return true;
    }

    public boolean removeItem(String name) {
        ListItem listItem = findListItem(name);
        if (listItem == null) {
            return false;
        }
        return items.remove(listItem);
    }

    public double calculateTotal() {
        double total = 0.0;
        for (ListItem listItem : items) {
            total += listItem.getItem().getPrice() * listItem.getQuantity();
        }
        return total;
    }

    public boolean canPurchase(Inventory inventory) {
        if (inventory == null) {
            return false;
        }

        for (ListItem listItem : items) {
            if (!inventory.isInStock(
                    listItem.getItem().getName(),
                    listItem.getQuantity())) {
                return false;
            }
        }
        return true;
    }

    public void clearList() {
        items.clear();
    }

    public List<ListItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    private ListItem findListItem(String name) {
        if (name == null) {
            return null;
        }

        for (ListItem listItem : items) {
            if (listItem.getItem().getName().equalsIgnoreCase(name.trim())) {
                return listItem;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if (items.isEmpty()) {
            return "Grocery list is empty.";
        }

        StringBuilder builder = new StringBuilder();
        for (ListItem listItem : items) {
            builder.append(listItem)
                   .append(" - $")
                   .append(String.format("%.2f",
                           listItem.getItem().getPrice() * listItem.getQuantity()))
                   .append(System.lineSeparator());
        }

        builder.append("Total: $")
               .append(String.format("%.2f", calculateTotal()));

        return builder.toString();
    }
}
