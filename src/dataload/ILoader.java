package dataload;

import java.util.ArrayList;

public interface ILoader<E> {

	/**
	 * Reads the data from the given file and stores them in an ArrayList
	 * 
	 * @param fileName: a String with the name of the input file
	 * @param delimeter: a String with the delimeter between columns of the source file
	 * @param hasHeaderLine: specifies whether the file has a header (true) or not (false)
	 * @param numFields: an int with the number of columns in the input file
	 * @param objCollection: and empty list which will be loaded with the data from the input file
	 * 
	 * @return the number of rows that are eventually added to objCollection
	 */
	int load(String fileName, String delimeter, boolean hasHeaderLine, int numFields, ArrayList<E> objCollection);

}