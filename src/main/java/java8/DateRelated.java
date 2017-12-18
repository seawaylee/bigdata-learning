package java8;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author NikoBelic
 * @create 2017/10/16 19:50
 */
public class DateRelated
{
    public static void main(String[] args)
    {
        // JDK1.7
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        // LocalDate
        // JDK1.8
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        System.out.println(LocalDate.of(2011, 1, 1));
        System.out.println(LocalDate.parse("2016-02-15"));
        System.out.println(LocalDate.now().plusDays(1));
        System.out.println(LocalDate.now().minus(1, ChronoUnit.MONTHS));

        // LocalTime

        // LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        //2017-12-10 10:59:59
        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    }
}
