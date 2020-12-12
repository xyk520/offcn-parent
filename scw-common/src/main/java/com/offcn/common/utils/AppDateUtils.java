package com.offcn.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// 格式化输出时间日期
public class AppDateUtils {

    public static String getFormartTime(){
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dataFormat.format(new Date());
    }

    public static String getFormartTime(String pattern){
        DateFormat dataFormat = new SimpleDateFormat(pattern);
        return dataFormat.format(new Date());
    }

    public static String getFormartTime(Date date,String pattern){
        DateFormat dataFormat = new SimpleDateFormat(pattern);
        return dataFormat.format(date);
    }

}
