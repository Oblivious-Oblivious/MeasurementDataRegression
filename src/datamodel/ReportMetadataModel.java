package datamodel;

/**
 * @class: ReportMetadataModel
 * @desc: A model that holds report metadata that Reporter and History will use to form reports
 * @param description The report description
 * @param outputPath The path where the report is saved
 * @param exportType The type of file that was created (html, md, txt)
 */
public class ReportMetadataModel {
    private String description;
    private String outputPath;
    private String exportType;
    
    public ReportMetadataModel() {}
    
    /**
     * @func correctOutputPath
     * @desc Retrieves the full path of the file and corrects slashes according to the operating system used
     **/
    public void correctOutputPath() {
    	if(System.getProperty("os.name").contains("Windows"))
    		outputPath = outputPath.replace("/", "\\");

        outputPath = System.getProperty("user.dir") + "\\" + outputPath;
    }

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getOutputPath() {
        return this.outputPath;
    }
    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getExportType() {
        return this.exportType;
    }
    public void setExportType(String exportType) {
        this.exportType = exportType;
    }
}