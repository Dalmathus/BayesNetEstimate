
import java.util.*;
import java.io.*;

public class BayesNetwork {

	private Node[] nodes;
	private csvEventParser cp;

	public BayesNetwork(String f, String k) throws FileNotFoundException {
		createNetwork(f);
		Scanner sc = createScanner(k);
		String[] words = sc.nextLine().split(",");
		cp = new csvEventParser(words.length, k);
		calcProbs(nodes.length);
	}

	/**
	// Creates a Network from a given .str file
	// @return number of nodes in network
	**/
	private void createNetwork(String filename) throws FileNotFoundException {

		Scanner sc = createScanner(filename);

		String line;
		String[] s;
		String[] p;
		Node[] r;
		List<Node> parents = new ArrayList<Node>();

		int c = countLines(sc);
		nodes = new Node[c];

		sc = createScanner(filename);

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
				System.out.println(nodes[i].getName() + " " + nodes[i].getProbs()[0] + " " + nodes[i].getProbs()[1]);
			}
			else {
				// to save space I will make the length of parents a variable
				int length = nodes[i].getParents().length;

				// storage boolean for child truth value
				boolean childVal;

				// index of truth configuration from binary string
				int binaryIndex;

				// get the indexed locations of the keywords in events
				int[] parentIndex = new int[length];
				int childIndex = cp.getKeywordIndex(nodes[i].getName());
				for (int j = 0; j < length; j++) {
					parentIndex[j] = cp.getKeywordIndex(nodes[i].getParents()[j].getName());
				}

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

				System.out.println();
				System.out.print("P(" + nodes[i].getName() + "|");
					for (Node n : nodes[i].getParents()) System.out.print(n.getName() + ",");
				System.out.print(")");
				System.out.println();

				for (double d : probs) System.out.print(d + " ");
			}
		}
	}

	/**
	//	Write nodes and probabilities out to output.txt
	**/
	public void toFile() throws IOException {
		File output = new File("output.txt");
		FileWriter fw = new FileWriter(output);
		BufferedWriter bw = new BufferedWriter(fw);

		// output name and parents
		for (Node n : nodes) {
			double[] probs = n.getProbs();			
			bw.write(n.getName() + ":");

			if (n.getParents().length == 0) {
				bw.newLine();
				bw.write("0 " + String.format("%.5f", probs[1]) + '\n'  + "1 " + String.format("%.5f", probs[0]));
				bw.newLine();
				bw.newLine();
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

	/**
	//	creates a scanner object for parsing a text file
	//	@param filename
	**/
	private Scanner createScanner(String filename) throws FileNotFoundException {
		String workingDirectory = System.getProperty("user.dir");
        File tempFile = new File(workingDirectory + File.separator + filename);
		Scanner sc = new Scanner(tempFile);
		return sc;
	}

	//	TODO: Move this and other scanner related methods into a static class to handle IO reading
	/**
	//	Counts the number of lines in a file through a scanner object
	// @param sc a scanner object with the file already loaded into it
	// @return the number of lines in file
	**/
	private int countLines(Scanner sc) {
		int count = 0;
		while (sc.hasNext()) {
			sc.nextLine();
			count++;
		}
		return count;
	}

	/**
	// Getters and Setters
	**/

	public void setNodes(Node[] n) { this.nodes = n; }
	public Node[] getNodes() { return this.nodes; }
}