package ozzydev.springdemos.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ozzydev.springdemos.common.Tuple2;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ValueToPathConverter
{

    final static Logger logger = LoggerFactory.getLogger(ValueToPathConverter.class);

    public static Tuple2<Path<LocalDate>, LocalDate> getLocalDateBindInfo(Root root, QueryFilter input)
    {

        final var entityFieldName = input.getField();
        final var firstValue = input.getValues().size() > 0 ? input.getValues().get(0) : null;
        final var secondValue = input.getValues().size() > 1 ? input.getValues().get(1) : null;

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE.withResolverStyle(ResolverStyle.SMART);

        var path = root.<LocalDate>get(entityFieldName);
        var bindValue = LocalDate.parse(firstValue.toString(), formatter);

        Tuple2<Path<LocalDate>, LocalDate> bindingInfo = new Tuple2<>(path, bindValue);

        return bindingInfo;
    }

    public static Tuple2<Path<LocalDateTime>, LocalDateTime> getLocalDateTimeBindInfo(Root root, QueryFilter input)
    {

        final var entityFieldName = input.getField();
        final var firstValue = input.getValues().size() > 0 ? input.getValues().get(0) : null;

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withResolverStyle(ResolverStyle.SMART);

        var path = root.<LocalDateTime>get(entityFieldName);
        var bindValue = LocalDateTime.parse(firstValue.toString(), formatter);

        Tuple2<Path<LocalDateTime>, LocalDateTime> bindingInfo = new Tuple2<>(path, bindValue);

        return bindingInfo;
    }

    public static Tuple2<Path<LocalTime>, LocalTime> getLocalTimeBindInfo(Root root, QueryFilter input)
    {

        final var entityFieldName = input.getField();
        final var firstValue = input.getValues().size() > 0 ? input.getValues().get(0) : null;

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME.withResolverStyle(ResolverStyle.SMART);

        var path = root.<LocalTime>get(entityFieldName);
        var bindValue = LocalTime.parse(firstValue.toString(), formatter);

        Tuple2<Path<LocalTime>, LocalTime> bindingInfo = new Tuple2<>(path, bindValue);

        return bindingInfo;
    }


    public static Tuple2<Path<ZonedDateTime>, ZonedDateTime> getZonedDateTimeBindInfo(Root root, QueryFilter input)
    {

        final var entityFieldName = input.getField();
        final var firstValue = input.getValues().size() > 0 ? input.getValues().get(0) : null;

        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME.withResolverStyle(ResolverStyle.SMART);

        var path = root.<ZonedDateTime>get(entityFieldName);
        var bindValue = ZonedDateTime.parse(firstValue.toString(), formatter);

        Tuple2<Path<ZonedDateTime>, ZonedDateTime> bindingInfo = new Tuple2<>(path, bindValue);

        return bindingInfo;
    }

    public static Tuple2<Path<OffsetDateTime>, OffsetDateTime> getOffsetDateTimeBindInfo(Root root, QueryFilter input)
    {

        final var entityFieldName = input.getField();
        final var firstValue = input.getValues().size() > 0 ? input.getValues().get(0) : null;

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withResolverStyle(ResolverStyle.SMART);

        var path = root.<OffsetDateTime>get(entityFieldName);
        var bindValue = OffsetDateTime.parse(firstValue.toString(), formatter);

        Tuple2<Path<OffsetDateTime>, OffsetDateTime> bindingInfo = new Tuple2<>(path, bindValue);

        return bindingInfo;
    }


    public static Tuple2<Path<OffsetTime>, OffsetTime> getOffsetTimeBindInfo(Root root, QueryFilter input)
    {

        final var entityFieldName = input.getField();
        final var firstValue = input.getValues().size() > 0 ? input.getValues().get(0) : null;

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_TIME.withResolverStyle(ResolverStyle.SMART);

        var path = root.<OffsetTime>get(entityFieldName);
        var bindValue = OffsetTime.parse(firstValue.toString(), formatter);

        Tuple2<Path<OffsetTime>, OffsetTime> bindingInfo = new Tuple2<>(path, bindValue);

        return bindingInfo;
    }


    public static Tuple2<Path<Instant>, Instant> getInstantBindInfo(Root root, QueryFilter input)
    {

        final var entityFieldName = input.getField();
        final var firstValue = input.getValues().size() > 0 ? input.getValues().get(0) : null;

        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT.withResolverStyle(ResolverStyle.SMART);

        var path = root.<Instant>get(entityFieldName);
        var bindValue = Instant.parse(firstValue.toString());

        Tuple2<Path<Instant>, Instant> bindingInfo = new Tuple2<>(path, bindValue);

        return bindingInfo;
    }


    public static Object castToEntityFieldType(Class fieldType, Object value)
    {
        if (fieldType.isAssignableFrom(Double.class))
        {
            return Double.valueOf(value.toString());
        }
        else if (fieldType.isAssignableFrom(Float.class))
        {
            return Float.valueOf(value.toString());
        }
        else if (fieldType.isAssignableFrom(Integer.class))
        {
            return Integer.valueOf(value.toString());
        }
        else if (fieldType.isAssignableFrom(Long.class))
        {
            //System.out.println("Long value found->" + value.toString());
            return Long.valueOf(value.toString());
        }
        else if (fieldType.isAssignableFrom(Short.class))
        {
            return Short.valueOf(value.toString());
        }
        else if (fieldType.isAssignableFrom(Byte.class))
        {
            return Byte.valueOf(value.toString());
        }
        else if (fieldType.isAssignableFrom(LocalDate.class))
        {
            //var localDate = (LocalDate) value;

            var strDate = "24/11/1983";
            var df1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            var strDate2 = "1983/11/24";
            var date1 = LocalDate.parse(strDate, df1);
            logger.info("date1 value->" + date1);
            logger.info("date1 string value->" + date1.toString());

            var date2 = LocalDate.parse(strDate2, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            logger.info("date2 value->" + date2);
            logger.info("date2 string value->" + date2.toString());


            logger.info("param value->" + value);
            logger.info("LocalDate value found->" + value.toString());
            logger.info("value type name->" + value.getClass().getTypeName());
            logger.info("value simple name->" + value.getClass().getSimpleName());

            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter format2 = new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .append(DateTimeFormatter.ofPattern("yyy-MMM-dd")).toFormatter();
            //DateTimeFormatter format3 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            var format4 = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
            DateTimeFormatter format5 = DateTimeFormatter.ISO_LOCAL_DATE
                    //.ofPattern("yyyy-MM-dd", Locale.US)
                    //.ofPattern("yyyy-MM-dd")
                    .withResolverStyle(ResolverStyle.SMART);

            logger.info("LocalDate formatted value 1->" + LocalDate.parse(value.toString(), format5));
            //logger.info("LocalDate formatted value 2->" + LocalDate.parse(value.toString(), formatter));

            logger.info("date parse successful");

            var dateValue = LocalDate.parse(value.toString(), format5);
            logger.info("final LocalDate parsed value->" + dateValue);

            //ZoneId defaultZoneId = ZoneId.systemDefault();
            //Date jpaDate = Date.from(dateValue.atStartOfDay(defaultZoneId).toInstant());

            return dateValue;
            //return jpaDate;
        }
        else if (fieldType.isAssignableFrom(LocalDateTime.class))
        {
            DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withResolverStyle(ResolverStyle.SMART);
            return LocalDateTime.parse(value.toString(), format);
        }
        else if (fieldType.isAssignableFrom(LocalTime.class))
        {
            DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_TIME.withResolverStyle(ResolverStyle.SMART);
            return LocalTime.parse(value.toString(), format);
        }
        else if (fieldType.isAssignableFrom(OffsetTime.class))
        {
            DateTimeFormatter format = DateTimeFormatter.ISO_OFFSET_TIME.withResolverStyle(ResolverStyle.SMART);
            return OffsetTime.parse(value.toString(), format);
        }
        else if (fieldType.isAssignableFrom(OffsetDateTime.class))
        {
            DateTimeFormatter format = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withResolverStyle(ResolverStyle.SMART);
            return OffsetDateTime.parse(value.toString(), format);
        }
        else if (fieldType.isAssignableFrom(ZonedDateTime.class))
        {
            DateTimeFormatter format = DateTimeFormatter.ISO_ZONED_DATE_TIME.withResolverStyle(ResolverStyle.SMART);
            return ZonedDateTime.parse(value.toString(), format);
        }
        else if (fieldType.isAssignableFrom(Instant.class))
        {
            return Instant.parse(value.toString());
        }
        else if (fieldType.isAssignableFrom(Date.class))
        {
            return Date.parse(value.toString());
        }
        else if (Enum.class.isAssignableFrom(fieldType))
        {
            return Enum.valueOf(fieldType, value.toString());
        }
        return null;
    }

    public static Object castToEntityFieldType(Class fieldType, List<Object> values)
    {
        List<Object> lists = new ArrayList<>();
        for (Object value : values)
        {
            lists.add(castToEntityFieldType(fieldType, value));
        }
        return lists;
    }
}
