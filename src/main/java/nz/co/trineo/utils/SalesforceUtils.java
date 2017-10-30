package nz.co.trineo.utils;

import java.lang.reflect.Field;
import java.util.Map;

public class SalesforceUtils {
	public static <T> String getPrefixFor(final Class<T> clazz) {
		try {
			final Field field = clazz.getDeclaredField("PREFIX");
			field.setAccessible(true);
			final String value = (String) field.get(null);
			return value;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> Map<String, String> getFieldMapFor(final Class<T> clazz) {
		try {
			final Field field = clazz.getDeclaredField("fieldToPageField");
			field.setAccessible(true);
			final Map<String, String> value = (Map<String, String>) field.get(null);
			return value;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
