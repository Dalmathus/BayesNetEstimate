
public class BayesEvents {
	String[] keywords;
	int[] count;

	public BayesEvents(int N){
		keywords = new String[N];
		count = new int[N];
		// Initialize all counts to 1 to avoid the zero ferquency problem
		for (int i = 0; i < N; count[i]++, i++);
	}

	public void incrementKeyword(String s) {
		for (int i = 0; i < keywords.length; i++) {
			if (keywords[i].equals(s)) count[i]++;
		}
	}

	public int getKeywordCount(String s) {
		for (int i = 0; i < keywords.length; i++) {
			if (keywords[i].equals(s)) return count[i];			
		}
		
		return 0;
	}
}