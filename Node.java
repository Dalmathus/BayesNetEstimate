// Node class taken from BayesNet.java with minor adjustments to account for not knowing the probability of an event on creation

public class Node {

	// The name of the node
	private String name;

	// The parent nodes
	private Node[] parents;

	// The probabilities for the CPT
	private double[] probs;

	// The current value of the node
	private boolean value;
		
	/**
	//	A node inside a Bayes Network, each node has 
	//	a name to identify it
	//	a list of parents
	//	a list of probabilities given its parents
	// 	and a value the node is currently set to
	//	only name and parent lists are required
	**/
	public Node(String n, Node[] pa) {
			name = n;
			parents = pa;
	}

	/**
	// Prints the name of a node followed by its parents if any to console
	// Should only be called if debug = true
	**/
	public void print(){
		System.out.print(this.name + ": ");
		for (Node n : parents) {
			System.out.print(n.getName() + " ");
		}
		System.out.println();
	}

	/**
	// Getters and Setters
	**/
	
	public String getName() { return this.name; }
	public void setName(String n) { this.name = n; }

	public Node[] getParents() { return this.parents; }
	public void setParents(Node[] p) { this.parents = p; }

	public double[] getProbs() { return this.probs; }
	public void setProbs(double[] p) { this.probs = p; }

	public boolean getValue() { return this.value; }
	public void setValue(boolean v) { this.value = v; }
}