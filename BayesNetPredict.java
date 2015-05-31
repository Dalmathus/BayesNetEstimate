
import java.io.*;

public class BayesNetPredict {

		public static void main(String[] args) throws IOException{
		if (args.length != 3) System.exit(0);
		BayesNetwork b = new BayesNetwork(args[0], args[1], args[2]);
		b.toFile(".csv");
	}
	
}