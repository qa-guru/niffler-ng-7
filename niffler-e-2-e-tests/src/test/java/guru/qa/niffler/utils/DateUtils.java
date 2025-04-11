package guru.qa.niffler.utils;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@ParametersAreNonnullByDefault
public class DateUtils {

  @Nonnull
  public static String getDateAsString(Date date) {
    return getDateAsString(date, "dd MMM yy");
  }

  @Nonnull
  public static String getDateAsString(Date date, String stringFormat) {
    SimpleDateFormat sdf = new SimpleDateFormat(stringFormat);
    return sdf.format(date);
  }

  @Nonnull
  public static Date fromString(String dateAsString) {
    return fromString(dateAsString, "dd MMM yy");
  }

  @Nonnull
  public static Date fromString(String dateAsString, String stringFormat) {
    SimpleDateFormat sdf = new SimpleDateFormat(stringFormat);
    try {
      return sdf.parse(dateAsString);
    } catch (ParseException e) {
      throw new RuntimeException();
    }
  }

  @Nonnull
  public static Date addDaysToDate(Date date, int selector, int days) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(selector, days);
    return cal.getTime();
  }
}