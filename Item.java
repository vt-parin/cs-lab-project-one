public class Item {
    private String name;
    private double price;
    private int stockCount;

    public Item(String name, double price, int stockCount) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (stockCount < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }

        this.name = name.trim();
        this.price = price;
        this.stockCount = stockCount;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStockCount() {
        return stockCount;
    }

    public boolean setPrice(double price) {
        if (price < 0) {
            return false;
        }
        this.price = price;
        return true;
    }

    public boolean updateStock(int amount) {
        if (stockCount + amount < 0) {
            return false;
        }
        stockCount += amount;
        return true;
    }

    @Override
    public String toString() {
        return name + " - $" + String.format("%.2f", price) + " - Stock: " + stockCount;
    }
}
