package app;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    /**
     * Validates name.
     * @param name Name to validate.
     * @return If valid.
     */
    public static boolean isNameValid(String name) {
        return Pattern.matches("[a-zA-Z]+", name);
    }

    /**
     * Validates email.
     *
     * @param email email to validate.
     * @return If email is valid.
     */
    public static boolean isEmailValid(String email) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }

    /**
     * Validates phone number.
     *
     * @param phoneNumber Number to validate.
     * @return If phone number is valid.
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.charAt(0) == '9'
                && phoneNumber.length() == 10
                && Pattern.matches("[0-9]+", phoneNumber);
    }

    /**
     * Validate a string. Make sure it is not length zero.
     *
     * @param str String to validate.
     * @return If empty() string is not valid.
     */
    public static Optional<String> validateStr(String str) {
        if (str == null) {
            return Optional.empty();
        }
        if (str.length() == 0) {
            return Optional.empty();
        }
        return Optional.of(str);
    }
}
