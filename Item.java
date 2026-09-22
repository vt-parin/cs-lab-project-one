/** PLACEHOLDER for Ian's Item, written to the spec so the team can integrate. Replace with the real one. */
public class Item {
    private final String name;
    private double price;
    private int stockCount;

    public Item(String name, double price, int stockCount) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Item name cannot be empty.");
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        if (stockCount < 0) throw new IllegalArgumentException("Stock cannot be negative.");
        this.name = name.trim();
        this.price = price;
        this.stockCount = stockCount;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStockCount() { return stockCount; }

    public boolean setPrice(double price) {
        if (price < 0) return false;
        this.price = price;
        return true;
    }

    /** Changes stock by amount (negative to reduce). Rejected if the result would be negative. */
    public boolean updateStock(int amount) {
        if (stockCount + amount < 0) return false;
        stockCount += amount;
        return true;
    }
}
