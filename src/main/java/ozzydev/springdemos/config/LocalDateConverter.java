package ozzydev.springdemos.config;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDate;

@Converter(autoApply = false)
public class LocalDateConverter implements AttributeConverter<LocalDate, Timestamp>
{

    @Override
    public Timestamp convertToDatabaseColumn(LocalDate localDate)
    {
        return (localDate == null ? null : Timestamp.valueOf(localDate.atStartOfDay()));
    }

    @Override
    public LocalDate convertToEntityAttribute(Timestamp sqlTimestamp)
    {
        return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime().toLocalDate());
    }

}
