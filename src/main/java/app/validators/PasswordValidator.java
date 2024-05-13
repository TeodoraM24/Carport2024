package app.validators;

public class PasswordValidator {

    public static boolean isValidPassword(String password) {
        // Password should have at least 8 characters
        if (password.length() < 8) {
            return false;
        }

        // Password should contain at least one uppercase letter, one lowercase letter, and one digit
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        return hasUppercase && hasLowercase && hasDigit;
    }
}