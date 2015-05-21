// James Luxton 1190809
// nodes 
// bayes_net.str
// naive_bayes.str
// training data
// spam_train.csv
// spam_test.csv
// Java implemention of a Spam Filter Detection program usign Bayes and Inference Models

import java.io.*;

public class BayesNetEstimate {

	public static void main(String[] args) throws FileNotFoundException {
		
		if (args.length == 0) System.exit(0);

		boolean debug = true;

		BayesNetwork b = new BayesNetwork(args[0], args[1]);

		if (debug) {
			System.out.println('\n' + "Nodes Network" + '\n');
			for (Node n : b.getNodes()) {
				n.print();
			}
		}
	}
}