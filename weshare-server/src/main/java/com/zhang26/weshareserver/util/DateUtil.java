package com.zhang26.weshareserver.util;

public class DateUtil {
    static public java.sql.Date utilDateToSQLDate(java.util.Date utilDate){
        return new java.sql.Date(utilDate.getTime());
    };
    static public java.util.Date SQLDateToUtilDate(java.sql.Date sqlDate){
        return new java.util.Date(sqlDate.getTime());
    };
}
