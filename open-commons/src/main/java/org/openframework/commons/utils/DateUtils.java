package org.openframework.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {

	// Pattern for parsing a dateTime literal as specified in
	// "XML Schema Part 2: Datatypes Second Edition� specification
	// (http://www.w3.org/TR/xmlschema-2/#dateTime)
	//
	// Result of a match contains the following parts:
	// group(1): date part
	// group(2): time part incl. initial 'T' and fractional seconds
	// group(3): time part incl. initial 'T' but without fractional seconds
	// group(4): fractional seconds
	// group(5): timezone
	private static final Pattern PAT_DATE_TIME = Pattern
			.compile("(-?\\d{4}-\\d{2}-\\d{2})((T\\d{2}:\\d{2}:\\d{2})(?:\\.(\\d+))?)?([-+]\\d{2}:\\d{2}|Z)?$");

	private static final String FMT_DATE = "yyyy-MM-dd";
	private static final String FMT_DATE_TIME = FMT_DATE + "'T'HH:mm:ss";
	private static final String FMT_DATE_TIME_FRAC = FMT_DATE_TIME + ".SSS";
	private static final String FMT_TZ = "XXX";

	public static void main(String[] args) throws ParseException {

		String dfltPattern = "yyyy-MM-dd'T'HH:mm:ss";
		String dfltTimeZone = "UTC";
		List<String> dateStrList = new ArrayList<String>();
		dateStrList.add("1996-06-16T11:00:00");
		List<Date> result = ConvertDateTimesToDates(dateStrList, dfltTimeZone, dfltPattern);
		System.out.println(result);
	}

	/**
	 * Converts dateTime strings into {@code Date} instances. Expects dateTime
	 * literals that are compliant with <i>XML Schema Part 2: Datatypes Second
	 * Edition</i> specification (http://www.w3.org/TR/xmlschema-2/#dateTime). In
	 * case the passed dateTime strings have another format a conversion pattern can
	 * be passed which is used if a non-compliant dateTime string is encountered.
	 * 
	 * @param dateStrList  the dateTime strings
	 * 
	 * @param dfltTimeZone the timezone to be used if the dateTime string does not
	 *                     explicitly specify a timezone
	 * 
	 * @param dfltPattern  conversion pattern for non-compliant dateTime strings; if
	 *                     {@code null} is passed format
	 *                     {@code "yyyy-MM-dd'T'HH:mm:ss"} is used which causes a
	 *                     {@code ParseException} with non-compliant dateTime
	 *                     strings
	 * 
	 * @return the {@code Date} instances
	 * 
	 * @throws ParseException if a dateTime string could not be parsed
	 */
	public static List<Date> ConvertDateTimesToDates(List<String> dateStrList, String dfltTimeZone, String dfltPattern)
			throws ParseException {
		Matcher m = PAT_DATE_TIME.matcher("");
		Map<String, SimpleDateFormat> sdfMap = new HashMap<String, SimpleDateFormat>();
		String pat;
		SimpleDateFormat dfltFormat;
		TimeZone dfltTz;
		List<Date> ret = new ArrayList<Date>();

		if (dfltPattern == null) {
			dfltPattern = FMT_DATE_TIME;
		}

		dfltTz = getTimeZone(dfltTimeZone);
		dfltFormat = new SimpleDateFormat(dfltPattern);
		dfltFormat.setTimeZone(dfltTz);

		if (dateStrList != null) {
			for (String dateStr : dateStrList) {
				if (dateStr == null || dateStr.trim().length() == 0) {
					ret.add(null);
					continue;
				}

				m.reset(dateStr);
				if (!m.matches()) {
					Date date = dfltFormat.parse(dateStr);
					ret.add(date);
					continue;
				}

				String fracs = m.group(4);
				boolean roundUp = false;
				if (fracs != null) {
					// fractional seconds
					int fracsLen = fracs.length();

					// SimpleDateFormat converts only .SSS fractionals correctly.
					// Need to adjust the value to have exactly 3 digits.
					// Don't deal with rounding up here.
					switch (fracsLen) {
					case 1:
						fracs = fracs + "00";
						break;
					case 2:
						fracs = fracs + "0";
						break;
					case 3:
						break;
					default:
						roundUp = (fracs.charAt(3) - '0') >= 5;
						fracs = fracs.substring(0, 3);
						break;
					}

					if (fracsLen != 3) {
						dateStr = m.group(1) // date part
								+ m.group(3) // time part with initial 'T' and without fractional
								+ '.' // fractional seconds separator
								+ fracs // the adjusted fractional seconds
								+ ( // the optional timezone
								m.group(5) != null ? m.group(5) : "");
					}

					pat = FMT_DATE_TIME_FRAC;
				} else if (m.group(3) != null) {
					// date and time
					pat = FMT_DATE_TIME;
				} else {
					// only date
					pat = FMT_DATE;
				}

				if (m.group(5) != null) {
					pat += FMT_TZ;
				}

				SimpleDateFormat sdf = sdfMap.get(pat);
				if (sdf == null) {
					sdf = new SimpleDateFormat(pat);
					sdfMap.put(pat, sdf);
				}
				sdf.setTimeZone(dfltTz);

				Date date = sdf.parse(dateStr);
				if (roundUp) {
					date.setTime(date.getTime() + 1);
				}

				ret.add(date);
			}
		}

		return ret;
	}

	/**
	 * Returns the {@code TimeZone} object for the specified timezone ID. Does not
	 * default to the system default timezone if an unknown timezone id is passed.
	 * 
	 * @param timezone the ID of the wanted timezone
	 * 
	 * @return the {@code TimeZone} object
	 * 
	 * @throws java.time.zone.ZoneRulesException if an invalid timezone ID is passed
	 */
	private static TimeZone getTimeZone(String timezone) {
		TimeZone ret = null;

		if (timezone == null || timezone.trim().length() == 0) {
			ret = TimeZone.getDefault();
		} else {
			ZoneId zoneId = ZoneId.of(timezone);
			ret = TimeZone.getTimeZone(zoneId);
		}

		return ret;
	}

}
