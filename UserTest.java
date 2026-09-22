import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class UserTest {
    @Test void constructorAndUsername() {
        User user = new User(" alice ", "secret");
        assertEquals("alice", user.getUsername());
        assertTrue(user.checkPassword("secret"));
        assertFalse(user.checkPassword("wrong"));
    }

    @Test void invalidCredentialsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new User("", "secret"));
        assertThrows(IllegalArgumentException.class, () -> new User("alice", ""));
        assertThrows(IllegalArgumentException.class, () -> new User(null, "secret"));
    }
}
