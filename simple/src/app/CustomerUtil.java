package app;

import java.util.Optional;

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

    public static Optional<Customer> getOptional(Customer customer){
        if(customer == null){
            return Optional.empty();
        }

        return Optional.of(customer);
    }
}
