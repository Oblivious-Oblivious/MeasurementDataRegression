package mainengine;

import java.util.ArrayList;

import datamodel.IResult;
import datamodel.MeasurementRecord;

public interface IMainEngine {
	/**
	 * A method that reads the data from the given file and stores them in an ArrayList
	 * 
	 * @param fileName: a String with the name of the input file
	 * @param delimiter: a String with the delimiter between columns of the source file
	 * @param hasHeaderLine: specifies whether the file has a header (true) or not (false)
	 * @param numFields: an int with the number of columns in the input file
	 * @param objCollection: and empty list which will be loaded with the data from the input file
	 * @return the number of rows that are eventually added to objCollection
	 */
	int loadData(String fileName, String delimiter, Boolean hasHeaderLine, int numFields,
				 ArrayList<MeasurementRecord> objCollection);

	/**
	 * A method that aggregates measurements by a time unit, e.g., month, day of week, period of day etc.
	 * 
	 * @param inputMeasurements the measurements to be aggregated
	 * @param aggregatorType a string belonging to the set "season", "month", "dayofweek", "periodofday" to determine by which time unit type the records will be aggregated 
	 * @param aggFunction a String representing the aggregate function (avg, sum) to be applied to the input
	 * @param description a String with a textual description of the result
	 * @return An IResult object where the input is aggregated by time period, or null if sth goes wrong
	 * 
	 */
	IResult aggregateByTimeUnit(ArrayList<MeasurementRecord> inputMeasurements, String aggregatorType,
								String aggFunction, String description);

	/**
	 * 	A method that reports the contents of an aggregate result to a file
	 * 
	 * @param result an instance of a class implementing the IResult interface, containing the aggregate results
	 * @param reportType a string belonging to the set "text", "md", "html" to determine the type of report that will be generated 
	 * @param filename a String with the path of the file where the report will be written
	 * @return 0 if the task completes successfully; a negative integer otherwise
	 */
	int reportResultInFile(IResult result, String reportType, String filename);
}
