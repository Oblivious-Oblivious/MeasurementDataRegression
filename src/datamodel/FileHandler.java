package datamodel;

import java.io.File;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.Objects;

/**
 * @class FileHandler
 * @brief Implements the functions of reading data from and writing to a file
 */
public class FileHandler {
    /**
     * reader -> A reader file object
     * writer -> A writer file object
     * fd -> The buffered reader file descriptor
     * filename -> the filename we want to either read or write to
     */
    private FileReader reader;
    private FileWriter writer;
    private BufferedReader fd;
    private final String filename;

    public FileHandler(String filename) {
        this.reader = null;
        this.writer = null;
        this.fd = null;
        this.filename = filename;
    }

    /**
     * @message write
     * @brief Manages the errors of writing data to a file
     * @param data the data to write
     */
    private int write(String data) {
        try {
            /* Reset the file stream */
            Objects.requireNonNull(getWriterFD()).write(data);
            return 0;
        }
        catch(Exception e) {
            System.out.println("There was an error with writing data to the output file.");
            return -1;
        }
    }

    /**
     * @message readLine
     * @brief Manages the errors of reading data from a file
     * @return the line read
     */
    private String readLine() {
        try {
            /* Reset the file stream */
            return Objects.requireNonNull(getReaderFD()).readLine();
        }
        catch(Exception e) {
            System.out.println("There was an error with reading a line from the input file.");
            return null;
        }
    }

    /**
     * @message getReaderFD
     * @brief Manages the errors of a file descriptor getter
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
     * @message getWriterFD
     * @brief Manages the errors of a file descriptor getter
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
     * @message createWriterFD
     * @brief Manages the errors of creating a writer file object
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
     * @message createReaderFD
     * @brief Manages the errors of creating a reader file object as well as its buffered reader
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
     * @message readFromFile
     * @brief Read data from a file
     * @return The data captured from the file descriptor 
     */
    public String readLineFromFile() {
        return readLine();
    }

    /**
     * @message writeToFile
     * @brief Write data to file (NOT APPEND)
     * @param data A string of data to write
     */
    public int writeToFile(String data) {
        if(write(data) == 0)
            return 0;
        else
            return -1;
    }

    /**
     * @message closeFD()
     * @brief Close all file descriptors after flushing any data remaining
     */
    public void closeFD() {
        try {
            if(writer != null) writer.flush();
            if(reader != null) reader.close();
            if(writer != null) writer.close();
        }
        catch(Exception e) {
            System.out.println("There was an error with closing the files.");
        }
    }
    
    /**
     * @message appendToDB
     * @brief Write data ReportMetadataModel objects to the database
     * @param metadata The metadata to write
     **/
    public void appendToDB(ReportMetadataModel metadata) {
    	try {
    		File file = new File("___db.dbfile");
            FileWriter fr = new FileWriter(file, true);
            
            String desc = metadata.getDescription();
            String export = metadata.getExportType();
            String output = metadata.getOutputPath();
            
            /* Write the string with the ';' delimiter and append a newline at the end */
            fr.append(desc)
                    .append(";")
                    .append(export)
                    .append(";")
                    .append(output)
                    .append("\n");
            fr.close();
    	}
    	catch(Exception e) {
    		/* System.out.println(e) */
    	}
    }
}
