package util;

import java.time.LocalDate;

public class Validator {

    public static boolean isNonBlank(String value) { 
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isPositive(double value) { 
        if(value > 0){
            return true;
        }
        return false;
    }

    public static boolean isInRange(double value, double min, double max) {
        if(value >= min && value <=max){
            return true;
        }
        return false; 
    }

    public static boolean isValidDateRange(LocalDate debut, LocalDate fin) {
        return debut != null
            && fin != null
            && !fin.isBefore(debut);
    }
}
