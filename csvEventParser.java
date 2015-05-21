
/**
//	This class creates a list of keywords and a count of ho wmany times that keyword shows up in a csv file
**/
public class csvEventParser {
	
	private String[] keywords;
	private int[] Tcount;
	private int[] Fcount;

	/**
	//	Initialize the array to the number of events found inside a csv and update each count ot start at 1
	//	@param N count of unique keywords
	**/
	public csvEventParser(int N){
		keywords = new String[N];
		count = new int[N];
		// Initialize all counts to 1 to avoid the zero ferquency problem
		for (int i = 0; i < N; Fcount[i]++, Tcount[i]++, i++);
	}

	/**
	//	Increments the number of times a keyword has appeared in the training data at given index
	//	@param i index of keyword
	**/
	public void incrementKeyword(int i, String val) {
		if (val.equals("1")) Tcount[i]++;
		else if (val.equals("0")) Fcount[i]++;
	}

	/**
	//	@param s the keyword in keywords[] I want the count for
	//	@return the number of times s has appeared in the csv
	**/
	public int[] getKeywordCount(String s) {

		int[] output = new int[2];

		for (int i = 0; i < keywords.length; i++) {
			if (keywords[i].equals(s)) {
				output[0] = Tcount[i];
				output[1] = Fcount[i];
			} 			
		}
		return output;
	}

	/**
	//	@param s new keyword to add
	//	@param index typically first empty value in the keywords[]
	**/
	public void addKeyword(String s, int index) {
		keywords[index] = s;
	}

	/**
	//	prints all keywords and their counts
	**/
	public void printCounts() {
		for (int i = 0; i < keywords.length; i++) {
			System.out.println(keywords[i] + " True: " + Tcount[i] + " False: " + Fcount[i]);
		}
	}

	/**
	//	@return sum of all counts returning sample size
	**/
	public int eventSamples() {
		int n = 0;

		for (int i : count) 
			n += i;

		return n;
	}
}