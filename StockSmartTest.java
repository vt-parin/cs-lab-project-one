public class StockSmartTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("Running StockSmart tests...\n");

        testItem();
        testInventory();
        testGroceryList();
        testLogin();
        testPurchase();
        testCustomer();
        testWorker();

        System.out.println("\n==============================");
        System.out.println("Tests passed: " + passed);
        System.out.println("Tests failed: " + failed);
        System.out.println("==============================");

        if (failed == 0) {
            System.out.println("All tests passed.");
        }
    }

    private static void testItem() {
        System.out.println("--- Item Tests ---");

        Item milk = new Item("Milk", 3.50, 10);

        check("Item.getName normal",
                milk.getName().equals("Milk"));

        check("Item.getPrice normal",
                approximatelyEqual(milk.getPrice(), 3.50));

        check("Item.getStockCount normal",
                milk.getStockCount() == 10);

        check("Item.setPrice normal",
                milk.setPrice(4.00)
                        && approximatelyEqual(milk.getPrice(), 4.00));

        check("Item.setPrice negative",
                !milk.setPrice(-1.00)
                        && approximatelyEqual(milk.getPrice(), 4.00));

        check("Item.updateStock normal",
                milk.updateStock(5)
                        && milk.getStockCount() == 15);

        check("Item.updateStock below zero",
                !milk.updateStock(-20)
                        && milk.getStockCount() == 15);

        boolean invalidNameRejected = false;
        try {
            new Item("", 1.00, 1);
        } catch (IllegalArgumentException e) {
            invalidNameRejected = true;
        }

        check("Item empty name rejected",
                invalidNameRejected);
    }

    private static void testInventory() {
        System.out.println("\n--- Inventory Tests ---");

        Inventory inventory = new Inventory(2);
        Item milk = new Item("Milk", 3.50, 5);
        Item bread = new Item("Bread", 2.50, 4);
        Item eggs = new Item("Eggs", 4.00, 3);

        check("Inventory.addItem normal",
                inventory.addItem(milk)
                        && inventory.getItemCount() == 1);

        check("Inventory.findItem normal",
                inventory.findItem("Milk") == milk);

        check("Inventory.findItem case insensitive",
                inventory.findItem("mIlK") == milk);

        check("Inventory.findItem missing",
                inventory.findItem("Dragon Fruit") == null);

        check("Inventory.isInStock normal",
                inventory.isInStock("Milk", 2));

        check("Inventory.isInStock insufficient",
                !inventory.isInStock("Milk", 8));

        check("Inventory.isInStock invalid quantity",
                !inventory.isInStock("Milk", -1));

        check("Inventory.addItem second item",
                inventory.addItem(bread));

        check("Inventory full",
                !inventory.addItem(eggs));

        check("Inventory.getItemCount",
                inventory.getItemCount() == 2);

        check("Inventory.getTotalStock",
                inventory.getTotalStock() == 9);

        check("Inventory.removeItem missing",
                !inventory.removeItem("Eggs"));

        check("Inventory.removeItem normal",
                inventory.removeItem("Bread")
                        && inventory.findItem("Bread") == null);
    }

    private static void testGroceryList() {
        System.out.println("\n--- GroceryList Tests ---");

        Inventory inventory = new Inventory();
        Item milk = new Item("Milk", 3.00, 5);
        Item bread = new Item("Bread", 2.00, 10);

        inventory.addItem(milk);
        inventory.addItem(bread);

        GroceryList list = new GroceryList();

        check("GroceryList empty total",
                approximatelyEqual(list.calculateTotal(), 0.00));

        check("GroceryList.addItem normal",
                list.addItem(milk, 3)
                        && list.getItems().size() == 1
                        && list.getItems().get(0).getQuantity() == 3);

        check("GroceryList.addItem existing item updates quantity",
                list.addItem(milk, 1)
                        && list.getItems().size() == 1
                        && list.getItems().get(0).getQuantity() == 4);

        check("GroceryList.addItem negative quantity",
                !list.addItem(bread, -2));

        check("GroceryList.calculateTotal",
                list.addItem(bread, 2)
                        && approximatelyEqual(list.calculateTotal(), 16.00));

        check("GroceryList.canPurchase normal",
                list.canPurchase(inventory));

        check("GroceryList.removeItem missing",
                !list.removeItem("Eggs"));

        check("GroceryList.removeItem normal",
                list.removeItem("Bread"));

        list.clearList();

        check("GroceryList.clearList",
                list.isEmpty());
    }

    private static void testLogin() {
        System.out.println("\n--- Login Tests ---");

        GroceryStore store = new GroceryStore();
        Customer customer = new Customer("ian", "abc123");
        store.addUser(customer);

        check("GroceryStore.login normal",
                store.login("ian", "abc123") == customer);

        check("GroceryStore.login username case insensitive",
                store.login("IAN", "abc123") == customer);

        check("GroceryStore.login wrong password",
                store.login("ian", "wrong") == null);

        check("GroceryStore.login nonexistent user",
                store.login("nobody", "abc123") == null);
    }

    private static void testPurchase() {
        System.out.println("\n--- Purchase Tests ---");

        GroceryStore store = new GroceryStore();
        Item milk = new Item("Milk", 3.00, 5);
        Item bread = new Item("Bread", 2.00, 4);

        store.getInventory().addItem(milk);
        store.getInventory().addItem(bread);

        GroceryList validList = new GroceryList();
        validList.addItem(milk, 2);
        validList.addItem(bread, 1);

        check("GroceryStore.purchaseList normal",
                store.purchaseList(validList)
                        && milk.getStockCount() == 3
                        && bread.getStockCount() == 3);

        GroceryList invalidList = new GroceryList();
        invalidList.addItem(milk, 2);
        invalidList.addItem(bread, 10);

        int milkBefore = milk.getStockCount();
        int breadBefore = bread.getStockCount();

        check("GroceryStore.purchaseList atomic failure",
                !store.purchaseList(invalidList)
                        && milk.getStockCount() == milkBefore
                        && bread.getStockCount() == breadBefore);

        GroceryList emptyList = new GroceryList();

        check("GroceryStore.purchaseList empty list",
                !store.purchaseList(emptyList));
    }

    private static void testCustomer() {
        System.out.println("\n--- Customer Tests ---");

        GroceryStore store = new GroceryStore();
        Item apple = new Item("Apple", 1.00, 10);
        store.getInventory().addItem(apple);

        Customer customer = new Customer("customer", "1234");

        check("Customer.addItem normal",
                customer.addItem(apple, 3)
                        && customer.getGroceryList().getItems().size() == 1);

        check("Customer.addItem bad quantity",
                !customer.addItem(apple, -2));

        check("Customer.removeItem missing",
                !customer.removeItem("Milk"));

        check("Customer.checkout normal",
                customer.checkout(store)
                        && apple.getStockCount() == 7
                        && customer.getGroceryList().isEmpty());

        customer.addItem(apple, 20);
        int stockBefore = apple.getStockCount();

        check("Customer.checkout unavailable stock",
                !customer.checkout(store)
                        && apple.getStockCount() == stockBefore
                        && !customer.getGroceryList().isEmpty());
    }

    private static void testWorker() {
        System.out.println("\n--- Worker Tests ---");

        GroceryStore store = new GroceryStore(new Inventory(2));
        Worker worker = new Worker("worker", "1234", store);

        Item milk = new Item("Milk", 3.00, 5);
        Item bread = new Item("Bread", 2.00, 4);
        Item eggs = new Item("Eggs", 4.00, 3);

        check("Worker.addStock normal",
                worker.addStock(milk)
                        && store.getInventory().findItem("Milk") == milk);

        check("Worker.updateStock normal",
                worker.updateStock("Milk", 5)
                        && milk.getStockCount() == 10);

        check("Worker.updateStock below zero",
                !worker.updateStock("Milk", -20)
                        && milk.getStockCount() == 10);

        check("Worker.updatePrice normal",
                worker.updatePrice("Milk", 3.75)
                        && approximatelyEqual(milk.getPrice(), 3.75));

        check("Worker.updatePrice negative",
                !worker.updatePrice("Milk", -2.00)
                        && approximatelyEqual(milk.getPrice(), 3.75));

        check("Worker.addStock second item",
                worker.addStock(bread));

        check("Worker.addStock inventory full",
                !worker.addStock(eggs));

        check("Worker.removeStock normal",
                worker.removeStock("Bread")
                        && store.getInventory().findItem("Bread") == null);

        check("Worker.removeStock missing",
                !worker.removeStock("Bread"));
    }

    private static void check(String testName, boolean condition) {
        if (condition) {
            System.out.println("[PASS] " + testName);
            passed++;
        } else {
            System.out.println("[FAIL] " + testName);
            failed++;
        }
    }

    private static boolean approximatelyEqual(double first, double second) {
        return Math.abs(first - second) < 0.0001;
    }
}
