package com.jingdong.manager.utils;

import com.mysql.cj.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtils {
    public static String dateFormat(String time){
        if (time == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = Long.parseLong(time);
        Date date = new Date(lt);
        return sdf.format(date);
    }

    public static Long strForDateLong(String str) {
        if (StringUtils.isNullOrEmpty(str)) {
            return null;
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() * 1000;
    }

    public static String dateToStr(Date date){
        if (date == null) {
            return "暂无";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static void main(String[] args) {
//        System.out.println(dateFormat("1663829235"));
//        System.out.println(strForDateLong(""));
//        System.out.println(strForDateLong("2022-11-14"));
        System.out.println(dateToStr(new Date()));

//        String s = dateFormat(strForDateLong("").toString());
//        System.out.println(s);
    }
}
