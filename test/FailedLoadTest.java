package test;

import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import mainengine.IMainEngine;
import mainengine.MainEngineFactory;
import datamodel.MeasurementRecord;

public class FailedLoadTest {
	private static MainEngineFactory factory = new MainEngineFactory();
	private static IMainEngine mainEngine = factory.createMainEngine("MainEngine");
	private static ArrayList<MeasurementRecord> objCollection;
	
	private static String inputFile;
	private static String delimeter;
	private static boolean hasHeaderLine;
	private static int numFields;
	
	@Test
	public void readWrongInputFileTest() {
		inputFile = "^__randomfilethatisinvalid^";
		delimeter = "\t";
		hasHeaderLine = false;
		numFields = 9;
		objCollection = new ArrayList<MeasurementRecord>();
		
		int wrongOutput = mainEngine.loadData(inputFile, delimeter, hasHeaderLine, numFields, objCollection);
		assertEquals(wrongOutput, -1);
	}
	
	@Test
	public void readWrongDelimeterTest() {
		inputFile = "./Resources/TestInput/household_preview.txt";
		delimeter = "randomdelimeter";
		hasHeaderLine = true;
		numFields = 9;
		objCollection = new ArrayList<MeasurementRecord>();
		
		int wrongOutput = mainEngine.loadData(inputFile, delimeter, hasHeaderLine, numFields, objCollection);
		assertEquals(wrongOutput, -1);
	}
	
	@Test
	public void readWrongHeaderLineTest() {
		inputFile = "./Resources/TestInput/household_preview.txt";
		delimeter = ";";
		hasHeaderLine = false;
		numFields = 9;
		objCollection = new ArrayList<MeasurementRecord>();
		
		int wrongOutput = mainEngine.loadData(inputFile, delimeter, hasHeaderLine, numFields, objCollection);
		assertEquals(wrongOutput, -1);
		
		inputFile = "./Resources/TestInput/2007_sample.tsv";
		delimeter = "\t";
		hasHeaderLine = true;
		wrongOutput = mainEngine.loadData(inputFile, delimeter, hasHeaderLine, numFields, objCollection);
		assertEquals(wrongOutput, -1);
	}
	
	@Test
	public void executeWithWrongNumFieldsTest() {
		inputFile = "./Resources/TestInput/household_preview.txt";
		delimeter = ";";
		hasHeaderLine = true;
		numFields = -5;
		objCollection = new ArrayList<MeasurementRecord>();
		
		int wrongOutput = mainEngine.loadData(inputFile, delimeter, hasHeaderLine, numFields, objCollection);
		assertEquals(wrongOutput, -1);
	}
	
	@Test
	public void executeWithNullCollection () {
		inputFile = "./Resources/TestInput/household_preview.txt";
		delimeter = ";";
		hasHeaderLine = true;
		numFields = 9;
		objCollection = null;
		
		int wrongOutput = mainEngine.loadData(inputFile, delimeter, hasHeaderLine, numFields, objCollection);
		assertEquals(wrongOutput, -1);
	}
}

