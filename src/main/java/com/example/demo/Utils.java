package com.example.demo;

import java.util.Calendar;
import java.util.Date;

public class Utils {
    private static Date previousTime;

    public boolean checkIfResetNeed(){
        if(previousTime == null)
            return true;

        Date now = getZeroTimeDate(new Date());
        if(now.after(previousTime)){
            previousTime = now;
            return true;
        }
        return false;
    }

    public  static Date getZeroTimeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        return date;
    }


}
