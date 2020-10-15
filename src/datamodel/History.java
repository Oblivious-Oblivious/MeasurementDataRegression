package datamodel;

import java.util.ArrayList;

/**
 * @class: History
 * @desc: Implements a history database model for saving aggregate metric reports 
 * @param reports An arraylist where ReportMetadataModels are saved
 */
public class History {
    private ArrayList<ReportMetadataModel> reports = new ArrayList<ReportMetadataModel>();
    private FileHandler handler = new FileHandler("___db.dbfile");

    /**
     * @Constructor
     */
    public History() {
        /* TODO MAKE HISTORY UPDATE WHEN I DELETE A REPORT OUTSIDE THE FILE */
    	
    	/* The custom database file reader */
        handler.createReaderFD();
        
        String data;
        
        /* The data list to use in the loop for regaining saved reports */
        ArrayList<String> dataList = new ArrayList<String>();
        
        /* Read all saved reports */
        while(true) {
			data = handler.readLineFromFile();
            if(data == null)
                /* Loaded the whole file */
				break;
			dataList.add(data);
        }

        /* For each item in the data list remap it into a ReportMetadataModel object and save in the reports arraylist */
        for(String item : dataList) {
        	String list[] = item.split(";");
        	String desc = list[0];
        	String export = list[1];
        	String output = list[2];
        	
        	ReportMetadataModel repModel = new ReportMetadataModel();
        	repModel.setDescription(desc);
        	repModel.setExportType(export);
        	repModel.setOutputPath(output);
        	
        	reports.add(repModel);
        }
    }

    /**
     * @func: saveReport
     * @desc: Saves the ReportMetadataModels into the arraylist of reports
     * @param metadata the object to save
     */
    public void saveReport(ReportMetadataModel metadata) {
        /* Metadata is already checked to be valid */
        reports.add(metadata);

        /* TODO IS BETTER TO USE YAML OR XML FOR SAVING DATA LIKE THIS */
        handler.appendToDB(metadata);
    }

    /**
     * @func: listReports
     * @desc: Iterates over the reports arraylist and prints each saved report (not the measurements)
     */
    /* TODO FIND FULL FILE PATH INSTEAD OF LOCAL FOR REPORT HISTORY */
    public void listReports() {
        int counter = 0;
        System.out.println("Available reports: " + getHistorySize() + "\n");
        for(ReportMetadataModel data : reports) { 		      
            counter++;
            System.out.println("Report: " + counter);
            System.out.println("\t" + data.getDescription());
            System.out.println("\tOutput path: " + data.getOutputPath());
            System.out.println("\tExport type: " + data.getExportType());
       }
    }

    /**
     * @func: getHistorySize
     * @desc: Finds the size of the history arraylist
     * @return the size
     */
    public int getHistorySize() {
        return reports.size();
    }
}