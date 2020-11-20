package app;

import java.io.IOException;
import java.io.Writer;

/**
 *
 */
public interface Printable {
	/**
	 * prints implemented class into the console
	 */
	void print();
	
	/**
	 * gets class into a string
	 * @return String of the class
	 */
	String getString();
	
	/**
	 * Prints the String of the class into any type of Writer e.g. a file
	 * @param writer a writer interface such as a file
	 * @throws IOException throws if there is an error with the output file
	 */
	void write(Writer writer) throws IOException;
}
