package app;

import java.util.Optional;

public class NullAccount extends Account {

    @Override
    public Optional<Account> getOptional() {
        return Optional.empty();
    }
}
