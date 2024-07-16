package com.unla.eventos.helpers;

import java.time.LocalDateTime;

public class FunctionsHelper {
	public static String formatLocalDateToARGTime(LocalDateTime date) {
		String month = String.valueOf(date.getMonthValue());
		if(date.getMonthValue() < 10) month = "0" + month;
		
		String hour = String.valueOf(date.getHour());
		if(date.getHour() < 10) hour = "0" + hour;
		
		String minute = String.valueOf(date.getMinute());
		if(date.getMinute() < 10) minute = "0" + minute;
		
    	return date.getDayOfMonth() + "/" + month + "/" + date.getYear() + " " + hour + ":" + minute;
    }
}
