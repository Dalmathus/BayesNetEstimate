
import java.util.*;
import java.io.*;

public class BayesNetwork {

	private Node[] nodes;
	// event parser for training data
	private csvEventParser cp;
	// event parser for testing data
	private csvEventParser ct;

	/**
	//	Bayes Network constructor for only working on training data
	//	@param f the structure file containg the node layout
	//	@param k the comma separated file containing training data
	**/
	public BayesNetwork(String f, String k) throws FileNotFoundException {
		createNetwork(f);
		Scanner sc = IO.createScanner(k);
		String[] words = sc.nextLine().split(",");
		cp = new csvEventParser(words.length, k);
		calcProbs(nodes.length);
	}

	/**
	//	Bayes Network constructor for dealing with testing data once trainign data is processed
	//	@param f the structure file containg the node layout
	//	@param k the comma separated file containing training data
	//	@param y the comma separated file containing testing data
	**/
	public BayesNetwork(String f, String k, String y) throws FileNotFoundException {
		createNetwork(f);
		Scanner sc = IO.createScanner(k);
		String[] words = sc.nextLine().split(",");
		cp = new csvEventParser(words.length, k);
		ct = new csvEventParser(words.length, y);
		calcProbs(nodes.length);
		guessMissing();
	}

	/**
	// Creates a Network from a given .str file
	// @return number of nodes in network
	**/
	private void createNetwork(String filename) throws FileNotFoundException {

		Scanner sc = IO.createScanner(filename);

		String line;
		String[] s;
		String[] p;
		Node[] r;
		List<Node> parents = new ArrayList<Node>();

		int c = IO.countLines(sc);
		nodes = new Node[c];

		sc = IO.createScanner(filename);

		for (int i = 0; i < c; i++) {
			
			line = sc.nextLine();
			s = line.split(":");
			
			// If the node has parents I need to find those nodes in the list of nodes I have already created
			if (s.length > 1) { 
				
				p = s[1].split(" "); 
				
				for (int j = 0; j < nodes.length; j++) {
					if (nodes[j] == null) break;
					for (String n : p) { 
						if (n.equals(nodes[j].getName())) parents.add(nodes[j]);
					}
				}

				r = new Node[parents.size()];
				r = parents.toArray(r);
				parents.clear();
				nodes[i] = new Node(s[0], r); 
			}
			else nodes[i] = new Node(s[0], new Node[] {});
		}		
	} 

	/**
	//	Find probability of key words being spam given we know if email was spam
	//	@param N number of nodes
	**/
	private void calcProbs(int N) {
		for (int i = 0; i < N; i++) {
			
			double[] tfCount;
			int index = cp.getKeywordIndex(nodes[i].getName());
			tfCount = cp.getKeywordCount(nodes[i].getName());
			if (tfCount.length == 0) continue;

			// Sum the false and true counts to save on line space
			double total = tfCount[0] + tfCount[1];

			// If the node in question is an orphan just calculate basic odds of the event being true or not
			if (nodes[i].getParents().length == 0) {
				double[] probs = new double[2];
				probs[0] = tfCount[0] / total;
				probs[1] = tfCount[1] / total;
				nodes[i].setProbs(probs);
			}
			else {
				// to save space I will make the length of parents a variable
				int length = nodes[i].getParents().length;

				// storage boolean for child truth value
				boolean childVal;

				// index of truth configuration from binary string
				int binaryIndex;

				// get the indexed locations of the keywords in events
				int[] parentIndex = getParentIndexes(length, i);
				int childIndex = cp.getKeywordIndex(nodes[i].getName());

				// create the string builder to make our 'binary' number
				StringBuilder sb;

				// create the final truth values for each possibility
				double[] probs = new double[(int)Math.pow(2, length)];

				// create the probability array to store true and false results
				double[][] data = new double[probs.length][2];

				// set all values to 1 to avoid the zero frequency problem
				for (int k = 0; k < probs.length; k++) {
					data[k][0]++;
					data[k][1]++;
				}

				// for each atomic event build the binary string
				for (String[] sa : cp.getEvents()) {
					sb = new StringBuilder();
					for (int j = 0; j < parentIndex.length; j++) {
						if (sa[parentIndex[j]].equals("1")) sb.append("1");
						// need to make this else if instead of else for the unknown case '?'
						else if (sa[parentIndex[j]].equals("0")) sb.append("0");
					}
					// find the truth value of our child
					if (sa[childIndex].equals("1")) childVal = true;
					else childVal = false;

					// now that the string is built we have a binary representation of the index
					binaryIndex = Integer.parseInt(sb.toString(), 2);

					// if Child is true increment the true column '0' vice versa false
					if (childVal) data[binaryIndex][1]++;
					else data[binaryIndex][0]++;
				} 

				for (int k = 0; k < probs.length; k++) {
					probs[k] = data[k][1] / (data[k][0] + data[k][1]);
				}

				nodes[i].setProbs(probs);
			}
		}
	}

	/**
	//	Use training data to guess the probability of missing data in a identical data set with missing truth values
	**/
	private void guessMissing() {

		// identify the index the query node is located at
		int queryIndex = ct.findQueryNode();

		// create the Random number generator to roll from 0.0 to 1.0 this will serve to fill in our missing data
		Random ran = new Random();

		// get the keyword associated with index so we can get the nodes information
		String queryName = ct.getKeywordAtIndex(queryIndex);

		// get the query node out of the list of nodes so we can finally start working on it
		Node queryNode = null;
		int index = 0;

		for (Node n : nodes) {
			if (n.getName().equals(queryName)) { 
				queryNode = n;
				break;
			} 
			index++;
		}

		if (queryNode.getParents().length > 0) {
		// by getting all the indexes of the parents of the node
			int[] parentIndex = getParentIndexes(queryNode.getParents().length, index);

			for (String[] sa : ct.getEvents()) {

			// create the index of the probability that an event will happen given its parents
				StringBuilder sb = new StringBuilder();
				for (int k : parentIndex) {
					sb.append(sa[k]);
				}
				int probIndex = Integer.parseInt(sb.toString(), 2);

			// now that I have the index update the query node with its new value after creating a new random number
			// getting a result of either true or false
				if (ran.nextDouble() < queryNode.getProbs()[probIndex])	sa[queryIndex] = "1";
				else sa[queryIndex] = "0";
			}
		}
		else {
		// if the node is an orphan just use its probability table for true
			for (String[] sa : ct.getEvents()) {
				if(ran.nextDouble() < queryNode.getProbs()[0]) sa[queryIndex] = "1";
				else sa[queryIndex] = "0";
			}
		}
	}

	/**
	//	@param l number of parents
	//	@param i child node index in nodes
	//	@return int[] containing all indexs of parents in the csv file
	**/
	private int[] getParentIndexes(int l, int i) {
		int[] parentIndex = new int[l];		
		for (int j = 0; j < l; j++) {
			parentIndex[j] = cp.getKeywordIndex(nodes[i].getParents()[j].getName());
		}
		return parentIndex;
	}

	/**
	//	Write nodes and probabilities out to output.txt
	//	@param type of file to output
	**/
	public void toFile(String fileType) throws IOException {

		if (fileType.equals(".txt")) {
			File output = new File("output.txt");
			FileWriter fw = new FileWriter(output);
			BufferedWriter bw = new BufferedWriter(fw);

		// output name and parents
			for (Node n : nodes) {
				double[] probs = n.getProbs();			
				bw.write(n.getName() + ":");

			// I PRINT OUT TRUE AND FALSE VALUES FOR ORPHAN NODES WHILE ONLY PRINTING OUT TRUE VALUES FOR OTHERS
				if (n.getParents().length == 0) {
					bw.newLine();
					bw.write("0 " + String.format("%.5f", probs[1]) + '\n'  + "1 " + String.format("%.5f", probs[0]) + '\n' + '\n');
				}
				else {
					bw.newLine();
					for (Node p : n.getParents()) {
						bw.write(p.getName() + " ");
					}
					bw.newLine();

			// create binaryString representation
					int size = n.getParents().length;				
					for (int i = 0; i < Math.pow(2, size); i++) {
						String binaryString = Integer.toString(i, 2);
						while (binaryString.length() < size) {
							binaryString = "0" + binaryString;
						}
						bw.write(binaryString + " ");
						bw.write(String.format("%.5f", probs[i]));
						bw.newLine();
					}
					bw.newLine();
				}
			}
			bw.close();
		}
		else if (fileType.equals(".csv")) {
			File output = new File("completedTest.csv");
			FileWriter fw = new FileWriter(output);
			BufferedWriter bw = new BufferedWriter(fw);

			String[] words = ct.getKeywords();
			for (int i = 0; i < words.length; i++) {
				bw.write(words[i]);
				if (i != words.length - 1) bw.write(",");
			}
			bw.newLine();
			for (String[] sa : ct.getEvents()) {
					for (int i = 0; i < sa.length; i++) {
					bw.write(sa[i]);
					if (i != words.length - 1) bw.write(",");
				}
				bw.newLine();
			}
			bw.close();
		}
		else {
			System.out.println("Improper output file type specfied, currently supported file types are .csv and .txt");
		}
	}

	/**
	// Getters and Setters
	**/

	public void setNodes(Node[] n) { this.nodes = n; }
	public Node[] getNodes() { return this.nodes; }
}