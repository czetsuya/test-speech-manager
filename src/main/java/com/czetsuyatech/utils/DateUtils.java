package com.czetsuyatech.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
public final class DateUtils {

	public static final String SDF_STRING = "yyyy-MM-dd";

	public static Date parseDateWithPattern(String dateValue, String pattern) {
		if (dateValue == null || dateValue.trim().length() == 0) {
			return null;
		}
		Date result = null;

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		try {
			result = sdf.parse(dateValue);

		} catch (Exception e) {
			result = new Date(1);
		}

		return result;
	}

	public static String formatDateWithPattern(Date value, String pattern) {
		if (value == null) {
			return "";
		}
		String result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		try {
			result = sdf.format(value);

		} catch (Exception e) {
			result = "";
		}

		return result;
	}

	public static LocalDate toLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date fromLocalDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
