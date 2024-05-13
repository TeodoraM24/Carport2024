package app.persistence;

import app.validators.PasswordValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidatorTest {

    @Test
    void isValidPasswordNoUpper() {
        String password = "dsfd2313123bl";
        boolean actual = PasswordValidator.isValidPassword(password);
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    void isValidPasswordNoLower() {
        String password = "GSKK238554FD";
        boolean actual = PasswordValidator.isValidPassword(password);
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    void isValidPasswordNoNumber() {
        String password = "SFDFsfdffsfdsDbDs";
        boolean actual = PasswordValidator.isValidPassword(password);
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    void isValidPasswordUnder8Characters() {
        String password = "2Ghd3k";
        boolean actual = PasswordValidator.isValidPassword(password);
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    void isValidPasswordTrue() {
        String password = "dfd2DD2ds32y!";
        boolean actual = PasswordValidator.isValidPassword(password);
        boolean expected = true;
        assertEquals(expected, actual);
    }
}
