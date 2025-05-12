package com.andrewbycode.cookdocs.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

    public static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    public static final String DD_MM_YYYY_2 = "ddMMyyyy";
    public static final String FORWARD_SLASH = "/";

    public static boolean isNullOrBlank(String str){
        if(str == null){
            return true;
        }else {
            str = str.trim();
        }
        return str.isBlank();
    }

    public static String dateToString(Date date, String... format){
        if(date == null){
            return null;
        }
        return new SimpleDateFormat(format.length > 0 ? format[0]: DD_MM_YYYY_HH_MM_SS).format(date);
    }
}
