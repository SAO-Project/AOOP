package app;

public class InvalidTransaction extends RuntimeException {
    public InvalidTransaction(String errorMessage){
        super(errorMessage);
    }
}
