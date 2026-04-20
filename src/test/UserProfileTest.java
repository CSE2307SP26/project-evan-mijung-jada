package test;

import main.UserProfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class UserProfileTest {

    @Test
    public void testCreateUserProfileStoresUsername() {
        UserProfile profile = new UserProfile("jada", "1234");
        assertEquals("jada", profile.getUsername());
    }

    @Test
    public void testCreateUserProfileRejectsEmptyUsername() {
        try {
            new UserProfile("   ", "1234");
            fail();
        } catch (IllegalArgumentException e) {
            // test passes
        }
    }

    @Test
    public void testCreateUserProfileRejectsBadPin() {
        try {
            new UserProfile("jada", "12");
            fail();
        } catch (IllegalArgumentException e) {
            // test passes
        }
    }

    @Test
    public void testMatchesPin() {
        UserProfile profile = new UserProfile("jada", "1234");
        assertTrue(profile.matchesPin("1234"));
        assertFalse(profile.matchesPin("0000"));
    }
}
