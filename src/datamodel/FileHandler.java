package datamodel;

import java.io.File;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;

/**
 * @class: FileHandler
 * @desc: Implements the funcitons of reading data from and writing to a file
 * @param reader A reader file object
 * @param writer A writer file object
 * @param fd The buffered reader file descriptor
 * @param filename the filename we want to either read or write to
 */
public class FileHandler {
    private FileReader reader;
    private FileWriter writer;
    private BufferedReader fd;
    private String filename;

    /**
     * @Constructor
     */
    public FileHandler(String filename) {
        this.reader = null;
        this.writer = null;
        this.fd = null;
        this.filename = filename;
    }

    /**
     * @func: write
     * @desc: Manages the errors of writing data to a file
     * @param data the data to write
     */
    private int write(String data) {
        try {
            /* Reset the file stream */
            getWriterFD().write(data);
            return 0;
        }
        catch(Exception e) {
            System.out.println("There was an error with writing data to the output file.");
            return -1;
        }
    }

    /**
     * @func: readLine
     * @desc: Manages the errors of reading data from a file
     * @param fd The file descriptor buffered reader to read from
     * @return the line read
     */
    private String readLine(BufferedReader fd) {
        try {
            /* Reset the file stream */
            return getReaderFD().readLine();
        }
        catch(Exception e) {
            System.out.println("There was an error with reading a line from the input file.");
            return null;
        }
    }

    /**
     * @func: getReaderFD
     * @desc: Manages the errors of a file descriptor getter
     * @return the file descriptor
     */
    private BufferedReader getReaderFD() {
        try {
            return fd;
        }
        catch(Exception e) {
            return null;
        }
    }

    /**
     * @func: getWriterFD
     * @desc: Manages the errors of a file descriptor getter
     * @return the file descriptor
     */
    private FileWriter getWriterFD() {
        try {
            return writer;
        }
        catch(Exception e) {
            return null;
        }
    }

    /**
     * @func: createWriterFD
     * @desc: Manages the errors of creating a writer file object
     */
    public int createWriterFD() {
        try {
            writer = new FileWriter(filename);
            return 0;
        }
        catch(Exception e) {
            System.out.println("There was an error with creating the output file.");
            return -1;
        }
    }

    /**
     * @func: createReaderFD
     * @desc: Manages the errors of creating a reader file object as well as its buffered reader
     */
    public int createReaderFD() {
        try {
            reader = new FileReader(filename);
            fd = new BufferedReader(reader);
            return 0;
        }
        catch(Exception e) {
            System.out.println("There was an error with creating the input file.");
            return -1;
        }
    }

    /**
     * @func: readFromFile
     * @desc: Read data from a file
     * @return The data captured from the file descriptor 
     */
    public String readLineFromFile() {
        return readLine(getReaderFD());
    }

    /**
     * @func: writeToFile
     * @desc: Write data to file (NOT APPEND)
     * @param data A string of data to write
     */
    public int writeToFile(String data) {
        if(write(data) == 0)
            return 0;
        else
            return -1;
    }

    /**
     * @func: closeFD()
     * @desc: Close all file descriptors after flushing any data remaining
     */
    public int closeFD() {
        try {
            if(writer != null) writer.flush();
            if(reader != null) reader.close();
            if(writer != null) writer.close();
            return 0;
        }
        catch(Exception e) {
            System.out.println("There was an error with closing the files.");
            return -1;
        }
    }
    
    /**
     * @func: appendToDB
     * @desc: Write data ReportMetadataModel objects to the database 
     * @param metadata The metadata to write
     **/
    public void appendToDB(ReportMetadataModel metadata) {
    	try {
    		File file = new File("___db.dbfile");
            FileWriter fr = new FileWriter(file, true);
            
            String desc = metadata.getDescription();
            String export = metadata.getExportType();
            String output = metadata.getOutputPath();
            
            /* Write the string with the ';' delimeter and append a newline at the end */
            fr.append(desc + ";" + export + ";" + output + "\n");
            fr.close();
    	}
    	catch(Exception e) {
    		/* System.out.println(e) */
    	}
    }
}