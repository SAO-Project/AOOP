package app;

public class CustomerUtil {
    /**
     * Method to create a generic password for every new customer.
     *
     * Password will always be "lastName*firstName!987"
     *
     * @param firstName Last word of password.
     * @param lastName First word of password.
     * @return Full password.
     */
    public static String generatePassword(String firstName, String lastName) {
        return lastName + "*" + firstName + "!987";
    }
}
