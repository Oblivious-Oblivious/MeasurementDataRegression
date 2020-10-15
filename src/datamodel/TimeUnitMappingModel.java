package datamodel;

import java.util.HashMap;

/**
 * @class TimeUnitMappingModel
 * @desc A simple model that maps time units read from the input file, into actual names
 * @param seasons A HashMap that takes the coding [01-12] and maps it to [WINTER|SPRING|Summer|AUTUMN]
 * @param months A HashMap that takes the coding [01-12] and maps it to [JAN-DEC]
 * @param days A HashMap that takes the coding [01-07] and maps it to [MON-SUN]
 * @param periodOfDay A HashMap that takes the coding [00-23] and maps it to [NIGHT|EARLY MORNING|MORNING|AFTERNOON|EVENING]
 */
public class TimeUnitMappingModel {
    private HashMap<String, String> seasons = new HashMap<String, String>() {
        /* Suppresses warnings */
        private static final long serialVersionUID = 1L;

        {
        	/* Care for inputs that are either '01' or '1' and so on*/
        	put("1", "WINTER");
            put("2", "WINTER");
            put("3", "SPRING");
            put("4", "SPRING");
            put("5", "SPRING");
            put("6", "SUMMER");
            put("7", "SUMMER");
            put("8", "SUMMER");
            put("9", "AUTUMN");
            
            put("01", "WINTER");
            put("02", "WINTER");
            put("03", "SPRING");
            put("04", "SPRING");
            put("05", "SPRING");
            put("06", "SUMMER");
            put("07", "SUMMER");
            put("08", "SUMMER");
            put("09", "AUTUMN");
            put("10", "AUTUMN");
            put("11", "AUTUMN");
            put("12", "WINTER");
        }

    };

    private HashMap<String, String> months = new HashMap<String, String>() {
        /* Suppresses warnings */
        private static final long serialVersionUID = 1L;

        {
        	put("1", "JAN");
            put("2", "FEB");
            put("3", "MAR");
            put("4", "APR");
            put("5", "MAY");
            put("6", "JUN");
            put("7", "JUL");
            put("8", "AUG");
            put("9", "SEP");
            
            put("01", "JAN");
            put("02", "FEB");
            put("03", "MAR");
            put("04", "APR");
            put("05", "MAY");
            put("06", "JUN");
            put("07", "JUL");
            put("08", "AUG");
            put("09", "SEP");
            put("10", "OCT");
            put("11", "NOV");
            put("12", "DEC");
        }

    };

    private HashMap<String, String> days = new HashMap<String, String>() {
        /* Suppresses warnings */
        private static final long serialVersionUID = 1L;

        {
            put("01", "MON");
            put("02", "TUE");
            put("03", "WED");
            put("04", "THU");
            put("05", "FRI");
            put("06", "SAT");
            put("07", "SUN");
        }

    };
    private HashMap<String, String> periodOfDay = new HashMap<String, String>() {
        /* Suppresses warnings */
        private static final long serialVersionUID = 1L;

        {
            put("00", "NIGHT");
            put("01", "NIGHT");
            put("02", "NIGHT");
            put("02", "NIGHT");
            put("03", "NIGHT");
            put("04", "NIGHT");
            put("05", "EARLY MORNING");
            put("06", "EARLY MORNING");
            put("07", "EARLY MORNING");
            put("08", "EARLY MORNING");
            put("09", "MORNING");
            put("10", "MORNING");
            put("11", "MORNING");
            put("12", "MORNING");
            put("13", "AFTERNOON");
            put("14", "AFTERNOON");
            put("15", "AFTERNOON");
            put("16", "AFTERNOON");
            put("17", "EVENING");
            put("18", "EVENING");
            put("19", "EVENING");
            put("20", "EVENING");
            put("21", "NIGHT");
            put("22", "NIGHT");
            put("23", "NIGHT");
        }

    };

    public HashMap<String, String> getSeasons() {
        return this.seasons;
    }
    public HashMap<String, String> getMonths() {
        return this.months;
    }
    public HashMap<String, String> getDays() {
        return this.days;
    }
    public HashMap<String, String> getPeriodOfDay() {
        return this.periodOfDay;
    }
    /* These maps, give concrete information about what time means
        in terms of our program requirements, and can be extended
        easily for any new sort of time aggregation */
}