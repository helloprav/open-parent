package org.openframework.commons.ofds.utils;

import org.apache.commons.lang3.EnumUtils;
import org.openframework.commons.domain.exceptions.ApplicationValidationException;
import org.openframework.commons.enums.Gender;

public class EnumUtility {

	public Gender getGenderEnum(String gender) {
		Gender genderEnum = null;
		if (EnumUtils.isValidEnum(Gender.class, gender)) {
			genderEnum = Gender.valueOf(gender);
		}
		return genderEnum;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getEnumConstant(Class<?> clazz, String value) {
		if (clazz == null || value == null || value.isEmpty()) {
			return null;
		}
		Object obj = null;
		try {
			obj = Enum.valueOf((Class<Enum>) clazz, value);
		} catch (IllegalArgumentException ex) {
			throw new ApplicationValidationException("Invalid value ["+value+"] for " + clazz.getSimpleName());
		}
		return obj;
	}

	@SuppressWarnings({ "rawtypes" })
	public static String getEnumString(Class<?> clazz, Enum value) {
		if (value == null) {
			return null;
		} else return value.toString();
	}
}
