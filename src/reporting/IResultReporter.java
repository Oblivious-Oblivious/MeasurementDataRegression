package reporting;

import datamodel.IResult;

public interface IResultReporter {

	/**
	 * Reports the contents of an aggregate result to a file
	 * 
	 * @param result an instance of a class implementing the IResult interface, containing the aggregate results
	 * @param filename a String with the path of the file where the report will be written
	 * @return 0 if the task completes successfully; a negative integer otherwise
	 */
	int reportResultInFile(IResult result, String filename);
}
