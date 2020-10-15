package datamodel;

/**
 * @class DateModel
 * @desc A custom model for describing dates
 */
public class DateModel {
    private String day;
    private String month;
    private String year;

    public String getDay() {
        return this.day;
    }
    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return this.month;
    }
    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return this.year;
    }
    public void setYear(String year) {
        this.year = year;
    }
}