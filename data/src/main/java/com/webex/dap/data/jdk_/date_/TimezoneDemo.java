package com.webex.dap.data.jdk_.date_;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by harry on 8/6/18.
 */
public class TimezoneDemo {
    public static void main(String[] args) {
        System.out.println(TimeZone.getDefault());
        Date date = new Date(1515634393000L); // GMT => 2018-01-11 01:33:13  GMT+8 2018-01-11 09:33:13
        String dateStr = "2018-01-11 01:33:13";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            //字符串是GMT时间，parse转成Date时，要加上 默认的时区(本机时区)
            Date dateTmp = dateFormat.parse(dateStr);
            System.out.println("DateStr===>" + dateTmp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateStrTmp = dateFormat.format(date);
        System.out.println("Date===>" + dateStrTmp);
    }
}
