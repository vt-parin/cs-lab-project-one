# StockSmart

StockSmart is a grocery store management application written in Java. The application allows customers to create grocery lists, check item availability, and purchase groceries. Workers can manage the store's inventory and item prices.

## Source Code

The main Java classes are:

* `Main.java` - Runs the application and provides the console interface.
* `GroceryStore.java` - Manages the store, users, inventory, and purchases.
* `Item.java` - Represents a grocery item.
* `Inventory.java` - Manages the store's inventory.
* `User.java` - Base class for users and login information.
* `Customer.java` - Allows customers to manage grocery lists and checkout.
* `Worker.java` - Allows workers to manage inventory.
* `GroceryList.java` - Stores the items a customer wants to purchase.
* `ListItem.java` - Represents an item and its requested quantity.

## How to Compile

From the directory containing the `.java` files, run:

## How to Run

After compiling, run:

```bash
java Main
```

## Demo Login

The program includes the following demo accounts:

**Customer**

* Username: `customer`
* Password: `1234`

**Worker**

* Username: `worker`
* Password: `1234`

## Testing

The project includes tests for item validation, inventory management, grocery lists, purchases, login, and other required functionality.

If using JUnit, run the JUnit test class through Eclipse or your JUnit-compatible Java environment.

## Requirements

* Java Development Kit (JDK)
* Eclipse or another Java IDE
* JUnit 5 for the JUnit tests

