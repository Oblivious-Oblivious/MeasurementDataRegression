package datamodel;

/**
 * @class MeasurementRecord
 * @brief A private model for injection that describes the data that will be recorded and aggregated
 */
public class MeasurementRecord {
    /**
     * date -> a DateModel object holding dates
     * time -> a TimeModel object holding times
     * doubles -> The rest of the 9 fields we read from the file
     * _delimiter_error -> a flag signaling whether the file has a different delimiter than the one we set
     */
    private DateModel date;
    private TimeModel time;
    private double sub_metering_1; /* Kitchen */
    private double sub_metering_2; /* Laundry */
    private double sub_metering_3; /* AC */

    private boolean _delimiter_error;

    public DateModel getDate() {
        return this.date;
    }
    public void setDate(DateModel date) {
        this.date = date;
    }

    public TimeModel getTime() {
        return this.time;
    }
    public void setTime(TimeModel time) {
        this.time = time;
    }

    public double getSub_metering_1() {
        return this.sub_metering_1;
    }
    public void setSub_metering_1(double sub_metering_1) {
        this.sub_metering_1 = sub_metering_1;
    }

    public double getSub_metering_2() {
        return this.sub_metering_2;
    }
    public void setSub_metering_2(double sub_metering_2) {
        this.sub_metering_2 = sub_metering_2;
    }

    public double getSub_metering_3() {
        return this.sub_metering_3;
    }
    public void setSub_metering_3(double sub_metering_3) {
        this.sub_metering_3 = sub_metering_3;
    }

    public boolean get__delimiter_error() {
        return this._delimiter_error;
    }
    public void set__delimiter_error(boolean _delimiter_error) {
        this._delimiter_error = _delimiter_error;
    }

    public void setGlobal_active_power(double global_active_power) {}

    public void setGlobal_reactive_power(double global_reactive_power) {}

    public void setVoltage(double voltage) {}

    public void setGlobal_intensity(double global_intensity) {}
}
