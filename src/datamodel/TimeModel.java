package datamodel;

/**
 * @class TimeModel
 * @brief A custom model for describing time
 */
public class TimeModel {
    private String hour;
    private String minute;
    private String second;

    public String getHour() {
        return this.hour;
    }
    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return this.minute;
    }
    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return this.second;
    }
    public void setSecond(String second) {
        this.second = second;
    }
}