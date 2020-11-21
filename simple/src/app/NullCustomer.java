package app;

import java.util.Optional;

public class NullCustomer extends Customer{
    @Override
    public Optional<Customer> getOptional(){
        return Optional.empty();
    }
}
