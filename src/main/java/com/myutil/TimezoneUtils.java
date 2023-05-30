package com.myutil;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.http.client.utils.DateUtils;

import java.time.*;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class TimezoneUtils {

	/**
	 * 0时区偏移量
	 */
	private static final String ZERO_OFFSET = "+00:00";

	/**
	 * @Description 时区转化
	 * 时区取浏览器传入时区偏移量，如果source为null，返回null
	 * 将时间偏移量(浮点型)转化为秒(整形)
	 * @Method convert
	 * @param source
	 * @return {@link Date}
	 * @Exception
	 * @Date 2020/11/11 11:30
	 */
	public static Date convert(Date source){
		if(source == null){ return null;}
		try {
			Calendar calendar = Calendar.getInstance();  
			calendar.setTime(source);  
			//String timezone = LoginInfoHolder.timezone();
			String timezone="";
			if(!StringUtils.isBlank(timezone)){
				// 将时间偏移量(浮点型)转化为秒(整形)
				Double timezoneInt = (Double.valueOf(timezone) *100) * 36;
				calendar.add(Calendar.SECOND, timezoneInt.intValue());
				return calendar.getTime();
			}
		} catch (NumberFormatException e) {
			log.error("timeZone convert exception source :{},reason:{}",source,e);
		}
		return source;
	}


	/**
	 * @Description 时区转化
	 * 时区取浏览器传入时区偏移量，如果source为null，返回null
	 * 将时间偏移量(浮点型)转化为秒(整形)
	 * @Method convert
	 * @param source
	 * @return {@link String}
	 * @Exception
	 * @Date 2020/11/11 11:30
	 */
	public static String convertString(Date source){
		if(source == null){ return null;}
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(source);
			//String timezone = LoginInfoHolder.timezone();
			String timezone="";
			if(!StringUtils.isBlank(timezone)){
				// 将时间偏移量(浮点型)转化为秒(整形)
				Double timezoneInt = (Double.valueOf(timezone) *100) * 36;
				calendar.add(Calendar.SECOND, timezoneInt.intValue());
				//return DateUtils.format(calendar.getTime(),"yyyy-MM-dd");
				return DateUtils.formatDate(calendar.getTime(),"yyyy-MM-dd");
			}
		} catch (NumberFormatException e) {
			log.error("timeZone convert exception source :{},reason:{}",source,e);
		}
		return null;
	}
	/**
	 * 转换时区 - 将时间偏移量(浮点型)转化为秒(整形)
	 * @param source 被转换时间
	 * @param timezoneOffset 时区偏差
	 * @return
	 */
	public static Date convert(Date source, Double timezoneOffset){
		if(source == null) {return null;}
		if(timezoneOffset == null) {return source;}
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(source);  
		Double d = timezoneOffset * 100 * 36;
		calendar.add(Calendar.SECOND, d.intValue());
		return calendar.getTime(); 
	}

	/**
	 * 转换时区 - 将时间偏移量(浮点型)转化为秒(整形)
	 * @param source 被转换时间
	 * @param timezone 时区
	 * @return
	 */
	public static Date convert(Date source, String timezone){
		if(source == null){ return null;}
		if(StringUtils.isEmpty(timezone)){
			timezone = "0";
		}
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(source);
			// 将时间偏移量(浮点型)转化为秒(整形)
			Double timezoneInt = (Double.valueOf(timezone) *100) * 36;
			calendar.add(Calendar.SECOND, timezoneInt.intValue());
			return calendar.getTime();
		} catch (NumberFormatException e) {
			log.error("timeZone convert exception source :{},reason:{}",source,e);
		}
		return source;
	}


	/**
	 * Date转换为LocalDateTime
	 * @param date
	 */
	public static LocalDateTime date2LocalDateTime(Date date){
		//An instantaneous point on the time-line.(时间线上的一个瞬时点。)
		Instant instant = date.toInstant();
		//A time-zone ID, such as {@code Europe/Paris}.(时区)
		ZoneId zoneId = ZoneId.of("GMT");
		return instant.atZone(zoneId).toLocalDateTime();
	}

	/**
	 * LocalDateTime转换为Date
	 * @param localDateTime
	 */
	public static Date localDateTime2Date( LocalDateTime localDateTime){
		ZoneId zoneId = ZoneId.of("GMT");
		//Combines this date-time with a time-zone to create a  ZonedDateTime.
		ZonedDateTime zdt = localDateTime.atZone(zoneId);
		return Date.from(zdt.toInstant());
	}

	/**
	 * @Description 根据传入的时区偏移量Double类型转换时区偏移量为‘HH：mm’格式
	 * 默认返回时区 +00:00
	 * @Method convertTimeZoneOffset
	 * @Author hyzhang
	 * @param timeZoneOffset
	 * @return {@link String}
	 * @Exception NumberFormatException
	 * @Date 2020/11/25 11:10
	 */
	public static String convertTimeZoneOffset(String timeZoneOffset){
		if(StringUtils.isBlank(timeZoneOffset)) {
			return ZERO_OFFSET;
		}
		try {
			Double aDouble = Double.valueOf(timeZoneOffset) * 3600 * 1000;
			String result = DurationFormatUtils.formatDuration(Math.abs(aDouble.longValue()), "HH:mm", Boolean.TRUE);
			if (StringUtils.containsAny(timeZoneOffset, "-")) {
				result = "-" + result;
			} else {
				result = "+" + result;
			}
			return result;
		} catch (NumberFormatException n){
			return ZERO_OFFSET;
		}
	}

	/**
	 * @Description 将LocalDateTime.now()根据前端传入转时区偏移量转换后再转为Date类型返回，默认的返回0时区
	 * @Method getDateTimeOffSetFromLocalDateTimeNow
	 * @Author hyzhang
	 * @return {@link Date}
	 * @Exception
	 * @Date 2020/11/25 14:22
	 */
	public static LocalDateTime getDateOffSetFromLocalDateTimeNow(){
		return getDateOffSetFromLocalDateTimeNow(null);
	}

	/**
	 * @Description 将LocalDateTime.now()根据传入转时区偏移量转换后再转为Date类型返回，默认返回0时区
	 *  -如果传入的offset不为null,则使用offset转换否则使用 LoginInfoHolder.timezone()进行时区偏移量计算
	 * @Method getDateOffSetFromLocalDateTimeNow
	 * @Author hyzhang
	 * @param offset '（+/-)HH:mm'
	 * @return {@link Date}
	 * @Exception
	 * @Date 2020/11/25 15:19
	 */
	public static LocalDateTime getDateOffSetFromLocalDateTimeNow(String offset){
		//String timezone = LoginInfoHolder.timezone();
		String timezone="";
		offset = StringUtils.isBlank(offset) ? TimezoneUtils.convertTimeZoneOffset(timezone) : offset;
		LocalDateTime localDateTimeNow = LocalDateTime.now(ZoneId.of(ZERO_OFFSET));
		return convertLocalDateTimeOffSet(localDateTimeNow,offset);
	}

	/**
	 * @Description 将将LocalDateTime 0时区 根据前端传入转时区偏移量转换后再转为Date类型返回，默认的返回0时区
	 * @Method convertLocalDateTimeOffSet
	 * @Author hyzhang
	 * @return {@link Date}
	 * @Exception
	 * @Date 2020/11/25 14:22
	 */
	public static LocalDateTime convertLocalDateTimeOffSet(LocalDateTime localDateTime) {
		return convertLocalDateTimeOffSet(localDateTime,null);
	}

	/**
	 * @Description 将LocalDateTime 0时区 根据传入转时区偏移量转换后返回，默认返回0时区
	 *  -如果传入的offset不为null,则使用offset转换否则使用 LoginInfoHolder.timezone()进行时区偏移量计算
	 * @Method convertLocalDateTimeOffSet
	 * @Author hyzhang
	 * @param offset '（+/-)HH:mm'
	 * @param localDateTime
	 * @return {@link Date}
	 * @Exception
	 * @Date 2020/11/25 15:19
	 */
	public static LocalDateTime convertLocalDateTimeOffSet(LocalDateTime localDateTime,String offset)  {
		//String timezone = LoginInfoHolder.timezone();
		String timezone="";
		offset = StringUtils.isBlank(offset) ? TimezoneUtils.convertTimeZoneOffset(timezone) : offset;
		// 先转0时区，然后时间偏移运算
		return convertLocalDateTime2UTCOffset(localDateTime).atOffset(ZoneOffset.UTC).withOffsetSameInstant(ZoneOffset.of(offset)).toLocalDateTime();
	}

	/**
	 * @Description 将传入的LocalDateTime初始化为0时区时间戳并返回
	 * 支撑部分orbit数据库中的时间回显，对服务器默认在不同时区设置做兼容
	 * @Method convertLocalDateTimeOffSet
	 * @Author hyzhang
	 * @param localDateTime
	 * @return {@link Date}
	 * @Exception
	 * @Date 2020/11/25 15:19
	 */
	public static LocalDateTime convertLocalDateTime2UTCOffset(LocalDateTime localDateTime)  {
		return localDateTime.atOffset(ZoneOffset.UTC).withOffsetSameInstant(ZoneOffset.of(ZERO_OFFSET)).toLocalDateTime();
	}
}
