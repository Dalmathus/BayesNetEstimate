import java.io.*;
import java.util.*;

/**
//	This class creates a list of keywords and a count of ho wmany times that keyword shows up in a csv file
**/
public class csvEventParser {

	String csvFile;
	private String[][] events;
	private String[] keywords;	
	private int[] Tcount;
	private int[] Fcount;

	/**
	//	Initialize the array to the number of events found inside a csv and update each count ot start at 1
	//	@param N count of unique keywords
	**/
	public csvEventParser(int N, String filename) throws FileNotFoundException {
		csvFile = filename;
		keywords = new String[N];
		Tcount = new int[N];
		Fcount = new int[N];
		// Initialize all counts to 1 to avoid the zero ferquency problem
		for (int i = 0; i < N; Fcount[i]++, Tcount[i]++, i++);
		findEvents();
	}

	/**
	//	Sums all events parsed from the csv file provided into a HashMap to calculate probabilites of nodes
	//	@param filename the csv file contatining frequency data
	**/
	private void findEvents() throws FileNotFoundException {
		Scanner sc = IO.createScanner(csvFile);
		int lineCount = IO.countLines(sc);

		sc = IO.createScanner(csvFile);
		String[] split = IO.parseLine(sc.nextLine());
		int n = split.length;

		events = new String[lineCount - 1][n];

		// fill the keyword array with all keyword names
		for (int i = 0; i < n; i++) {
			addKeyword(split[i], i);
		}

		int j = 0;
		while (sc.hasNext()){
			split = IO.parseLine(sc.nextLine());
			for (int i = 0; i < n; i++) {
				events[j][i] = split[i];
				incrementKeyword(i, split[i]);
			}
			j++;
		}
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
	//	@return the index of the given 
	**/
	public int getKeywordIndex(String s) {
		for (int i = 0; i < keywords.length; i++) {
			if (s.equals(keywords[i])) return i;
		}
		return -1;
	}

	/**
	//	@param i index of keyword
	// 	@return the keyword at index i
	**/
	public String getKeywordAtIndex(int i) {
		return keywords[i];
	}

	/**
	//	@param s the keyword in keywords[] I want the count for
	//	@return the number of times s has appeared in the csv
	**/
	public double[] getKeywordCount(String s) {

		double[] output = new double[0];

		for (int i = 0; i < keywords.length; i++) {
			if (keywords[i].equals(s)) {
				output = new double[2];
				output[0] = Tcount[i];
				output[1] = Fcount[i];
			} 			
		}
		return output;
	}

	/**
	//	Method assumes there is only ever one query node
	//	@return returns the location of the query node for test data
	**/
	public int findQueryNode() {
		for (int i = 0; i < keywords.length; i++) if (events[0][i].equals("?")) return i;
		return -1;
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
		for (int i = 0; i < keywords.length; i++){
			n += Tcount[i];
			n += Fcount[i];
		}
		return n;
	}

	/**
	//	getters and setters
	**/
	public String[][] getEvents() { return events; }
	public String[] getKeywords() { return keywords; }
	public int trueSamples(int i) { return Tcount[i]; }
	public int falseSamples(int i) { return Fcount[i]; }
	public int allSamples(int i) { return Tcount[i] + Fcount[i]; }

}