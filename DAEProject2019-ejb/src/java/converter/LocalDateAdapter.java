/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author admin
 */
public class LocalDateAdapter {

    public static LocalDate unmarshal(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
    }

    public static Date marshal(LocalDate localDate){
        return Date.from(
            LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth())
            .atStartOfDay(ZoneId.of( "Africa/Tunis" )).toInstant()
        );
    }
}
