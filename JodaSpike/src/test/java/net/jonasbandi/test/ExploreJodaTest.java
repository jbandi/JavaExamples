package net.jonasbandi.test;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.hamcrest.core.IsEqual;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class ExploreJodaTest {

	@Test
	public void AddTime() {
		
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.DATE, 1);
		Date tomorrow = cal.getTime();
		
		LocalDateTime jtoday = new LocalDateTime();
		LocalDateTime jtomorrow = jtoday.plusDays(1);

		assertEquals(cal.get(Calendar.YEAR), jtomorrow.getYear());
		assertEquals(cal.get(Calendar.MONTH) + 1, jtomorrow.getMonthOfYear());
		assertEquals(cal.get(Calendar.DAY_OF_MONTH), jtomorrow.getDayOfMonth());
		
		assertThat(jtomorrow.toDate().toString(), is(tomorrow.toString()));
	}
	
	@Test
	public void DaysRemainingInMonth() {
		LocalDate today = new LocalDate();
		LocalDate nextMonth = today.plusMonths(1).withDayOfMonth(1);
		Days remaining = Days.daysBetween(today, nextMonth);
		
		assertThat(remaining.getDays(), is(16));
	}
	
	@Test
	public void Formatting() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("E, d MMM yyyy HH:mm:ss");
		DateTimeFormatter dfmt = fmt.withLocale(Locale.GERMAN);
		DateTimeFormatter ffmt = fmt.withLocale(Locale.FRENCH);
		
		LocalDateTime now = new LocalDateTime();
		System.out.println(now.toString(fmt));
		System.out.println(now.toString(dfmt));
		System.out.println(now.toString(ffmt));
	}

}
