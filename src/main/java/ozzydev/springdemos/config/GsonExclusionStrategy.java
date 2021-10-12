package ozzydev.springdemos.config;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class GsonExclusionStrategy implements ExclusionStrategy
{

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes)
    {
        return fieldAttributes.getAnnotation(GsonIgnore.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz)
    {
        return clazz.getAnnotation(GsonIgnore.class) != null;
    }
}
