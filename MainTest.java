import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

class MainTest {
    @Test void mainCanStartAndExit() {
        PrintStream originalOut = System.out;
        java.io.InputStream originalIn = System.in;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream("2\n".getBytes()));
            System.setOut(new PrintStream(output));
            Main.main(new String[0]);
            String text = output.toString();
            assertTrue(text.contains("Welcome to StockSmart"));
            assertTrue(text.contains("Goodbye!"));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }
}
