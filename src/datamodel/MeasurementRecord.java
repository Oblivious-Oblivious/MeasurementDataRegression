package datamodel;

/**
 * @class MeasurementRecord
 * @desc: A private model for injection that describes the data that will be recorded and aggregated
 * @param date a DateModel object holding dates
 * @param time a TimeModel object holding times
 * @param doubles The rest of the 9 fields we read from the file
 * @param _delimeter_error a flag signaling whether the file has a different delimeter than the one we set
 */
public class MeasurementRecord {
    private DateModel date;
    private TimeModel time;
    private double global_active_power;
    private double global_reactive_power;
    private double voltage;
    private double global_intensity;
    private double sub_metering_1; /* Kitchen */
    private double sub_metering_2; /* Laundry */
    private double sub_metering_3; /* AC */

    private boolean _delimeter_error;

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

    public double getGlobal_active_power() {
        return this.global_active_power;
    }
    public void setGlobal_active_power(double global_active_power) {
        this.global_active_power = global_active_power;
    }

    public double getGlobal_reactive_power() {
        return this.global_reactive_power;
    }
    public void setGlobal_reactive_power(double global_reactive_power) {
        this.global_reactive_power = global_reactive_power;
    }

    public double getVoltage() {
        return this.voltage;
    }
    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public double getGlobal_intensity() {
        return this.global_intensity;
    }
    public void setGlobal_intensity(double global_intensity) {
        this.global_intensity = global_intensity;
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

    public boolean get__delimeter_error() {
        return this._delimeter_error;
    }
    public void set__delimeter_error(boolean _delimeter_error) {
        this._delimeter_error = _delimeter_error;
    }
}
