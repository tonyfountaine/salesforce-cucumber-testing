package nz.co.trineo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.function.Predicate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class ModelUtils {
	private static final ObjectMapper mapper = new ObjectMapper();

	public static final Predicate<Field> STATIC_PREDICATE = new Predicate<Field>() {
		@Override
		public boolean test(final Field f) {
			return (f.getModifiers() & Modifier.STATIC) != Modifier.STATIC;
		}
	};

	public static <T> String toJSON(final T model) throws JsonProcessingException {
		String asString = mapper.writeValueAsString(model);
		return asString;
	}

	public static <T> T fromJSON(final InputStream value, final Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		final T model = mapper.readValue(value, clazz);
		return model;
	}

	public static <T> T listFromJSON(final InputStream value, final Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		final CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
		final T model = mapper.readValue(value, listType);
		return model;
	}

	public static <T> T fromJSON(final String value, final Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		final T model = mapper.readValue(value, clazz);
		return model;
	}

	public static <T> T listFromJSON(final String value, final Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		final CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
		final T model = mapper.readValue(value, listType);
		return model;
	}
}
