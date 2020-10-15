package client;

import java.util.ArrayList;
import java.util.Scanner;

import datamodel.IResult;
import datamodel.MeasurementRecord;
import datamodel.History;
import mainengine.Engine;
import mainengine.MainEngineFactory;

/**
 * @class Main
 * @desc: Implements the starting point of the program as well as
 *          all the I/O of the view of the client
 * @param inputFileName a String with the name of the input file
 * @param delimeter a String with the delimeter between columns of the source file
 * @param hasHeaderLine specifies whether the file has a header (true) or not (false)
 * @param numFields an int with the number of columns in the input file
 * @param objCollection an empty list which will be loaded with the data from the input file
 * @param collectionRows the number of rows that are eventually added to objCollection
 * 
 * @param aggregatorType a string belonging to the set "season", "month", "dayofweek", "periodofday"
 *                      to determine by which time unit type the records will be aggregated
 * @param aggFunction a String representing the aggregate function (avg, sum) to be applied to the input
 * @param description a String with a textual description of the result
 * 
 * @param measurementsResult an instance of a class implementing the IResult interface, containing the aggregate results
 * @param exportType a string belonging to the set "text", "md", "html" to determine the type of report that will be generated
 * @param filename a String with the path of the file where the report will be written
 * 
 * @param s a scanner object used for getting user input
 * @param factory the factory that generates mainengines
 * @param engine an Engine object through which we will call our back end functions
 * @param history a model that is used for saving reports in a database (in memory)
 */
public class Main {
    private static String inputFileName = null;
    private static String delimeter = null;
    private static boolean hasHeaderLine = false;
    private static int numFields = -1;
    private static ArrayList<MeasurementRecord> objCollection = null; /* The contents of objColletion */
    private static int collectionRows = -1;

    private static String aggFunction = null;
    private static String aggregatorType = null;
    private static String description = null;

    private static IResult measurementsResult = null;
    private static String exportType = null;
    private static String reportFileName = null;

    private static Scanner s = new Scanner(System.in);
    private static MainEngineFactory factory = new MainEngineFactory();
    private static Engine engine = factory.createMainEngine("MainEngine");
    private static History history = new History();

    /**
     * @func: manageExit
     * @desc: Manages the return types and ensures a safe exit
     * @param s the scanner object
     * @param returnType the way the program exits 
     * @return the return type of the function
     */
    private static int manageExit(Scanner s, int returnType) {
        if(returnType == 0)
            /* Continue with the menu loop */
            return returnType;

        s.close();
        return returnType;
    }

    /**
     * @func: scanInput
     * @desc: Scans an input from the user
     * @param s the scanner object
     * @param message the message to print before receiving an input
     * @return the line captured from the user
     */
    private static String scanInput(Scanner s, String message) {
        System.out.print(message);
        return s.nextLine();
    }

    /**
     * @func: load
     * @desc: Gets the inputs and sends them UNCKECKED for viability into the engine checkers
     * @return the return type
     */
    private static int load() {
        while(true) {
            objCollection = new ArrayList<MeasurementRecord>();
            inputFileName = scanInput(s, "Provide the path of the input resource file: ");
            delimeter = scanInput(s, "Provide the delimeter of the data file: ");
            String hasHeaderLineInput = scanInput(s, "Does the file have a header line (true|false)? ");
            
            if(hasHeaderLineInput.equals("true")) hasHeaderLine = true;
            else if(hasHeaderLineInput.equals("false")) hasHeaderLine = false;

            /* In our case all files have 9 items */
            numFields = 9;
            
            collectionRows = engine.loadData(inputFileName, delimeter, hasHeaderLine, numFields, objCollection);
            
            /* Some case where a specific input or file error was found while checking */
            if(collectionRows == -1) {
                System.out.println("The data was not loaded correctly\n");
                return manageExit(s, 0);
            }

            System.out.println("The data was loaded correctly");
            return manageExit(s, 0);
        }
    }

    /**
     * @func: aggregateByTimeUnit
     * @desc: Gets the inputs and sends them UNCKECKED for viability into the engine checkers
     * @return the return type
     */
    private static int aggregateByTimeUnit() {
        while(true) {
            aggregatorType = scanInput(s, "Input the unit type to which I will aggregate data into (`season`, `month`, `dayofweek`, `periodofday`): ");
            aggFunction = scanInput(s, "Input the type of function to use for aggregating the measurements (`avg`, `sum`): ");
            description = scanInput(s, "Give a small description of the results: ");

            measurementsResult = engine.aggregateByTimeUnit(objCollection, aggregatorType, aggFunction, description);

            if(measurementsResult == null) {
                System.out.println("The data was not measured correctly");
                return manageExit(s, 0);
            }

            System.out.println("The data was measured correctly");
            return manageExit(s, 0);
        }
    }

    /**
     * @func: reportResultsInFile
     * @desc: Gets the inputs and sends them UNCKECKED for viability into the engine checkers
     * @return the return type
     */
    private static int reportResultsInFile() {
        while(true) {
            reportFileName = scanInput(s, "Input a file path to save the report: "); /* Grab the file path */
            exportType = scanInput(s, "Choose the export type (html, md, txt): ");

            if(engine.reportResultInFile(measurementsResult, exportType, reportFileName) == -1) {
                System.out.println("The report could not be created\n");
                return manageExit(s, 0);
            }
            System.out.println("Successfully created a report of the aggregated results");

            /* Already performed bounds checks */
            engine.addToHistory(description, reportFileName, exportType, history);
            return manageExit(s, 0);
        }
    }

    /**
     * @func: listReports
     * @desc: Lists all available reports
     * @return the return type
     */
    private static int listReports() {
        engine.listReports(history);
        return manageExit(s, 0);
    }

    /**
     * @func: mainLoop
     * @desc: Implements a main function but called from a loop
     * @return the return type
     */
    private static int mainLoop() {
        System.out.println("\n1) Load resource file.");
        System.out.println("2) Get aggregate measures.");
        System.out.println("3) Craft a report.");
        System.out.println("4) View the report history.");
        System.out.println("5) Exit.");
        System.out.print("Choose: ");

        String arg = s.nextLine();
        System.out.println();
        switch(arg) {
            case "1": /* Load the resources */
                return load();
            case "2": /* Get aggregate measures */
                return aggregateByTimeUnit();
            case "3": /* Craft the report */
                return reportResultsInFile();
            case "4": /* List the report history */
                return listReports();
            case "5": /* Exit the program */
                System.out.println("Goodbye.");
                return manageExit(s, 1);
            case "DEBUG MODE": /* TODO DEBUG ONLY: RUN A FULL TEST OF THE PROGRAM */
                return engine.autorun(history);
            default: /* Redo the loop */
                return manageExit(s, 0);
            /* return 1 if you want to exit the program on wrong input as well */
        }
    }

    /* main */
    public static void main(String args[]) {
        while(mainLoop() == 0);
        return;
    }
}
