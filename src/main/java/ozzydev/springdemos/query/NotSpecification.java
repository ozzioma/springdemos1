package ozzydev.springdemos.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotSpecification<T> extends ComposableSpecification<T>
{

    private JpaQueryResolver<T> queryResolver;
    private Set<JpaQuerySpecification<T>> specificationSet;

    public NotSpecification(JpaQuerySpecification<T> first)
    {
        specificationSet = new HashSet<>();
        queryResolver = new JpaQueryResolver<>();
        specificationSet.add(first);
    }

    public NotSpecification(QueryFilter... filters)
    {
        specificationSet = new HashSet<>();
        queryResolver = new JpaQueryResolver<>();
        for (var filter : filters)
        {
            specificationSet.add(queryResolver.createSpecification(filter));
        }
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb)
    {
        List<Predicate> predicates = new ArrayList<>();
        for (var specification : specificationSet)
        {
            var predicate = specification.toPredicate(root, query, cb);
            predicates.add(predicate);
        }

        return cb.and(predicates.toArray(new Predicate[0])).not();
    }

}


