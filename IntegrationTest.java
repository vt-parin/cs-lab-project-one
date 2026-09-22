/**
 * End-to-end tests for GroceryStore and the whole system working together
 * (login -> create list -> check stock -> purchase -> verify inventory).
 * Plain Java, no JUnit needed.
 */
public class IntegrationTest {
    private static int passed = 0, failed = 0;

    private static void check(String name, boolean ok) {
        if (ok) { passed++; System.out.println("PASS: " + name); }
        else    { failed++; System.out.println("FAIL: " + name); }
    }

    private static GroceryStore newStore() {
        GroceryStore s = new GroceryStore();
        Inventory inv = s.getInventory();
        inv.addItem(new Item("Milk", 3.50, 5));
        inv.addItem(new Item("Bread", 2.25, 10));
        s.addUser(new Customer("alice", "pw1"));
        s.addUser(new Worker("bob", "pw2", inv));
        return s;
    }

    public static void main(String[] args) {
        // ---- login ----
        GroceryStore s = newStore();
        User u = s.login("alice", "pw1");
        check("login normal: correct credentials return the user", u != null && u instanceof Customer);
        check("login: username is case-insensitive", s.login("ALICE", "pw1") != null);
        check("login bad: wrong password returns null", s.login("alice", "wrong") == null);
        check("login bad: unknown user returns null", s.login("nobody", "pw1") == null);
        check("login bad: null inputs return null", s.login(null, null) == null);
        check("login: worker logs in as Worker", s.login("bob", "pw2") instanceof Worker);
        check("addUser bad: duplicate username rejected", !s.addUser(new Customer("Alice", "x")));
        check("addUser bad: empty username rejected", !s.addUser(new Customer("  ", "x")));

        // ---- purchaseList ----
        s = newStore();
        GroceryList list = new GroceryList();
        list.addItem(s.getInventory().findItem("Milk"), 2);
        list.addItem(s.getInventory().findItem("Bread"), 4);
        check("purchaseList normal: succeeds when all in stock", s.purchaseList(list));
        check("purchaseList normal: stock decreased (Milk 5->3, Bread 10->6)",
              s.getInventory().findItem("Milk").getStockCount() == 3
              && s.getInventory().findItem("Bread").getStockCount() == 6);

        s = newStore();
        list = new GroceryList();
        list.addItem(s.getInventory().findItem("Bread"), 4);   // fine
        list.addItem(s.getInventory().findItem("Milk"), 9);    // too many
        check("purchaseList bad: canceled when one item exceeds stock", !s.purchaseList(list));
        check("purchaseList bad: NO inventory changed (Bread still 10, Milk still 5)",
              s.getInventory().findItem("Bread").getStockCount() == 10
              && s.getInventory().findItem("Milk").getStockCount() == 5);
        check("purchaseList bad: null list rejected", !s.purchaseList(null));
        check("purchaseList bad: empty list rejected", !s.purchaseList(new GroceryList()));

        // ---- Full customer flow ----
        s = newStore();
        Customer alice = (Customer) s.login("alice", "pw1");
        alice.addItem(s.getInventory().findItem("milk"), 3);       // lowercase lookup
        check("flow: customer list total = 3 x 3.50 = 10.50",
              Math.abs(alice.getGroceryList().calculateTotal() - 10.50) < 0.0001);
        check("flow: checkout succeeds", alice.checkout(s));
        check("flow: stock updated (Milk 5->2)", s.getInventory().findItem("Milk").getStockCount() == 2);
        check("flow: list cleared after checkout", alice.getGroceryList().isEmpty());

        alice.addItem(s.getInventory().findItem("Milk"), 3);       // only 2 left
        check("flow bad: checkout fails when stock too low", !alice.checkout(s));
        check("flow bad: list kept after failed checkout", alice.getGroceryList().size() == 1);
        check("flow bad: stock untouched after failed checkout", s.getInventory().findItem("Milk").getStockCount() == 2);

        // ---- Shared state: worker and customer see the same store ----
        Worker bob = (Worker) s.login("bob", "pw2");
        check("sync: worker restocks Milk +10", bob.addStock(new Item("Milk", 3.50, 10)));
        check("sync: customer's same list now checks out", alice.checkout(s));
        check("sync: Milk 12 - 3 = 9", s.getInventory().findItem("Milk").getStockCount() == 9);
        check("sync: worker adds new item, customer can find it",
              bob.addStock(new Item("Eggs", 4.25, 12)) && s.getInventory().findItem("EGGS") != null);
        check("sync: worker removes Bread, customer can't find it",
              bob.removeStock("Bread") && s.getInventory().findItem("Bread") == null);
        check("worker bad: remove nonexistent item returns false", !bob.removeStock("Dragon Fruit"));
        check("worker bad: add null item returns false", !bob.addStock(null));

        // ---- Inventory full ----
        Inventory tiny = new Inventory(1);
        check("inventory normal: add when space", tiny.addItem(new Item("Milk", 1, 1)));
        check("inventory bad: add when full rejected", !tiny.addItem(new Item("Eggs", 1, 1)));

        System.out.println("\n" + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
