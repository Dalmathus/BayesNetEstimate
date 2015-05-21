
import java.util.*;
import java.io.*;

public class BayesNetwork {

	private Node[] nodes;
	private HashMap<String, Integer> events;

	public BayesNetwork(String f, String k) throws FileNotFoundException {
		createNetwork(f);
		findEvents(k);
	}

	/**
	// Creates a Network from a given .str file
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
			if (s[1] != null) { 
				
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
	//	Sums all events parsed from the csv file provided into a HashMap to calculate probabilites of nodes
	//	@param filename the csv file contatining frequency data
	**/
	private void findEvents(String filename) throws FileNotFoundException {
		Scanner sc = createScanner(filename);
		int lineCount = 0;

		String[] split = parseLine(sc.nextLine());
		int n = split.length;

		csvEventParser cp = new csvEventParser(n);

		// fill the keyword array with all keyword names
		for (int i = 0; i < n; i++) {
			cp.addKeyword(split[i], i);
		}

		while (sc.hasNext()){
			lineCount++;
			split = parseLine(sc.nextLine());
			for (int i = 0; i < n; i++) {
				if (split[i].equals("1")) 
					cp.incrementKeyword(i);
			}
		}

		System.out.println(lineCount);
		cp.printCounts();

	}

	/**
	//	Find probability of key words being spam given we know if email was spam
	//	@param N number of samples
	**/
	private void findProbs(int N) {

	}

	/**
	// Just a simple method to split on comma so I can do a nextLine operation and split into an array in one tidy line
	**/
	private String[] parseLine(String s) {
		return s.split(",");
	}

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

	private int countWords(String s) {
		return 0;
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

	/**
	// Getters and Setters
	**/

	public void setNodes(Node[] n) { this.nodes = n; }
	public Node[] getNodes() { return this.nodes; }

	public void setEvents(HashMap<String, Integer> h) { this. events = h; }
	public HashMap<String, Integer> getEvents() { return this.events; }

}