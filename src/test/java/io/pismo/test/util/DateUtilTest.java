package io.pismo.test.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.Test;

class DateUtilTest {

	@Test
	void formatTest() {
		LocalDateTime dateTime = LocalDateTime.of(2020, Month.OCTOBER, 24, 18, 58);
		assertEquals("24/10/2020 18:58", DateUtil.formatDate(dateTime));
	}

}
