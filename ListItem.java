public class ListItem {
    private final Item item;
    private int quantity;

    public ListItem(Item item, int quantity) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean setQuantity(int quantity) {
        if (quantity <= 0) {
            return false;
        }
        this.quantity = quantity;
        return true;
    }

    @Override
    public String toString() {
        return item.getName() + " x" + quantity;
    }
}
