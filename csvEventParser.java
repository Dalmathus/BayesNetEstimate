
/**
//	This class creates a list of keywords and a count of ho wmany times that keyword shows up in a csv file
**/
public class csvEventParser {
	
	private String[] keywords;
	private int[] count;

	/**
	//	Initialize the array to the number of events found inside a csv and update each count ot start at 1
	//	@param N count of unique keywords
	**/
	public csvEventParser(int N){
		keywords = new String[N];
		count = new int[N];
		// Initialize all counts to 1 to avoid the zero ferquency problem
		for (int i = 0; i < N; count[i]++, i++);
	}

	/**
	//	Increments the number of times a keyword has appeared in the training data
	//	@param s Keyword already added to keyword[]
	**/
	public void incrementKeyword(String s) {
		for (int i = 0; i < keywords.length; i++) {
			if (keywords[i].equals(s)) count[i]++;
		}
	}

	/**
	//	@param s the keyword in keywords[] I want the count for
	//	@return the number of times s has appeared in the csv
	**/
	public int getKeywordCount(String s) {
		for (int i = 0; i < keywords.length; i++) {
			if (keywords[i].equals(s)) return count[i];			
		}

		return 0;
	}

	/**
	//	@param s new keyword to add
	//	@param index typically first empty value in the keywords[]
	**/
	public void addKeyWord(String s, int index) {
		keywords[index] = s;
	}
}