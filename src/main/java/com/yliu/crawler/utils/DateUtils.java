package com.yliu.crawler.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	
	public static Date strToDate(String str,String format){
	       SimpleDateFormat sdf = new SimpleDateFormat(format);
	       try {
			return sdf.parse(str);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
	   return null;
	}
	
	public static String dateToStr(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}
