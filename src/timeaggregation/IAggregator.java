package timeaggregation;

import java.util.ArrayList;

import datamodel.IResult;
import datamodel.MeasurementRecord;

public interface IAggregator {

	/**
	 * Aggregates measurements by a time unit, e.g., month, day of week, period of day etc.
	 * 
	 * @param inputMeasurements the measurements to be aggregated
	 * @param aggFunction a String representing the aggregate function (avg, sum, ...) to be applied to the input
	 * @param description a String with a textual description of the result
	 * @return A IResult object where the input is aggregated by time period, or null if sth goes wrong
	 */
	public IResult aggregateByTimeUnit(ArrayList<MeasurementRecord> inputMeasurements, String aggFunction, String description);

	/**
	 * Returns a textual description of the time granularity by which we aggregate data
	 * 
	 * @return a String with the granularity of the time aggregator
	 */
	public String getTimeUnitType();

}
