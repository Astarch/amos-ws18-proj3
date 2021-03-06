package de.pretrendr.util;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Custom Date Serializer to serialize {@link DateTime} into JSON.
 * 
 * @author Tristan Schneider
 *
 */
public class CustomDateSerializer extends JsonSerializer<DateTime> {

	private final static DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

	@Override
	public void serialize(DateTime value, JsonGenerator gen, SerializerProvider arg2)
			throws IOException, JsonProcessingException {

		gen.writeString(FORMATTER.print(value));
	}
}