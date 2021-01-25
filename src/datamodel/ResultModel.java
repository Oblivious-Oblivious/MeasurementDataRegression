package datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * @class ResultModel
 * @brief Implements the IResult interface and its base function
 *          primarily dealing with constructing the model of the measurement results
 */
public class ResultModel implements IResult {
    /**
     * description -> a String with a textual description of the result
     * detailedResults -> the detailed results hashmap that contains the grouped measurements per time unit
     * aggregateFunction -> a String representing the aggregate function (avg, sum) to be applied to the record
     */
    private String description;
    private String aggregateFunction;
    private final HashMap<String, ArrayList<MeasurementRecord>> detailedResults;
    private HashMap<String, Double> kitchenMeter;
    private HashMap<String, Double> laundryMeter;
    private HashMap<String, Double> acMeter;

    public ResultModel() {
        detailedResults = new HashMap<>();
        kitchenMeter = new HashMap<>();
        laundryMeter = new HashMap<>();
        acMeter = new HashMap<>();
    }

    /**
     * @message add
	 * @brief Adds a new measurement to the result, appropriately placed
	 * @param timeUnit a String by which we aggregate measurements
	 * @param record a MeasurementRecord to be added
	 * @return the size of the collection of Measurement objects to which the record has been added
	 */
    @Override
    public int add(String timeUnit, MeasurementRecord record) {
        /* We have already validated our data and fields */

        /* Check if the key does not exist yet */
        ArrayList<MeasurementRecord> updatedList;
        try {
            /* In this case the key already exists */
            /* Get the ArrayList of the specific time unit */
            updatedList = detailedResults.get(timeUnit);
            updatedList.add(record);
        }
        catch(Exception e) {
            updatedList = new ArrayList<>();
            updatedList.add(record);
        }

        /* Edit the ArrayList stored as a value to the specific time unit with the updated one */
        detailedResults.put(timeUnit, updatedList);

        return updatedList.size();
    }

    /**
     * @message getDescription
	 * @brief Return the textual description for what the result is all about
	 * @return A String with the text describing the result
	 */
    @Override
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @message getDetailedResults
	 * @brief Returns the source measurements organized per grouping time unit
	 *          For example, if the grouping is done per month, for String "January" there is an ArrayList of MeasurementRecord with the 
	 *          measurements with their date being in January, String "February" has the respective measurements with date in February, and so on...
	 * @return A HashMap<String, ArrayList<MeasurementRecord>> with a ArrayList<MeasurementRecord> for each String representing a time unit 
	 */
    @Override
    public HashMap<String, ArrayList<MeasurementRecord>> getDetailedResults() {
        return this.detailedResults;
    }

    /**
     * @message calculateResult
     * @brief Calculates all 3 hashmaps containing avg or sum of type of elements
     */
    public void calculateResult() {
        kitchenMeter = calculateResultByType("kitchen");
        laundryMeter = calculateResultByType("laundry");
        acMeter = calculateResultByType("ac");
    }

    /**
     * @message calculateResultByType
     * @brief Calculates the metrics according to the time unit and device(kitchen, laundry, ac)
     * @param meterType The device to aggregate results for
     */
    /* TODO CAN BE IMPLEMENTED MUCH BETTER WITH FUNCTIONAL INTERFACES */
    private HashMap<String, Double> calculateResultByType(String meterType) {
        HashMap<String, Double> meter = new HashMap<>();
        ArrayList<Double> sumOfMetrics = new ArrayList<>();
        double sum;

        /* Iterate over the results added during the aggregation of MeasurementRecords */
        for(HashMap.Entry<String, ArrayList<MeasurementRecord>> entry : detailedResults.entrySet()) {
            /* entry is an object containing key and value */
            /* entry.getKey() gives the key */
            /* entry.getValue() is the ArrayList of MeasurementRecords with a date matching the key */
        	if(Objects.equals(meterType, "kitchen"))
                for(MeasurementRecord record : entry.getValue())
                    sumOfMetrics.add(record.getSub_metering_1());
            else if(Objects.equals(meterType, "laundry"))
                for(MeasurementRecord record : entry.getValue())
                    sumOfMetrics.add(record.getSub_metering_2());
            else if(Objects.equals(meterType, "ac"))
                for(MeasurementRecord record : entry.getValue())
                    sumOfMetrics.add(record.getSub_metering_3());

            sum = 0.0;
            for(double add_me : sumOfMetrics)
                sum += add_me;
            
            if(aggregateFunction.equals("avg"))
                /* Insert avg = sum/size into the meter HashMap using the key as the time unit */
                meter.put(entry.getKey(), sum / (double)sumOfMetrics.size());
            else
                /* Insert the sum into the meter HashMap using the key as the time unit */
                meter.put(entry.getKey(), sum);
        }

        return meter;
    }

    /**
     * @message getAggregateMeterKitchen
	 * @brief Stores the aggregate measurements for the Kitchen metric, one for each of the grouper time units
	 * @return A HashMap<String, Double>, where the grouping time unit is represented as a String and the aggregate value as a Double
	 */
    @Override
    public HashMap<String, Double> getAggregateMeterKitchen() {
        return kitchenMeter;
    }

    /**
     * @message getAggregateMeterLaundry
	 * @brief Stores the aggregate measurements for the Laundry metric, one for each of the grouper time units
	 * @return A HashMap<String, Double>, where the grouping time unit is represented as a String and the aggregate value as a Double
	 */
    @Override
    public HashMap<String, Double> getAggregateMeterLaundry() {
        return laundryMeter;
    }

    /**
     * @message getAggregateMeterAC
	 * @brief Stores the aggregate measurements for the air condition metric, one for each of the grouper time units
	 * @return A HashMap<String, Double>, where the grouping time unit is represented as a String and the aggregate value as a Double
	 */
    @Override
    public HashMap<String, Double> getAggregateMeterAC() {
        return acMeter;
    }

    public void setAggregateFunction(String aggregateFunction) {
        this.aggregateFunction = aggregateFunction;
    }
}