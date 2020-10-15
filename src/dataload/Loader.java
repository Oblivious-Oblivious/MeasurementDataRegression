package dataload;

import datamodel.FileHandler;
import datamodel.MeasurementRecord;
import datamodel.DateModel;
import datamodel.TimeModel;

import java.util.ArrayList;

/**
 * @class Loader
 * @desc: Implements the ILoader interface and its base functions
 * 			primarily dealing with loading and reading data files
 * @param <E> -> The type of collection data to read
 * @param delimeter a String with the delimeter between columns of the source fil
 * @param numFields an int with the number of columns in the input file
 * @param objCollection and empty list which will be loaded with the data from the input file
 * @param data a string that holds a line of data from the input files
 * @param fileHandler a custom FileHandler object for managine file descriptors
 */
public class Loader<E extends MeasurementRecord> implements ILoader<E> {
	private String delimeter;
	private int numFields;
	private ArrayList<E> objCollection;
	private String data;
	private FileHandler fileHandler;
	
	/**
	 * @func: writeToCollection
	 * @desc: Writes the data to the provided collection
	 * @return the return type of the function
	 */
	private int writeToCollection() {
		/* Write data while reading */
		while(true) {
			data = fileHandler.readLineFromFile();
			if(data == null)
				break;

			/* In most cases 'E' desolves into MeasurementRecord */
			E dataRecord = createDataRecord(data, delimeter);

			/* Some line had wrong data */
			if(dataRecord == null)
				continue;

			/* The delimeter is different than the one in the file */
			if(dataRecord.get__delimeter_error() == true)
				return -1;

			objCollection.add(dataRecord);
		}
		return 0;
	}

	/**
	 * @func: createDataRecord
	 * @desc: Creates a new E object and sets its fields according to what we read from the input file
	 * @param data the string we split into the different fields
	 * @param delimeter the delimeter to which we split to
	 * @return (downcasted MeasurementRecord into E) the filled data we want to insert to objCollection ArrayList
	 */
	@SuppressWarnings("unchecked")
	private E createDataRecord(String data, String delimeter) {
		String dataItems[] = data.split(delimeter);
		String dateItems[];
		String timeItems[];
		try {
			dateItems = dataItems[0].split("/");
			timeItems = dataItems[1].split(":");
		}
		/* Posible excpetions on spliting, due to wrong inputs on the data files */
		catch(Exception e) {
			System.out.println("The delimeter you set was wrong for the specific input file.");
			MeasurementRecord wrongData = new MeasurementRecord();	
			wrongData.set__delimeter_error(true);
			return (E)wrongData;
		}

		/* Consider for possible inputs between the delimeter that are empty */
		if(dataItems.length != numFields)
			return null;
		
		DateModel dateModel = new DateModel();
		TimeModel timeModel = new TimeModel();
		MeasurementRecord dataRecord = new MeasurementRecord();

		dateModel.setDay(dateItems[0]);
		try {
			/* Trying to access dateItems[1] might reproduce out of index errors due to a wrong hasHeaderLine input */
			dateModel.setMonth(dateItems[1]);
		}
		catch(Exception e) { /* Swallow the error */
			System.out.println("The file has a header line though you provided that it didn't.");
			MeasurementRecord wrongData = new MeasurementRecord();
			wrongData.set__delimeter_error(true);
			return (E)wrongData;
		}
		dateModel.setMonth(dateItems[1]);
		dateModel.setYear(dateItems[2]);

		timeModel.setHour(timeItems[0]);
		timeModel.setMinute(timeItems[1]);
		timeModel.setSecond(timeItems[2]);

		dataRecord.setDate(dateModel);
		dataRecord.setTime(timeModel);
		dataRecord.setGlobal_active_power(Double.parseDouble(dataItems[2]));
		dataRecord.setGlobal_reactive_power(Double.parseDouble(dataItems[3]));
		dataRecord.setVoltage(Double.parseDouble(dataItems[4]));
		dataRecord.setGlobal_intensity(Double.parseDouble(dataItems[5]));
		dataRecord.setSub_metering_1(Double.parseDouble(dataItems[6]));
		dataRecord.setSub_metering_2(Double.parseDouble(dataItems[7]));
		dataRecord.setSub_metering_3(Double.parseDouble(dataItems[8]));
		/* Convert inputs from strings to numeric values */

		return (E)dataRecord;
	}

	/**
	 * @func: load
	 * @desc: Reads the data from the given file and stores them in an ArrayList
	 * @return the number of rows that are eventually added to objCollection
	 * @param filename: a String with the name of the input file
	 * @param delimeter: a String with the delimeter between columns of the source file
	 * @param hasHeaderLine: specifies whether the file has a header (true) or not (false)
	 * @param numFields: an int with the number of columns in the input file
	 * @param objCollection: and empty list which will be loaded with the data from the input file
	 * @return the number of rows that are eventually added to objCollection
	 */
	@Override
	public int load(String filename, String delimeter, boolean hasHeaderLine, int numFields, ArrayList<E> objCollection) {
		this.delimeter = delimeter;
		this.numFields = numFields;
		this.objCollection = objCollection;

		this.fileHandler = new FileHandler(filename);
		
		/* Create a managed file descriptor */
		if(fileHandler.createReaderFD() == -1)
			return -1;

		if(hasHeaderLine) {
			/* Start reading from 2nd row and ignore the first line */
			fileHandler.readLineFromFile();
			if(writeToCollection() == -1)
				return -1;
		}
		/* Start reading from 1st row */
		else
			if(writeToCollection() == -1)
				return -1;

		fileHandler.closeFD();
		/* TODO CHECK IF WE CLEAR THE COLLECTION AFTER WE GET MEASURES OR NOT */
		return objCollection.size();
	}
}