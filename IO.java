import java.io.*;
import java.util.*;

/**
//	A static class to handle my scanner creation and linecounting operations from multiple classes
**/
public class IO {
	
	/**
	//	creates a scanner object for parsing a text file
	//	@param filename
	**/
	public static Scanner createScanner(String filename) throws FileNotFoundException {
		String workingDirectory = System.getProperty("user.dir");
        File tempFile = new File(workingDirectory + File.separator + filename);
		Scanner sc = new Scanner(tempFile);
		return sc;
	}

	/**
	// Just a simple method to split on comma so I can do a nextLine operation and split into an array in one tidy line
	**/
	public static String[] parseLine(String s) {
		return s.split(",");
	}

	/**
	//	Counts the number of lines in a file through a scanner object
	// @param sc a scanner object with the file already loaded into it
	// @return the number of lines in file
	**/
	public static int countLines(Scanner sc) {
		int count = 0;
		while (sc.hasNext()) {
			sc.nextLine();
			count++;
		}
		return count;
	}
}