// James Luxton 1190809
// nodes 
// bayes_net.str
// naive_bayes.str
// training data
// spam_train.csv
// test data
// spam_test.csv
// Java implemention of a Spam Filter Detection program usign Bayes and Inference Models

import java.io.*;

public class BayesNetEstimate {

	public static void main(String[] args) throws IOException {		
		if (args.length != 2) System.exit(0);
		BayesNetwork b = new BayesNetwork(args[0], args[1]);
		b.toFile(".txt");
	}
}