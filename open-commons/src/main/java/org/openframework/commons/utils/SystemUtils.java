package org.openframework.commons.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

public class SystemUtils {

	public SystemUtils() {
		// TODO Auto-generated constructor stub
	}

	public static void printSystemProperties() {
		System.out.println("*****START*****");
		Properties properties =  System.getProperties();
		Map<Object, Object> prop = new TreeMap<>(properties);
		Set<Entry<Object, Object>> propSet = prop.entrySet();
		Iterator<Entry<Object, Object>> iterator = propSet.iterator();
		while(iterator.hasNext()) {
			Entry<Object, Object> nextObj = iterator.next();
			String key = nextObj.getKey().toString();
			String val = nextObj.getValue().toString();
			System.out.println(key + ":::" + val);
		}
		System.out.println("*****END*****");
	}
}
