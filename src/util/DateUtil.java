package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DateUtil {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String format(LocalDate date) { 
        return date.format(FORMATTER);
    }

    public static Optional<LocalDate> parse(String dateStr) {
        return Optional.of(LocalDate.parse(dateStr,FORMATTER));
    }

    public static boolean isBeforeToday(LocalDate date) { 
        return date.isBefore(LocalDate.now()); 
    }

    public static boolean isAfterToday(LocalDate date) { 
        return date.isAfter(LocalDate.now());
    }

    public static boolean isBefore(LocalDate date, LocalDate reference) { 
        return date.isBefore(reference); 
    }

    public static boolean isSameMonth(LocalDate date, int year, int month) { 
        return date.getYear() == year && date.getMonthValue() == month;
    }

    public static boolean isSameYear(LocalDate date, int year) { 
        return date.getYear() == year; 
    }
}
