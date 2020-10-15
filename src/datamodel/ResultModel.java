package datamodel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @class: ResultModel
 * @desc: Implements the IResult interface and its base function
 *          primarily dealing with constructing the model of the measurement results
 * @param description a String with a textual description of the result
 * @param detailedResults the detailed results hashmap that contains the grouped measurements per time unit
 * @param aggregateFunction a String representing the aggregate function (avg, sum) to be applied to the record
 */
public class ResultModel implements IResult {
    private String description;
    private String aggregateFunction;
    private ArrayList<MeasurementRecord> updatedList;
    private HashMap<String, ArrayList<MeasurementRecord>> detailedResults;
    private HashMap<String, Double> kitchenMeter;
    private HashMap<String, Double> laundryMeter;
    private HashMap<String, Double> acMeter;

    /**
     * @Constructor
     */
    public ResultModel() {
        detailedResults = new HashMap<String, ArrayList<MeasurementRecord>>();
        kitchenMeter = new HashMap<String, Double>();
        laundryMeter = new HashMap<String, Double>();
        acMeter = new HashMap<String, Double>();
    }

    /**
     * @func: add
	 * @desc: Adds a new measurement to the result, appropriately placed
	 * @param timeUnit a String by which we aggregate measurements
	 * @param record a MeasurementRecord to be added
	 * @return the size of the collection of Measurement objects to which the record has been added
	 */
    @Override
    public int add(String timeUnit, MeasurementRecord record) {
        /* We have already validated our data and fields */

        /* Check if the key does not exist yet */
        try {
            /* In this case the key already exists */
            /* Get the ArrayList of the specific time unit */
            updatedList = detailedResults.get(timeUnit);
            updatedList.add(record);
        }
        catch(Exception e) {
            updatedList = new ArrayList<MeasurementRecord>();
            updatedList.add(record);
        }

        /* Edit the ArrayList stored as a value to the specific time unit with the updated one */
        detailedResults.put(timeUnit, updatedList);

        return updatedList.size();
    }

    /**
     * @func: getDescription
	 * @desc: Return the textual description for what the result is all about
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
     * @func: getDetailedResults
	 * @desc: Returns the source measurements organized per grouping time unit
	 *          For example, if the grouping is done per month, for String "January" there is an ArrayList of MeasurementRecord with the 
	 *          measurements with their date being in January, String "February" has the respective measurements with date in February, and so on...
	 * @return A HashMap<String, ArrayList<MeasurementRecord>> with a ArrayList<MeasurementRecord> for each String representing a time unit 
	 */
    @Override
    public HashMap<String, ArrayList<MeasurementRecord>> getDetailedResults() {
        return this.detailedResults;
    }

    /**
     * @func: calculateResult
     * @desc: Calculates all 3 hashmaps containing avg or sum of type of elements
     */
    public void calculateResult() {
        kitchenMeter = calculateResultByType("kitchen");
        laundryMeter = calculateResultByType("laundry");
        acMeter = calculateResultByType("ac");
    }

    /**
     * @func: calculateResultByType
     * @desc: Calculates the metrics according to the time unit and device(kitchen, laundry, ac)
     * @param meterType The device to aggregate results for
     */
    /* TODO CAN BE IMPLEMENTED MUCH BETTER WITH FUNCTIONAL INTERFACES */
    private HashMap<String, Double> calculateResultByType(String meterType) {
        HashMap<String, Double> meter = new HashMap<String, Double>();
        ArrayList<Double> sumOfMetrics = new ArrayList<Double>();
        double sum = 0.0;

        /* Iterate over the results added during the aggregation of MeasurementRecords */
        for(HashMap.Entry<String, ArrayList<MeasurementRecord>> entry : detailedResults.entrySet()) {
            /* entry is an object containing key and value */
            /* entry.getKey() gives the key */
            /* entry.getValue() is the ArrayList of MeasurementRecords with a date matching the key */
        	if(meterType == "kitchen")
                for(MeasurementRecord record : entry.getValue())
                    sumOfMetrics.add(record.getSub_metering_1());
            else if(meterType == "laundry")
                for(MeasurementRecord record : entry.getValue())
                    sumOfMetrics.add(record.getSub_metering_2());
            else if(meterType == "ac")
                for(MeasurementRecord record : entry.getValue())
                    sumOfMetrics.add(record.getSub_metering_3());

            sum = 0.0;
            for(double addme : sumOfMetrics)
                sum += addme;
            
            if(aggregateFunction.equals("avg"))
                /* Insert avg = sum/size into the meter HashMap using the key as the time unit */
                meter.put(entry.getKey(), (double)sum/(double)sumOfMetrics.size());
            else
                /* Insert the sum into the meter HashMap using the key as the time unit */
                meter.put(entry.getKey(), sum);
        }

        return meter;
    }

    /**
     * @func: getAggregateMeterKitchen
	 * @desc: Stores the aggregate measurements for the Kitchen metric, one for each of the grouper time units 
	 * @return A HashMap<String, Double>, where the grouping time unit is represented as a String and the aggregate value as a Double
	 */
    @Override
    public HashMap<String, Double> getAggregateMeterKitchen() {
        return kitchenMeter;
    }

    /**
     * @func: getAggregateMeterLaundry
	 * @desc: Stores the aggregate measurements for the Laundry metric, one for each of the grouper time units 
	 * @return A HashMap<String, Double>, where the grouping time unit is represented as a String and the aggregate value as a Double
	 */
    @Override
    public HashMap<String, Double> getAggregateMeterLaundry() {
        return laundryMeter;
    }

    /**
     * @func: getAggregateMeterAC
	 * @desc: Stores the aggregate measurements for the air condition metric, one for each of the grouper time units 
	 * @return A HashMap<String, Double>, where the grouping time unit is represented as a String and the aggregate value as a Double
	 */
    @Override
    public HashMap<String, Double> getAggregateMeterAC() {
        return acMeter;
    }

    /**
     * @func: getAggregateFunction
     * @desc: The aggregate function used to produce statistics over the source measurements
	 * @return A string with the aggregate function (e.g., "sum", "avg", ...}
	 */
    @Override
    public String getAggregateFunction() {
        return this.aggregateFunction;
    }
    public void setAggregateFunction(String aggregateFunction) {
        this.aggregateFunction = aggregateFunction;
    }
}