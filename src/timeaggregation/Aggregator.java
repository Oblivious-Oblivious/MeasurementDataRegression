package timeaggregation;

import java.util.ArrayList;

import datamodel.IResult;
import datamodel.ResultModel;
import datamodel.MeasurementRecord;
import datamodel.TimeUnitMappingModel;

/**
 * @class: Aggregator
 * @desc: Implements the IAggregator interface and its base functions
 *          primarily dealing with producing data aggregations
 * @param timeUnitType The time unit we want to use for aggregations
 * @param result The ResultModel that will contain the aggregated by time unit results
 * @param unitMap A map that connects time models with time units
 */
public class Aggregator implements IAggregator {
    private String timeUnitType;
    private ResultModel result;
    private TimeUnitMappingModel unitMap;

    /**
     * @Constructor
     */
    public Aggregator() {
        this.unitMap = new TimeUnitMappingModel();
    }

    /**
     * @func: aggregateByTimeUnit
	 * @desc: Aggregates measurements by a time unit, e.g., month, day of week, period of day etc.
	 * @param inputMeasurements the measurements to be aggregated
	 * @param aggFunction a String representing the aggregate function (avg, sum, ...) to be applied to the input (ONLY USED TO BE PASSED THROUGH TO THE RESULT MODEL)
	 * @param description a String with a textual description of the result (ONLY USED TO BE PASSED THROUGH TO THE RESULT MODEL)
	 * @return A IResult object where the input is aggregated by time period, or null if sth goes wrong
	 */
    @Override
    public IResult aggregateByTimeUnit(ArrayList<MeasurementRecord> inputMeasurements, String aggFunction, String description) {
        result = new ResultModel();
        
        result.setAggregateFunction(aggFunction);
        result.setDescription(description);

        if(timeUnitType.equals("season"))
            return aggregateBySeason(inputMeasurements);
        else if(timeUnitType.equals("month"))
            return aggregateByMonth(inputMeasurements);
        else if(timeUnitType.equals("dayofweek"))
            return aggregateByDayofweek(inputMeasurements);
        else if(timeUnitType.equals("periodofday"))
            return aggregateByPeriodofday(inputMeasurements);

        System.out.println("The aggregator function input was invalid");
        return null;
    }

    /**
     * @func: aggregateByPeriodofday
     * @desc: Aggregate the input measurements by period of day, mapping spesific hours to day periods
     * @param inputMeasurements The data to be aggregated
     * @return The ResultModel of the aggregated data
     */
    private IResult aggregateByPeriodofday(ArrayList<MeasurementRecord> inputMeasurements) {
        for(MeasurementRecord record : inputMeasurements) {
            /* For each record in inputMeasurements get the hour
                value and use the mapped version of it as a key for the record */
            result.add(unitMap.getPeriodOfDay().get(record.getTime().getHour()), record);
        }

        result.calculateResult();
        return this.result;
    }

    /**
     * @func: aggregateByDayofweek
     * @desc: Aggregate the input measurements by day of week, mapping spesific day encoding values to day names
     * @param inputMeasurements The data to be aggregated
     * @return The Result<Model of the aggregated data
     */
    private IResult aggregateByDayofweek(ArrayList<MeasurementRecord> inputMeasurements) {
        /* We first need to map our encoding of week days with the days in a month */
        /* Once we find a specific day's name (Monday, Tuesday..) we calculate the margin to shift */
        for(MeasurementRecord record : inputMeasurements) {
            int day = Integer.parseInt(record.getDate().getDay());
            int month = Integer.parseInt(record.getDate().getMonth());
            int year = Integer.parseInt(record.getDate().getYear());

            /* Call fidnDayOfWeek so that our table matches the real dates */
            result.add(unitMap.getDays().get(findDayOfWeek(day, month, year)), record);
        }
        
        result.calculateResult();
        return this.result;
    }

    /**
     * @func: aggregateByMonth
     * @desc: Aggregate the input measurements by month, mapping spesific month encoding values to month names
     * @param inputMeasurements The data to be aggregated
     * @return The ResultModel of the aggregated data
     */
    private IResult aggregateByMonth(ArrayList<MeasurementRecord> inputMeasurements) {
        for(MeasurementRecord record : inputMeasurements)
            result.add(unitMap.getMonths().get(record.getDate().getMonth()), record);

        result.calculateResult();        
        return this.result;
    }

    /**
     * @func: aggregateBySeason
     * @desc: Aggregate the input measurements by season, mapping spesific month encoding values to season names
     * @param inputMeasurements The data to be aggregated
     * @return The ResultModel of the aggregated data
     */
    private IResult aggregateBySeason(ArrayList<MeasurementRecord> inputMeasurements) {
        for(MeasurementRecord record : inputMeasurements)
            result.add(unitMap.getSeasons().get(record.getDate().getMonth()), record);

        result.calculateResult();        
        return this.result;
    }

    /**
     * @func: findDayOfWeek
     * @desc: Finds the name of day knowing only a spesific date
     * @param day The day number
     * @param month The month number
     * @param year The year number
     * @return The name of the day
     */
    private String findDayOfWeek(int day, int month, int year) {
        int monthCodeTable[] = {0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4};
        year -= (month < 3) ? 1 : 0; /* Look for a leap year */
        int dayCode = (year + year/4 - year/100 + year/400 + monthCodeTable[month-1] + day) % 7;
        if(dayCode == 0) dayCode = 7; /* Function results 0 for Sunday */
        String dayCodeString = "0" + String.valueOf(dayCode);
        return dayCodeString; /* Either '00', '01', '02'.. and so on */
    }

    /**
     * @func: getTimeUnitType
     * @desc: Returns a textual description of the time granularity by which we
     *        aggregate data
     * @return a String with the granularity of the time aggregator
     */
    @Override
    public String getTimeUnitType() {
        return this.timeUnitType;
    }
    public void setTimeUnitType(String timeUnitType) {
        this.timeUnitType = timeUnitType;
    }
}