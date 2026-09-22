/**
 * Simple standalone tests for ListItem and GroceryList (no JUnit needed).
 * Covers the normal + bad/boundary cases from the spec's test plan.
 */
public class GroceryListTest {
    private static int passed = 0, failed = 0;

    private static void check(String name, boolean condition) {
        if (condition) { passed++; System.out.println("PASS: " + name); }
        else           { failed++; System.out.println("FAIL: " + name); }
    }

    private static boolean close(double a, double b) { return Math.abs(a - b) < 0.0001; }

    public static void main(String[] args) {
        Item milk  = new Item("Milk", 3.50, 5);
        Item bread = new Item("Bread", 2.25, 10);

        // ---- GroceryList.addItem ----
        GroceryList list = new GroceryList();
        check("addItem normal: 3 Milk to empty list returns true", list.addItem(milk, 3));
        check("addItem normal: list has Milk x3",
              list.size() == 1 && list.getItems().get(0).getQuantity() == 3);
        check("addItem bad: -2 Milk rejected", !list.addItem(milk, -2));
        check("addItem bad: list unchanged after -2",
              list.size() == 1 && list.getItems().get(0).getQuantity() == 3);
        check("addItem bad: 0 quantity rejected", !list.addItem(milk, 0));
        check("addItem bad: null item rejected", !list.addItem(null, 1));
        list.addItem(new Item("MILK", 3.50, 5), 2);
        check("addItem: same item (different case) merges to x5, one entry",
              list.size() == 1 && list.getItems().get(0).getQuantity() == 5);

        // ---- GroceryList.removeItem ----
        check("removeItem normal: 'milk' (lowercase) removed", list.removeItem("milk"));
        check("removeItem normal: list now empty", list.isEmpty());
        check("removeItem bad: item not on list returns false", !list.removeItem("Dragon Fruit"));
        check("removeItem bad: null name returns false", !list.removeItem(null));

        // ---- GroceryList.calculateTotal ----
        check("calculateTotal boundary: empty list is 0", close(list.calculateTotal(), 0.0));
        list.addItem(milk, 2);   // 7.00
        list.addItem(bread, 3);  // 6.75
        check("calculateTotal normal: 2 milk + 3 bread = 13.75", close(list.calculateTotal(), 13.75));

        // ---- GroceryList.canPurchase ----
        Inventory inv = new Inventory();
        inv.addItem(new Item("Milk", 3.50, 5));
        inv.addItem(new Item("Bread", 2.25, 10));
        check("canPurchase normal: everything in stock", list.canPurchase(inv));
        list.addItem(milk, 10); // milk now x12 > 5
        check("canPurchase bad: one item exceeds stock", !list.canPurchase(inv));
        check("canPurchase does not change inventory",
              inv.findItem("Milk").getStockCount() == 5);
        check("canPurchase boundary: empty list is false",
              !new GroceryList().canPurchase(inv));
        check("canPurchase bad: null inventory is false", !list.canPurchase(null));

        // ---- GroceryList.clearList ----
        list.clearList();
        check("clearList: list is empty afterwards", list.isEmpty());

        // ---- ListItem ----
        ListItem li = new ListItem(bread, 4);
        check("ListItem subtotal = 2.25 x 4 = 9.00", close(li.getSubtotal(), 9.0));
        check("ListItem setQuantity valid", li.setQuantity(6) && li.getQuantity() == 6);
        check("ListItem setQuantity invalid rejected", !li.setQuantity(0) && li.getQuantity() == 6);
        boolean threw = false;
        try { new ListItem(bread, -1); } catch (IllegalArgumentException e) { threw = true; }
        check("ListItem constructor rejects negative quantity", threw);

        System.out.println("\n" + passed + " passed, " + failed + " failed");
    }
}
