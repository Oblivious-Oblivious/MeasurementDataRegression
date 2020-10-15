package datamodel;

import java.util.ArrayList;
import java.util.HashMap;

public interface IResult {

	/**
	 * Adds a new measurement to the result, appropriately placed
	 * 
	 * @param timeUnit a String by which we aggregate measurements
	 * @param record a MeasurementRecord to be added
	 * 
	 * @return the size of the collection of Measurement objects to which record has been added
	 */
	int add(String timeUnit, MeasurementRecord record);

	/**
	 * Return the textual description for what the result is all about
	 * 
	 * @return A String with the text describing the result
	 */
	String getDescription();

	/**
	 * Returns the source measurements organized per grouping time unit
	 * 
	 * For example, if the grouping is done per month, for String "January" there is an ArrayList of MeasurementRecord with the 
	 * measurements with their date being in January, String "February" has the respective measurements with date in February, and so on...
	 * 
	 * @return A HashMap<String, ArrayList<MeasurementRecord>> with a ArrayList<MeasurementRecord> for each String representing a time unit 
	 */
	HashMap<String, ArrayList<MeasurementRecord>> getDetailedResults();
	
	/**
	 * Stores the aggregate measurements for the Kitchen metric, one for each of the grouper time units 
	 * 
	 * @return A HashMap<String, Double>, where the grouping time unit is represented as a String and the aggregate value as a Double
	 */
	HashMap<String, Double> getAggregateMeterKitchen();

	/**
	 * Stores the aggregate measurements for the Laundry metric, one for each of the grouper time units 
	 * 
	 * @return A HashMap<String, Double>, where the grouping time unit is represented as a String and the aggregate value as a Double
	 */
	HashMap<String, Double> getAggregateMeterLaundry();

	/**
	 * Stores the aggregate measurements for the air condition metric, one for each of the grouper time units 
	 * 
	 * @return A HashMap<String, Double>, where the grouping time unit is represented as a String and the aggregate value as a Double
	 */
	HashMap<String, Double> getAggregateMeterAC();

	/**
	 * The aggregate function used to produce statistics over the source measurements
	 * 
	 * @return A string with the aggregate function (e.g., "sum", "avg", ...}
	 */
	String getAggregateFunction();



}