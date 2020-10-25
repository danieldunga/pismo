package io.pismo.test.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

	public static LocalDateTime getDate() {
		return LocalDateTime.now();
	}
	
	public static String formatDate(LocalDateTime dateTime) {
		String dateFormat = null;
		if (dateTime != null) {
			DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			dateFormat = dateTime.format(newFormat);
		}
		return dateFormat;
	}
}
