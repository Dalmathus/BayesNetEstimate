
import java.util.*;
import java.io.*;

public class BayesNetwork {

	private Node[] nodes;
	private HashMap<String, Integer> events;

	public BayesNetwork(String f, String N) throws FileNotFoundException {
		createNetwork(f);
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