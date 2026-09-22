import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * StockSmart user interface. Main only prompts and displays messages;
 * the business rules live in GroceryStore, Inventory, GroceryList, etc.
 */
public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static GroceryStore store;

    public static void main(String[] args) {
        store = new GroceryStore();
        seedStore();
        System.out.println("=== Welcome to StockSmart ===");
        try {
            startMenu();
        } catch (NoSuchElementException e) {
            System.out.println("\nInput ended. Goodbye!");
        }
    }

    // ------------------------------------------------------------------
    // Setup
    // ------------------------------------------------------------------

    /** Sample data so the program is usable right away. */
    private static void seedStore() {
        Inventory inv = store.getInventory();
        inv.addItem(new Item("Milk", 3.50, 20));
        inv.addItem(new Item("Bread", 2.25, 15));
        inv.addItem(new Item("Apples", 0.99, 50));
        inv.addItem(new Item("Eggs", 4.25, 12));
        inv.addItem(new Item("Cheese", 5.75, 8));
        store.addUser(new Customer("customer", "pass123"));
        store.addUser(new Worker("worker", "pass123", inv));
    }

    // ------------------------------------------------------------------
    // Start menu
    // ------------------------------------------------------------------

    private static void startMenu() {
        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Log in");
            System.out.println("2. Create account");
            System.out.println("3. Quit");
            String choice = prompt("Choose an option: ");
            switch (choice) {
                case "1": handleLogin(); break;
                case "2": handleCreateAccount(); break;
                case "3": System.out.println("Goodbye!"); return;
                default:  System.out.println("Please enter 1, 2, or 3.");
            }
        }
    }

    private static void handleLogin() {
        String username = prompt("Username: ");
        String password = prompt("Password: ");
        User user = store.login(username, password);
        if (user == null) {
            System.out.println("Invalid username or password. Please try again.");
        } else if (user instanceof Customer) {
            System.out.println("Welcome, " + user.getUsername() + "! (customer)");
            customerMenu((Customer) user);
        } else if (user instanceof Worker) {
            System.out.println("Welcome, " + user.getUsername() + "! (worker)");
            workerMenu((Worker) user);
        }
    }

    private static void handleCreateAccount() {
        String username = prompt("Choose a username: ");
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }
        String password = prompt("Choose a password: ");
        if (password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }
        String role = prompt("Role - 1 for customer, 2 for worker: ");
        User user;
        if (role.equals("1")) {
            user = new Customer(username, password);
        } else if (role.equals("2")) {
            user = new Worker(username, password, store.getInventory());
        } else {
            System.out.println("Invalid role. Account not created.");
            return;
        }
        if (store.addUser(user)) {
            System.out.println("Account created! You can now log in.");
        } else {
            System.out.println("That username is already taken.");
        }
    }

    // ------------------------------------------------------------------
    // Customer menu
    // ------------------------------------------------------------------

    private static void customerMenu(Customer customer) {
        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. Browse store items");
            System.out.println("2. Search for an item");
            System.out.println("3. Add item to my list");
            System.out.println("4. Remove item from my list");
            System.out.println("5. View my list");
            System.out.println("6. Check out");
            System.out.println("7. Log out");
            String choice = prompt("Choose an option: ");
            switch (choice) {
                case "1": showInventory(); break;
                case "2": searchItem(); break;
                case "3": addToList(customer); break;
                case "4": removeFromList(customer); break;
                case "5": System.out.println("\n" + customer.getGroceryList()); break;
                case "6": checkout(customer); break;
                case "7": System.out.println("Logged out."); return;
                default:  System.out.println("Please enter a number from 1 to 7.");
            }
        }
    }

    private static void searchItem() {
        String name = prompt("Item name: ");
        Item item = store.getInventory().findItem(name);
        if (item == null) {
            System.out.println("Item not found.");
        } else if (item.getStockCount() == 0) {
            System.out.printf("%s costs $%.2f but is out of stock.%n", item.getName(), item.getPrice());
        } else {
            System.out.printf("%s costs $%.2f, %d in stock.%n", item.getName(), item.getPrice(), item.getStockCount());
        }
    }

    private static void addToList(Customer customer) {
        String name = prompt("Item name: ");
        Item item = store.getInventory().findItem(name);
        if (item == null) {
            System.out.println("Item not found in store. Please only add items the store carries.");
            return;
        }
        Integer qty = readWholeNumber("Quantity: ");
        if (qty == null) {
            return;
        }
        if (!customer.addItem(item, qty)) {
            System.out.println("Quantity must be at least 1. Nothing was added.");
            return;
        }
        System.out.println("Added " + qty + " x " + item.getName() + " to your list.");
        int requested = requestedQuantity(customer.getGroceryList(), item.getName());
        if (requested > item.getStockCount()) {
            System.out.println("Warning: your list now asks for " + requested + " but only "
                    + item.getStockCount() + " are in stock. Reduce it before checking out.");
        }
    }

    private static void removeFromList(Customer customer) {
        String name = prompt("Item to remove: ");
        if (customer.removeItem(name)) {
            System.out.println("Removed from your list.");
        } else {
            System.out.println("Item not found in your list. You can only remove items you have added.");
        }
    }

    private static void checkout(Customer customer) {
        GroceryList list = customer.getGroceryList();
        if (list.isEmpty()) {
            System.out.println("Your list is empty. Add some items first.");
            return;
        }
        System.out.println("\n" + list);
        String confirm = prompt("Purchase these items? (y/n): ");
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Checkout canceled.");
            return;
        }
        double total = list.calculateTotal();
        if (customer.checkout(store)) {
            System.out.printf("Purchase complete! You were charged $%.2f. Thank you!%n", total);
        } else {
            System.out.println("Checkout canceled. Everything on your list must be in stock:");
            for (ListItem li : list.getItems()) {
                if (!store.getInventory().isInStock(li.getItem().getName(), li.getQuantity())) {
                    System.out.println("  - " + li.getItem().getName() + ": you asked for " + li.getQuantity()
                            + ", store has " + li.getItem().getStockCount());
                }
            }
            System.out.println("Nothing was purchased. Your list is unchanged.");
        }
    }

    private static int requestedQuantity(GroceryList list, String name) {
        for (ListItem li : list.getItems()) {
            if (li.getItem().getName().equalsIgnoreCase(name)) {
                return li.getQuantity();
            }
        }
        return 0;
    }

    // ------------------------------------------------------------------
    // Worker menu
    // ------------------------------------------------------------------

    private static void workerMenu(Worker worker) {
        while (true) {
            System.out.println("\n--- Worker Menu ---");
            System.out.println("1. View inventory");
            System.out.println("2. Add new item / restock existing item");
            System.out.println("3. Remove item from inventory");
            System.out.println("4. Change an item's price");
            System.out.println("5. Log out");
            String choice = prompt("Choose an option: ");
            switch (choice) {
                case "1": showInventory(); break;
                case "2": addOrRestock(worker); break;
                case "3": removeFromInventory(worker); break;
                case "4": changePrice(); break;
                case "5": System.out.println("Logged out."); return;
                default:  System.out.println("Please enter a number from 1 to 5.");
            }
        }
    }

    private static void addOrRestock(Worker worker) {
        String name = prompt("Item name: ");
        if (name.isEmpty()) {
            System.out.println("Item name cannot be empty.");
            return;
        }
        Item existing = store.getInventory().findItem(name);
        try {
            if (existing != null) {
                Integer qty = readWholeNumber("Units to add to " + existing.getName() + ": ");
                if (qty == null) return;
                if (qty < 1) {
                    System.out.println("Quantity must be at least 1.");
                } else if (worker.addStock(new Item(existing.getName(), existing.getPrice(), qty))) {
                    System.out.println("Restocked. " + existing.getName() + " now has " + existing.getStockCount() + " in stock.");
                } else {
                    System.out.println("Could not restock that item.");
                }
            } else {
                Double price = readPrice("Price: $");
                if (price == null) return;
                Integer qty = readWholeNumber("Starting stock: ");
                if (qty == null) return;
                if (worker.addStock(new Item(name, price, qty))) {
                    System.out.println("Added " + name + " to inventory.");
                } else {
                    System.out.println("Could not add the item. The store inventory is full.");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private static void removeFromInventory(Worker worker) {
        String name = prompt("Item to remove: ");
        if (worker.removeStock(name)) {
            System.out.println("Removed from inventory.");
        } else {
            System.out.println("Item not found.");
        }
    }

    private static void changePrice() {
        String name = prompt("Item name: ");
        Item item = store.getInventory().findItem(name);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }
        Double price = readPrice("New price: $");
        if (price == null) return;
        if (item.setPrice(price)) {
            System.out.printf("%s now costs $%.2f.%n", item.getName(), item.getPrice());
        } else {
            System.out.println("Price rejected. It must be non-negative.");
        }
    }

    // ------------------------------------------------------------------
    // Shared display and input helpers
    // ------------------------------------------------------------------

    private static void showInventory() {
        Inventory inv = store.getInventory();
        if (inv.getItemCount() == 0) {
            System.out.println("\nThe store has no items right now.");
            return;
        }
        System.out.println();
        System.out.printf("%-15s %8s %7s%n", "Item", "Price", "Stock");
        System.out.println("-------------------------------");
        for (Item item : inv.getItems()) {
            System.out.printf("%-15s %8s %7d%n", item.getName(), String.format("$%.2f", item.getPrice()), item.getStockCount());
        }
        System.out.println("-------------------------------");
        System.out.println(inv.getItemCount() + " item types, " + inv.getTotalStock() + " units total");
    }

    private static String prompt(String message) {
        System.out.print(message);
        return in.nextLine().trim();
    }

    /** @return the whole number entered, or null (after printing why) if it was not one */
    private static Integer readWholeNumber(String message) {
        String text = prompt(message);
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a whole number (no decimals or letters).");
            return null;
        }
    }

    /** @return the non-negative price entered, or null (after printing why) if invalid */
    private static Double readPrice(String message) {
        String text = prompt(message);
        try {
            double value = Double.parseDouble(text);
            if (value < 0 || Double.isNaN(value) || Double.isInfinite(value)) {
                System.out.println("Price must be a non-negative number.");
                return null;
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid price, like 3.50.");
            return null;
        }
    }
}
