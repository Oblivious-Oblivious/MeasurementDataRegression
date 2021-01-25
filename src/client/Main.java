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
 * @brief Implements the starting point of the program as well as
 *          all the I/O of the view of the client
 */
public class Main {
    /**
     * hasHeaderLine -> specifies whether the file has a header (true) or not (false)
     * objCollection -> an empty list which will be loaded with the data from the input file
     *
     * description -> a String with a textual description of the result
     *
     * measurementsResult -> an instance of a class implementing the IResult interface, containing the aggregate results
     *
     * s -> a scanner object used for getting user input
     * factory -> the factory that generates mainEngines
     * engine -> an Engine object through which we will call our back end functions
     * history -> a model that is used for saving reports in a database (in memory)
     */
    private static boolean hasHeaderLine = false;
    private static ArrayList<MeasurementRecord> objCollection = null; /* The contents of objCollection */

    private static String description = null;

    private static IResult measurementsResult = null;

    private static final Scanner s = new Scanner(System.in);
    private static final MainEngineFactory factory = new MainEngineFactory();
    private static final Engine engine = factory.createMainEngine("MainEngine");
    private static final History history = new History();

    /**
     * @message manageExit
     * @brief Manages the return types and ensures a safe exit
     * @param returnType the way the program exits
     * @return the return type of the function
     */
    private static int manageExit(int returnType) {
        if(returnType == 0)
            /* Continue with the menu loop */
            return returnType;

        Main.s.close();
        return returnType;
    }

    /**
     * @message scanInput
     * @brief Scans an input from the user
     * @param message the message to print before receiving an input
     * @return the line captured from the user
     */
    private static String scanInput(String message) {
        System.out.print(message);
        return Main.s.nextLine();
    }

    /**
     * @message load
     * @brief Gets the inputs and sends them UNCHECKED for viability into the engine checkers
     * @return the return type
     */
    private static int load() {
        while(true) {
            objCollection = new ArrayList<>();
            String inputFileName = scanInput("Provide the path of the input resource file: ");
            String delimiter = scanInput("Provide the delimiter of the data file: ");
            String hasHeaderLineInput = scanInput("Does the file have a header line (true|false)? ");

            if(hasHeaderLineInput.equals("true")) hasHeaderLine = true;
            else if(hasHeaderLineInput.equals("false")) hasHeaderLine = false;

            /* In our case all files have 9 items */
            int numFields = 9;

            int collectionRows = engine.loadData(inputFileName, delimiter, hasHeaderLine, numFields, objCollection);

            /* Some case where a specific input or file error was found while checking */
            if(collectionRows == -1) {
                System.out.println("The data was not loaded correctly\n");
                return manageExit(0);
            }

            System.out.println("The data was loaded correctly");
            return manageExit(0);
        }
    }

    /**
     * @message aggregateByTimeUnit
     * @brief Gets the inputs and sends them UNCHECKED for viability into the engine checkers
     * @return the return type
     */
    private static int aggregateByTimeUnit() {
        while(true) {
            String aggregatorType = scanInput("Input the unit type to which I will aggregate data into (`season`, `month`, `dayofweek`, `periodofday`): ");
            String aggFunction = scanInput("Input the type of function to use for aggregating the measurements (`avg`, `sum`): ");
            description = scanInput("Give a small description of the results: ");

            measurementsResult = engine.aggregateByTimeUnit(objCollection, aggregatorType, aggFunction, description);

            if(measurementsResult == null) {
                System.out.println("The data was not measured correctly");
                return manageExit(0);
            }

            System.out.println("The data was measured correctly");
            return manageExit(0);
        }
    }

    /**
     * @message reportResultsInFile
     * @brief Gets the inputs and sends them UNCHECKED for viability into the engine checkers
     * @return the return type
     */
    private static int reportResultsInFile() {
        while(true) {
            String reportFileName = scanInput("Input a file path to save the report: "); /* Grab the file path */
            String exportType = scanInput("Choose the export type (html, md, txt): ");

            if(engine.reportResultInFile(measurementsResult, exportType, reportFileName) == -1) {
                System.out.println("The report could not be created\n");
                return manageExit(0);
            }
            System.out.println("Successfully created a report of the aggregated results");

            /* Already performed bounds checks */
            engine.addToHistory(description, reportFileName, exportType, history);
            return manageExit(0);
        }
    }

    /**
     * @message listReports
     * @brief Lists all available reports
     * @return the return type
     */
    private static int listReports() {
        engine.listReports(history);
        return manageExit(0);
    }

    /**
     * @message mainLoop
     * @brief Implements a main function but called from a loop
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
                return manageExit(1);
            case "DEBUG MODE": /* TODO DEBUG ONLY: RUN A FULL TEST OF THE PROGRAM */
                return engine.autorun(history);
            default: /* Redo the loop */
                return manageExit(0);
            /* return 1 if you want to exit the program on wrong input as well */
        }
    }

    /* main */
    public static void main(String[] args) {
        while(mainLoop() == 0);
    }
}
