package com.shu.tongzhanbu.component.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;



/**
 * @author DS
 * @date 2022/5/4 0:35
 * @description
 */
@Slf4j
public class CommonUtils {
    public static String GetYearMonth(String date){
        if(date!=null){
            Date date1 = stringToDate(date);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            return sdf.format(date1).replace('-','.');
        }else {
            return null;
        }


    }

    public static int getAgeByBirth(Date birthday) {
        // 当前时间
        Calendar curr = Calendar.getInstance();
        // 生日
        Calendar born = Calendar.getInstance();
        born.setTime(birthday);
        // 年龄 = 当前年 - 出生年
        int age = curr.get(Calendar.YEAR) - born.get(Calendar.YEAR);
        if (age <= 0) {
            return 0;
        }
        // 如果当前月份小于出生月份: age-1
        // 如果当前月份等于出生月份, 且当前日小于出生日: age-1
        int currMonth = curr.get(Calendar.MONTH);
        int currDay = curr.get(Calendar.DAY_OF_MONTH);
        int bornMonth = born.get(Calendar.MONTH);
        int bornDay = born.get(Calendar.DAY_OF_MONTH);
        if ((currMonth < bornMonth) || (currMonth == bornMonth && currDay <= bornDay)) {
            age--;
        }
        return age < 0 ? 0 : age;
    }

    /**
     * String 转 date
     *
     * @param string
     * @return
     */
    public static Date stringToDate(String string) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
        try {
            return sdf.parse(string);
        } catch (ParseException e) {
            return new Date();
        }
    }


    /**
     * 返回过去n天的时间
     *
     * @param n
     * @return
     */
    public static String lastNdate(int n) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -n);
        Date d = c.getTime();
        String day = format.format(d);
        return day;
    }

    public static String lastNds(int n) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -n);
        Date d = c.getTime();
        String day = format.format(d);
        return day;
    }

    /**
     * 返回过去或者未来n分钟的时间
     *
     * @param n
     * @return
     */
    public static String aroundMinute(int n, Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, n);
        Date d = c.getTime();
        String time = format.format(d);
        return time;
    }

    public static String aroundHour(int n, Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, n);
        Date d = c.getTime();
        String time = format.format(d);
        return time;
    }

    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    // 获得某天最小时间 2020-02-17 00:00:00
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }


     public static void main(String[] args) {
         System.out.println(stringToDate("1979.09"));
    }

}
