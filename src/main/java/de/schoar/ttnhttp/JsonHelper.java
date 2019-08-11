package de.schoar.ttnhttp;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonHelper {

	public static String toText(JsonNode jn, String find) {
		if (jn == null || find == null || find.isEmpty()) {
			return null;
		}
		jn = jn.findValue(find);
		if (jn == null || jn.isNull()) {
			return null;
		}
		if (jn.isTextual()) {
			return jn.asText();
		}
		return null;

	}

	public static String toString(JsonNode jn, String find) {
		if (jn == null || find == null || find.isEmpty()) {
			return null;
		}
		jn = jn.findValue(find);
		if (jn == null || jn.isNull()) {
			return null;
		}
		return jn.toString();
	}

	public static Integer toInteger(JsonNode jn, String find) {
		if (jn == null || find == null || find.isEmpty()) {
			return null;
		}
		jn = jn.findValue(find);
		if (jn == null || jn.isNull()) {
			return null;
		}
		if (jn.isInt()) {
			return jn.asInt();
		}
		return null;
	}

	public static Long toLong(JsonNode jn, String find) {
		if (jn == null || find == null || find.isEmpty()) {
			return null;
		}
		jn = jn.findValue(find);
		if (jn == null || jn.isNull()) {
			return null;
		}
		if (jn.isLong()) {
			return jn.asLong();
		}
		if (jn.isInt()) {
			return Long.valueOf(jn.asInt());
		}
		return null;
	}

	public static Double toDouble(JsonNode jn, String find) {
		if (jn == null || find == null || find.isEmpty()) {
			return null;
		}
		jn = jn.findValue(find);
		if (jn == null || jn.isNull()) {
			return null;
		}
		if (jn.isDouble()) {
			return jn.asDouble();
		}
		if (jn.isInt()) {
			return Double.valueOf(jn.asInt());
		}
		return null;
	}

	public static Boolean toBoolean(JsonNode jn, String find) {
		if (jn == null || find == null || find.isEmpty()) {
			return null;
		}
		jn = jn.findValue(find);
		if (jn == null || jn.isNull()) {
			return null;
		}
		if (jn.isBoolean()) {
			return jn.asBoolean();
		}
		return null;
	}

	public static Boolean toBooleanDefault(JsonNode jn, String find, Boolean def) {
		Boolean b = toBoolean(jn, find);
		if (b == null) {
			return def;
		}
		return b;
	}
}
