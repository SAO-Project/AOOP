package app;

/**
 * @author Alex Avila
 * @since 11/22/20
 * @version 1
 *
 * Custom exception class for transaction actions.
 */
public class InvalidTransaction extends RuntimeException {
    public InvalidTransaction(String errorMessage){
        super(errorMessage);
    }
}
