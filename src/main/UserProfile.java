package main;

public class UserProfile {

    private final String username;
    private final String pin;

    public UserProfile(String username, String pin) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be 4 digits.");
        }
        this.username = username.trim();
        this.pin = pin;
    }

    public String getUsername() {
        return username;
    }

    public boolean matchesPin(String candidatePin) {
        return pin.equals(candidatePin);
    }
}
