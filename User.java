/** PLACEHOLDER for Isaac's User, written to the spec. Replace with the real one. */
public class User {
    private final String username;
    private final String password;

    public User(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        this.username = username.trim();
        this.password = password;
    }

    public String getUsername() { return username; }
    public boolean checkPassword(String password) { return this.password != null && this.password.equals(password); }
}
