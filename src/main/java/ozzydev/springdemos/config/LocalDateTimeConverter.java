package ozzydev.springdemos.config;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Converter(autoApply = false)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp>
{

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime)
    {
        return (locDateTime == null ? null : Timestamp.from(locDateTime.toInstant(ZoneOffset.UTC)));
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp)
    {
        return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime());
    }

}


