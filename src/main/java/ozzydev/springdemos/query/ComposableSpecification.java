package ozzydev.springdemos.query;

import org.springframework.core.GenericTypeResolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ComposableSpecification<T> implements JpaQuerySpecification<T>
{

    public ComposableSpecification<T> and(ComposableSpecification<T> specification)
    {
        return new AndSpecification<T>(this, specification);
    }

    public ComposableSpecification<T> or(ComposableSpecification<T> specification)
    {
        return new OrSpecification<T>(this, specification);
    }

    public ComposableSpecification<T> or(QueryFilter... filters)
    {
        var newSpecs = new OrSpecification<T>(filters);
        return new OrSpecification<T>(this, newSpecs);
    }

    public ComposableSpecification<T> and(QueryFilter... filters)
    {
        var newSpecs = new AndSpecification<T>(filters);
        return new AndSpecification<T>(this, newSpecs);
    }

    public ComposableSpecification<T> not()
    {
        return new NotSpecification<T>(this);
    }

    public ComposableSpecification<T> not(QueryFilter... filters)
    {
        return new NotSpecification<T>(filters);
    }

    public ComposableSpecification<T> not(ComposableSpecification<T> specification)
    {
        return new NotSpecification<T>(specification);
    }
}
