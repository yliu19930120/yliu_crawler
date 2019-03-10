package com.yliu.crawler.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	/**
	 * 字符串转时间
	 * @param str
	 * @param format
	 * @return
	 */
	public static Date strToDate(String str,String format){
	       SimpleDateFormat sdf = new SimpleDateFormat(format);
	       try {
			return sdf.parse(str);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
	   return null;
	}
	
	/**
	 * 时间转字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateToStr(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 日期对齐到天
	 * @param date
	 * @return
	 */
	public static Date aligningDateToDay(Date date){
		return strToDate(dateToStr(date,"yyyyMMdd"), "yyyyMMdd");
	}
}
