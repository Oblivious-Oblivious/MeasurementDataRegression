package test;

import java.util.ArrayList;
import java.io.File;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import mainengine.IMainEngine;
import mainengine.MainEngineFactory;
import datamodel.IResult;
import datamodel.MeasurementRecord;

public class FailedReportingTest {
	private static MainEngineFactory factory = new MainEngineFactory();
	private static IMainEngine mainEngine = factory.createMainEngine("MainEngine");
	
	private static String inputFile = "./Resources/TestInput/household_preview.txt";
	private static String delimeter = ";";
	private static boolean hasHeaderLine = true;
	private static int numFields = 9;
	private static ArrayList<MeasurementRecord>objCollection = new ArrayList<MeasurementRecord>();
	
	private static String aggType = "month";
	private static String aggFunction = "avg";
	private static String desc = "valid description";
	
	private static String exportType;
	private static String outputFilename;
	
	int goodData = mainEngine.loadData(inputFile, delimeter, hasHeaderLine, numFields, objCollection);
	IResult validResult = mainEngine.aggregateByTimeUnit(objCollection, aggType, aggFunction, desc);
	
	@Test
	public void readWrongExportType() {
		new File(System.getProperty("user.dir") + "\\Resources\\TestOutput\\2007TestOutput.md").delete();
		exportType = "random";
		outputFilename = "./Resources/TestOutput/2007TestOutput.md";
		int returnValue = mainEngine.reportResultInFile(validResult, exportType, outputFilename);
		assertEquals(returnValue, -1);
	}
	
	@Test
	public void readNullExportType() {
		new File(System.getProperty("user.dir") + "\\Resources\\TestOutput\\2007TestOutput.md").delete();
		exportType = null;
		outputFilename = "./Resources/TestOutput/2007TestOutput.md";
		int returnValue = mainEngine.reportResultInFile(validResult, exportType, outputFilename);
		assertEquals(returnValue, -1);
	}
	
	@Test
	public void readWrongOutputFilename() {
		new File(System.getProperty("user.dir") + "\\Resources\\TestOutput\\2007TestOutput.md").delete();
		exportType = "md";
		outputFilename = "^^^^^^^^^^^^^^^^^^^^^WRONG^^^^^^^^^^^^^";
		int returnValue = mainEngine.reportResultInFile(validResult, exportType, outputFilename);
		assertEquals(returnValue, -1);
	}
	
	@Test
	public void readNullOutputFilename() {
		new File(System.getProperty("user.dir") + "\\Resources\\TestOutput\\2007TestOutput.md").delete();
		exportType = "md";
		outputFilename = null;
		int returnValue = mainEngine.reportResultInFile(validResult, exportType, outputFilename);
		assertEquals(returnValue, -1);
	}
}