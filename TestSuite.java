import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/** Runs every JUnit test class together. Run with: java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore TestSuite */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ItemTest.class,
        InventoryTest.class,
        GroceryListTest.class,
        GroceryStoreTest.class,
        CustomerTest.class,
        WorkerTest.class
})
public class TestSuite {
}
