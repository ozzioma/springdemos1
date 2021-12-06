package ozzydev.springdemos.query.predicate;

import ozzydev.springdemos.query.QueryFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@FunctionalInterface
public interface PredicateResolver
{
    <T> Predicate resolve(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, QueryFilter input);
}
