import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static GroceryStore store;

    public static void main(String[] args) {
        setupStore();

        System.out.println("================================");
        System.out.println("        Welcome to StockSmart");
        System.out.println("================================");

        boolean running = true;

        while (running) {
            System.out.println("\n1. Login");
            System.out.println("2. Exit");

            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void setupStore() {
        store = new GroceryStore(new Inventory(100));

        store.getInventory().addItem(new Item("Milk", 3.49, 20));
        store.getInventory().addItem(new Item("Bread", 2.99, 15));
        store.getInventory().addItem(new Item("Eggs", 4.59, 12));
        store.getInventory().addItem(new Item("Apple", 0.89, 50));
        store.getInventory().addItem(new Item("Rice", 6.99, 10));

        Customer customer = new Customer("customer", "1234");
        Worker worker = new Worker("worker", "1234", store);

        store.addUser(customer);
        store.addUser(worker);
    }

    private static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = store.login(username, password);

        if (user == null) {
            System.out.println("Invalid login information.");
        } else if (user instanceof Customer) {
            customerMenu((Customer) user);
        } else if (user instanceof Worker) {
            workerMenu((Worker) user);
        }
    }

    private static void customerMenu(Customer customer) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. View inventory");
            System.out.println("2. Add item to grocery list");
            System.out.println("3. Remove item from grocery list");
            System.out.println("4. View grocery list");
            System.out.println("5. Checkout");
            System.out.println("6. Logout");

            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    displayInventory();
                    break;

                case 2:
                    System.out.print("Item name: ");
                    String itemName = scanner.nextLine();

                    Item item = store.getInventory().findItem(itemName);
                    if (item == null) {
                        System.out.println("Item not found in store.");
                        break;
                    }

                    int quantity = readInt("Quantity: ");
                    if (customer.addItem(item, quantity)) {
                        System.out.println("Item added to grocery list.");
                    } else {
                        System.out.println("Invalid quantity.");
                    }
                    break;

                case 3:
                    System.out.print("Item name to remove: ");
                    String removeName = scanner.nextLine();

                    if (customer.removeItem(removeName)) {
                        System.out.println("Item removed.");
                    } else {
                        System.out.println("Item not found on grocery list.");
                    }
                    break;

                case 4:
                    System.out.println(customer.getGroceryList());
                    break;

                case 5:
                    if (customer.checkout(store)) {
                        System.out.println("Purchase successful.");
                    } else {
                        System.out.println(
                                "Purchase failed. Make sure every requested item is available.");
                    }
                    break;

                case 6:
                    loggedIn = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void workerMenu(Worker worker) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n--- Worker Menu ---");
            System.out.println("1. View inventory");
            System.out.println("2. Add new item");
            System.out.println("3. Remove item");
            System.out.println("4. Change stock");
            System.out.println("5. Change price");
            System.out.println("6. Logout");

            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    displayInventory();
                    break;

                case 2:
                    System.out.print("Item name: ");
                    String name = scanner.nextLine();

                    double price = readDouble("Price: $");
                    int stock = readInt("Starting stock: ");

                    try {
                        Item item = new Item(name, price, stock);

                        if (worker.addStock(item)) {
                            System.out.println("Item added to inventory.");
                        } else {
                            System.out.println(
                                    "Could not add item. It may already exist or inventory may be full.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    System.out.print("Item name to remove: ");
                    String removeName = scanner.nextLine();

                    if (worker.removeStock(removeName)) {
                        System.out.println("Item removed from inventory.");
                    } else {
                        System.out.println("Item not found.");
                    }
                    break;

                case 4:
                    System.out.print("Item name: ");
                    String stockName = scanner.nextLine();

                    int amount = readInt(
                            "Amount to add/remove (use negative number to remove): ");

                    if (worker.updateStock(stockName, amount)) {
                        System.out.println("Stock updated.");
                    } else {
                        System.out.println(
                                "Stock update failed. Item may not exist or stock would become negative.");
                    }
                    break;

                case 5:
                    System.out.print("Item name: ");
                    String priceName = scanner.nextLine();

                    double newPrice = readDouble("New price: $");

                    if (worker.updatePrice(priceName, newPrice)) {
                        System.out.println("Price updated.");
                    } else {
                        System.out.println(
                                "Price update failed. Item may not exist or price may be invalid.");
                    }
                    break;

                case 6:
                    loggedIn = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void displayInventory() {
        System.out.println("\n--- Store Inventory ---");

        if (store.getInventory().getItems().isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        for (Item item : store.getInventory().getItems()) {
            System.out.println(item);
        }

        System.out.println(
                "Different item types: " + store.getInventory().getItemCount());
        System.out.println(
                "Total units in stock: " + store.getInventory().getTotalStock());
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
