package ozzydev.springdemos.query;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.Specification;
import ozzydev.springdemos.models.mysql.DemoCustomer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


@FunctionalInterface
public interface JpaQuerySpecification<T> extends Serializable
{

    default Class<T> getType()
    {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>) type.getActualTypeArguments()[0];
    }

    Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) throws UnsupportedOperationException;

}


//class SpecDemo
//{
//
//    void test1()
//    {
//        JpaQuerySpecification<DemoCustomer> spec1 = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(DemoCustomer.Fields.firstName), "%De%");
//        JpaQuerySpecification<DemoCustomer> spec2 = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(DemoCustomer.Fields.firstName), "%De%");
//        JpaQuerySpecification<DemoCustomer> spec3 = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(DemoCustomer.Fields.firstName), "%De%");
//        JpaQuerySpecification<DemoCustomer> spec4 = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(DemoCustomer.Fields.firstName), "%De%");
//        JpaQuerySpecification<DemoCustomer> spec5 = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(DemoCustomer.Fields.firstName), "%De%");
//
//        //var specs=nameLike.an
//    }
//}