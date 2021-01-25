package test;

import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import mainengine.IMainEngine;
import mainengine.MainEngineFactory;
import datamodel.IResult;
import datamodel.MeasurementRecord;

public class FailedAggregationTest {
	private static MainEngineFactory factory = new MainEngineFactory();
	private static IMainEngine mainEngine = factory.createMainEngine("MainEngine");
	
	private static String inputFile = "./Resources/TestInput/household_preview.txt";
	private static String delimeter = ";";
	private static boolean hasHeaderLine = true;
	private static int numFields = 9;
	private static ArrayList<MeasurementRecord>objCollection = new ArrayList<MeasurementRecord>();
	
	private static String aggType;
	private static String aggFunction;
	private static String desc;
	
	int goodData = mainEngine.loadData(inputFile, delimeter, hasHeaderLine, numFields, objCollection);
	
	@Test
	public void readWrongInputMeasurements() {
		aggType = "month";
		aggFunction = "avg";
		desc = "Description";
		objCollection = null;
		IResult result = mainEngine.aggregateByTimeUnit(objCollection, aggType, aggFunction, desc);
		assertEquals(result, null);
	}
	
	@Test
	public void readWrongAggType() {
		aggType = "random";
		aggFunction = "avg";
		desc = "Desc";
		objCollection = new ArrayList<MeasurementRecord>();
		IResult result = mainEngine.aggregateByTimeUnit(objCollection, aggType, aggFunction, desc);
		assertEquals(result, null);
	}
	
	@Test
	public void readNullAggType() {
		aggType = null;
		aggFunction = "avg";
		desc = "Desc";
		objCollection = new ArrayList<MeasurementRecord>();
		IResult result = mainEngine.aggregateByTimeUnit(objCollection, aggType, aggFunction, desc);
		assertEquals(result, null);
	}
	
	@Test
	public void readNoDescription() {
		aggType = "month";
		aggFunction = "avg";
		desc = "";
		objCollection = new ArrayList<MeasurementRecord>();
		IResult result = mainEngine.aggregateByTimeUnit(objCollection, aggType, aggFunction, desc);
		assertEquals(result, null);
	}
	
	@Test
	public void readNullDescription() {
		aggType = "month";
		aggFunction = "avg";
		desc = null;
		objCollection = new ArrayList<MeasurementRecord>();
		IResult result = mainEngine.aggregateByTimeUnit(objCollection, aggType, aggFunction, desc);
		assertEquals(result, null);
	}
}
