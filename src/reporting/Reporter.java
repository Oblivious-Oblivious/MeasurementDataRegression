package reporting;

import datamodel.IResult;
import datamodel.FileHandler;

import java.lang.StringBuffer;

/**
 * @class: Reporter
 * @desc: Implements the IResultReporter interface and its base functions
 * 			primarily dealing with crafting a report of the collected data
 * @param exportType The type of export file (html, md, txt)
 * @param result The aggregated results object containing hashmaps
 * @param filename The name of the file to write to
 * @param fileHandler The file handler object for writing the reports
 */
public class Reporter implements IResultReporter {
	private String exportType;
	private IResult result;
	private String filename;
	private FileHandler fileHandler;

	/**
	 * @func: writeReport
	 * @desc: Writes the report on the database, and stores it for the next use case 
	 * @param constructedData The data string constructed according to the export type
	 * @return the return type of the function
	 */
	private int writeReport(String constructedData) {
		/* Error messages will be printed inside the file handler model */
		if(fileHandler.writeToFile(constructedData) == -1)
			return -1;
		return 0;
	}

	/**
	 * @func: writeAsHtml
	 * @desc: Constructs a string buffer of report data using the html format
	 * @return the return type of the function
	 */
	private int writeAsHtml() {
		/* Thread safe way */
		/* We use a string buffer that has atomic properties,
			so that we can use it on the forEach functional iterator */
		StringBuffer constructedData = new StringBuffer();
		constructedData.append("<!doctype html>\n")
			.append("<html>\n<head>\n")
			.append("<meta http-equiv=\"Content-Type\" content\"text/html; charset=windows-1253\">\n")
			.append("<title>").append(result.getDescription()).append("</title>\n")
			.append("</head>\n<body>\n\n")
			.append("<h1>").append(result.getDescription()).append("</h1>\n\n")
			.append("<p>avg consumption (watt-hours) over (a) Kitchen, (b) Laundry, (c) A/C</p>\n\n")
			.append("<h2> Kitchen</h2>\n")
			.append("<ul>\n");

		result.getAggregateMeterKitchen().forEach((key, value) -> {
			constructedData.append("<li>" + key + ": &nbsp;&nbsp;&nbsp;&nbsp;" + value + "\n");
		});

		constructedData.append("</ul>\n\n<h2> Laundry</h2>\n<ul>\n");
		
		result.getAggregateMeterLaundry().forEach((key, value) -> {
			constructedData.append("<li>" + key + ": &nbsp;&nbsp;&nbsp;&nbsp;" + value + "\n");
		});

		constructedData.append("</ul>\n\n<h2> A/C</h2>\n<ul>\n");

		result.getAggregateMeterAC().forEach((key, value) -> {
			constructedData.append("<li>" + key + ": &nbsp;&nbsp;&nbsp;&nbsp;" + value + "\n");
		});
		constructedData.append("</ul>\n\n");

		if(writeReport(constructedData.toString()) == -1)
			return -1;

		fileHandler.closeFD();
		return 0;
	}

	/**
	 * @func: writeAsMd
	 * @desc: Constructs a string buffer of report data using the markdown format
	 * @return the return type of the function
	 */
	private int writeAsMd() {
		StringBuffer constructedData = new StringBuffer();
		constructedData.append("# ").append(result.getDescription())
			.append("\n\n")
			.append("avg consumption (watt-hours) over (a) Kitchen, (b) Laundry, (c) A/C\n\n")
			.append("## Kitchen\n\n");

		result.getAggregateMeterKitchen().forEach((key, value) -> {
			constructedData.append("* " + key + ": \t" + value + "\n");
		});

		constructedData.append("\n\n## Laundry\n\n");

		result.getAggregateMeterLaundry().forEach((key, value) -> {
			constructedData.append("* " + key + ": \t" + value + "\n");
		});

		constructedData.append("\n\n## A/C\n\n");

		result.getAggregateMeterAC().forEach((key, value) -> {
			constructedData.append("* " + key + ": \t" + value + "\n");
		});

		if(writeReport(constructedData.toString()) == -1)
			return -1;

		fileHandler.closeFD();
		return 0;
	}

	/**
	 * @func: writeAsTxt
	 * @desc: Constructs a string buffer of report data using the text format
	 * @return the return type of the function
	 */
	private int writeAsTxt() {
		StringBuffer constructedData = new StringBuffer();
		constructedData.append(result.getDescription())
			.append("\n=======================================\n")
			.append("avg consumption (watt-hours) over (a) Kitchen, (b) Laundry, (c) A/C\n\n")
			.append("Kitchen\n--------------\n");
		
		result.getAggregateMeterKitchen().forEach((key, value) -> {
			constructedData.append("* " + key + ": \t" + value + "\n");
		});

		constructedData.append("\n\nLaundry\n--------------\n");

		result.getAggregateMeterLaundry().forEach((key, value) -> {
			constructedData.append("* " + key + ": \t" + value + "\n");
		});

		constructedData.append("\n\nA/C\n--------------\n");

		result.getAggregateMeterAC().forEach((key, value) -> {
			constructedData.append("* " + key + ": \t" + value + "\n");
		});

		if(writeReport(constructedData.toString()) == -1)
			return -1;

		fileHandler.closeFD();
		return 0;
	}

	/**
	 * @Overriden func: reportResultInFile
	 * @desc: Reports the contents of an aggregate result to a file
	 * @param result an instance of a class implementing the IResult interface, containing the aggregate results
	 * @param filename a String with the path of the file where the report will be written
	 * @return 0 if the task completes successfully; a negative integer otherwise
	 */
	@Override
	public int reportResultInFile(IResult result, String filename) {
		this.result = result;
		this.fileHandler = new FileHandler(filename);

		/* Create a managed file descriptor */
		if(fileHandler.createWriterFD() == -1)
			return -1;

		if(exportType.equals("html"))
			return writeAsHtml();
		else if(exportType.equals("md"))
			return writeAsMd();
		else if(exportType.equals("txt"))
			return writeAsTxt();

		System.out.println("The export type is neither html nor md nor txt");
		return -1;
	}

	/* Accessor methods for class fields (FOR TESTING MOSTLY) */
	public String getExportType() {
		return this.exportType;
	}
	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public IResult getResult() {
		return this.result;
	}
	public void setResult(IResult result) {
		this.result = result;
	}

	public String getFilename() {
		return this.filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public FileHandler getFileHandler() {
		return this.fileHandler;
	}
	public void setFileHandler(FileHandler fileHandler) {
		this.fileHandler = fileHandler;
	}
}