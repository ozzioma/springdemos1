package ozzydev.springdemos.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrSpecification<T> extends ComposableSpecification<T>
{

    private JpaQueryResolver<T> queryResolver;
    private Set<JpaQuerySpecification<T>> specificationSet;

    public OrSpecification(QueryFilter... filters)
    {
        specificationSet = new HashSet<>();
        queryResolver = new JpaQueryResolver<>();
        for (var filter : filters)
        {
            specificationSet.add(queryResolver.createSpecification(filter));
        }
    }

    public OrSpecification(JpaQuerySpecification<T> first, JpaQuerySpecification<T> second)
    {
        specificationSet = new HashSet<>();
        queryResolver = new JpaQueryResolver<>();
        specificationSet.addAll(List.of(first, second));
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
        return cb.or(predicates.toArray(new Predicate[0]));
    }

}
