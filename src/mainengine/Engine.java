package mainengine;

import dataload.Loader;
import datamodel.IResult;
import datamodel.MeasurementRecord;
import datamodel.ReportMetadataModel;
import datamodel.History;
import timeaggregation.Aggregator;
import reporting.Reporter;

import java.io.File;
import java.util.ArrayList;

/**
 * @class Engine
 * @brief Implements the base functions of IMainEngine interface
 *          primarily dealing with the controller part of the front end view
 */
public class Engine implements IMainEngine {
    /**
     * loader -> the Loader object implemented for loading data files
     * aggregator -> the Aggregator object implemented for measuring and aggregating data
     * reported -> the Reporter object implemented for writing reports about the measured data
     */
    private final Loader<MeasurementRecord> loader;
    private final Aggregator aggregator;
    private final Reporter reporter;

    public Engine() {
        loader = new Loader<>();
        aggregator = new Aggregator();
        reporter = new Reporter();
    }

    /**
     * @message loadData
	 * @brief A method that reads the data from the given file and stores them in an ArrayList
	 * @param filename a String with the name of the input file
	 * @param delimiter a String with the delimiter between columns of the source file
	 * @param hasHeaderLine specifies whether the file has a header (true) or not (false)
	 * @param numFields an int with the number of columns in the input file
	 * @param objCollection and empty list which will be loaded with the data from the input file
	 * @return the number of rows that are eventually added to objCollection
	 */
    @Override
    public int loadData(String filename, String delimiter, Boolean hasHeaderLine, int numFields, ArrayList<MeasurementRecord> objCollection) {
        if(filename == null) {
            System.out.println("The given output filename is not valid.");
            return -1;
        }

        /* Check if the filename exists as a name in the filesystem */
        File checkDir = new File(filename);
        if(!checkDir.exists() || checkDir.isDirectory()) {
            /* It does not exits neither it is a directory */
            System.out.println("There does not exist a file with this name.");
            return -1;
        }
        if(delimiter == null) {
            System.out.println("There is no delimiter given.");
            return -1;
        }
        if(numFields != 9) {
            System.out.println("The number of columns is given incorrectly.");
            return -1;
        }
        if(objCollection == null) {
            System.out.println("The objCollection is null");
            return -1;
        }
        return loader.load(filename, delimiter, hasHeaderLine, numFields, objCollection);
    }

    /**
     * @message aggregateByTimeUnit
	 * @brief A method that aggregates measurements by a time unit, e.g., month, day of week, period of day etc.
	 * @param inputMeasurements the measurements to be aggregated
	 * @param aggregatorType a string belonging to the set "season", "month", "dayofweek", "periodofday" to determine by which time unit type the records will be aggregated 
	 * @param aggFunction a String representing the aggregate function (avg, sum) to be applied to the input
	 * @param description a String with a textual description of the result
	 * @return An IResult object where the input is aggregated by time period, or null if sth goes wrong
	 */
    @Override
    public IResult aggregateByTimeUnit(ArrayList<MeasurementRecord> inputMeasurements, String aggregatorType, String aggFunction, String description) {
        if(inputMeasurements == null) {
            System.out.println("The input measurements list is empty.");
            return null;
        }
        if(aggregatorType == null) {
            System.out.println("The aggregator time type is not given.");
            return null;
        }
        if(aggFunction == null) {
            System.out.println("The type of aggregate function is not given.");
            return null;
        }
        if(description == null || description.equals("")) {
            System.out.println("A description about the measurements was not given.");
            return null;
        }

        aggregator.setTimeUnitType(aggregatorType);
        return aggregator.aggregateByTimeUnit(inputMeasurements, aggFunction, description);
    }

    /**
     * @message reportResultInFile
	 * @brief A method that reports the contents of an aggregate result to a file
	 * @param result an instance of a class implementing the IResult interface, containing the aggregate results
	 * @param reportType a string belonging to the set "text", "md", "html" to determine the type of report that will be generated 
	 * @param filename a String with the path of the file where the report will be written
	 * @return 0 if the task completes successfully; a negative integer otherwise
	 */
    @Override
    public int reportResultInFile(IResult result, String reportType, String filename) {
        if(result == null) {
            System.out.println("There are no results in memory measured.");
            return -1;
        }
        if(reportType == null) {
            System.out.println("There has been no report type given.");
            return -1;
        }
        if(filename == null) {
            System.out.println("The given output filename is not valid.");
            return -1;
        }

        File checkDir = new File(filename);
        if(checkDir.exists()) {
            /* It exits or it IS a directory */
            System.out.println("There already exists a file with this name. Choose a different name.");
            return -1;
        }

        /* Through the setter, the class gains access of the export type */
        reporter.setExportType(reportType);
        return reporter.reportResultInFile(result, filename);
    }

    /**
     * @message addToHistory
     * @brief Creates a ReportMetadataModel model and inserts is to the history db
     * @param description the description of the specific measurements
     * @param reportFileName the filename of the report file
     * @param exportType the export type (md, txt, html)
     * @param history the history object being carried from main
     */
    public void addToHistory(String description, String reportFileName, String exportType, History history) {
        ReportMetadataModel metadata = new ReportMetadataModel();
        metadata.setDescription(description);
        metadata.setOutputPath(reportFileName);
        metadata.setExportType(exportType);
        metadata.correctOutputPath();
        history.saveReport(metadata);
    }

    /**
     * @message listReports
     * @brief List all the available reports save in the history db
     * @param history the history object being carried from main
     */
    public void listReports(History history) {
        history.listReports();
    }

    /**
     * @message autorun
     * @brief Run a test version of all program utilities
     * @return the return state
     */
    public int autorun(History  history) {
		String inputFilename = "./Resources/Data/2007-2009_full.tsv";
		String delimiter = "\t";
        int numFields = 9;
        ArrayList<MeasurementRecord> objCollection = new ArrayList<>();
        String outputFilename = "./Resources/TestOutput/2007-2009_full.html";
        String description = "Season average on full sample";
        String exportType = "html";
        
        int numRows = loadData(inputFilename, delimiter, true, numFields, objCollection);
		System.out.println("Size to process: " + numRows);
		
        IResult result = aggregateByTimeUnit(objCollection, "dayofweek", "avg", description);
		System.out.println("Time units with measurements: " + result.getDetailedResults().size());
		
        int printOutcome = reportResultInFile(result, exportType, outputFilename);
        System.out.println("printOutcome: " + printOutcome);
        addToHistory(description, outputFilename, exportType, history);
        return 0;
    }
}
