import java.util.Optional;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 10/26/20
 */
public class Main {
    public static void main(String[] args) {
        String str = "Alex";

        Optional<String> option = Optional.of(str);
        Optional<String> option2 = Optional.empty();

        if (option.isPresent()) {
            System.out.println(option.get());
        }

        if (option2.isEmpty()) {
            System.out.println("Option2");
        }


        option.ifPresent(System.out::println);

        // Throw if empty.
        String str2 = option2.orElseThrow();
    }
}
