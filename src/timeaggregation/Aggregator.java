package timeaggregation;

import java.util.ArrayList;

import datamodel.IResult;
import datamodel.ResultModel;
import datamodel.MeasurementRecord;
import datamodel.TimeUnitMappingModel;

/**
 * @class Aggregator
 * @brief Implements the IAggregator interface and its base functions
 *          primarily dealing with producing data aggregations
 */
public class Aggregator implements IAggregator {
    /**
     * timeUnitType -> The time unit we want to use for aggregations
     * result -> The ResultModel that will contain the aggregated by time unit results
     * unitMap -> A map that connects time models with time units
     */
    private String timeUnitType;
    private ResultModel result;
    private final TimeUnitMappingModel unitMap;

    public Aggregator() {
        this.unitMap = new TimeUnitMappingModel();
    }

    /**
     * @message aggregateByTimeUnit
	 * @brief Aggregates measurements by a time unit, e.g., month, day of week, period of day etc.
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

        switch(timeUnitType) {
            case "season":
                return aggregateBySeason(inputMeasurements);
            case "month":
                return aggregateByMonth(inputMeasurements);
            case "dayofweek":
                return aggregateByDayOfWeek(inputMeasurements);
            case "periodofday":
                return aggregateByPeriodOfDay(inputMeasurements);
        }

        System.out.println("The aggregator function input was invalid");
        return null;
    }

    /**
     * @message aggregateByPeriodOfDay
     * @brief Aggregate the input measurements by period of day, mapping specific hours to day periods
     * @param inputMeasurements The data to be aggregated
     * @return The ResultModel of the aggregated data
     */
    private IResult aggregateByPeriodOfDay(ArrayList<MeasurementRecord> inputMeasurements) {
        for(MeasurementRecord record : inputMeasurements) {
            /* For each record in inputMeasurements get the hour
                value and use the mapped version of it as a key for the record */
            result.add(unitMap.getPeriodOfDay().get(record.getTime().getHour()), record);
        }

        result.calculateResult();
        return this.result;
    }

    /**
     * @message aggregateByDayOfWeek
     * @brief Aggregate the input measurements by day of week, mapping specific day encoding values to day names
     * @param inputMeasurements The data to be aggregated
     * @return The Result<Model of the aggregated data
     */
    private IResult aggregateByDayOfWeek(ArrayList<MeasurementRecord> inputMeasurements) {
        /* We first need to map our encoding of week days with the days in a month */
        /* Once we find a specific day's name (Monday, Tuesday..) we calculate the margin to shift */
        for(MeasurementRecord record : inputMeasurements) {
            int day = Integer.parseInt(record.getDate().getDay());
            int month = Integer.parseInt(record.getDate().getMonth());
            int year = Integer.parseInt(record.getDate().getYear());

            /* Call findDayOfWeek so that our table matches the real dates */
            result.add(unitMap.getDays().get(findDayOfWeek(day, month, year)), record);
        }
        
        result.calculateResult();
        return this.result;
    }

    /**
     * @message aggregateByMonth
     * @brief Aggregate the input measurements by month, mapping specific month encoding values to month names
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
     * @message aggregateBySeason
     * @brief Aggregate the input measurements by season, mapping specific month encoding values to season names
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
     * @message findDayOfWeek
     * @brief Finds the name of day knowing only a specific date
     * @param day The day number
     * @param month The month number
     * @param year The year number
     * @return The name of the day
     */
    private String findDayOfWeek(int day, int month, int year) {
        int[] monthCodeTable = {0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4};
        year -= (month < 3) ? 1 : 0; /* Look for a leap year */
        int dayCode = (year + year/4 - year/100 + year/400 + monthCodeTable[month-1] + day) % 7;
        if(dayCode == 0) dayCode = 7; /* Function results 0 for Sunday */
        return "0" + dayCode; /* Either '00', '01', '02'.. and so on */
    }

    public void setTimeUnitType(String timeUnitType) {
        this.timeUnitType = timeUnitType;
    }
}