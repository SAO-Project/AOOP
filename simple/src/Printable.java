import java.io.IOException;
import java.io.Writer;

public interface Printable {
	public void print();
	public String getString();
	public void write(Writer writer) throws IOException;
}
