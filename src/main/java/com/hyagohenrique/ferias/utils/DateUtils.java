package com.hyagohenrique.ferias.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    
    public static Date convertLocalDateToDate(LocalDate localDate) {
        if(localDate != null) {
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }
}