/**
 * Represents one item and the quantity requested on a grocery list.
 * Keeping quantity here (instead of in Item) lets a list request, for
 * example, 3 apples without duplicating the Item object.
 */
public class ListItem {
    private final Item item;
    private int quantity;

    /**
     * @param item     the store item being requested (must not be null)
     * @param quantity number of units requested (must be at least 1)
     * @throws IllegalArgumentException if item is null or quantity is less than 1
     */
    public ListItem(Item item, int quantity) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
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

    /**
     * Changes the requested quantity.
     * @return true if updated; false (and no change) if newQuantity is less than 1
     */
    public boolean setQuantity(int newQuantity) {
        if (newQuantity < 1) {
            return false;
        }
        this.quantity = newQuantity;
        return true;
    }

    /** @return price of the item multiplied by the requested quantity */
    public double getSubtotal() {
        return item.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return item.getName() + " x" + quantity
                + String.format(" ($%.2f)", getSubtotal());
    }
}
