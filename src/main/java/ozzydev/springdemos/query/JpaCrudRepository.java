package ozzydev.springdemos.query;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class JpaCrudRepository<T, ID> implements BaseJpaRepository<T, ID>
{

    //@PersistenceContext(unitName = "primary")
    //@PersistenceContext
    protected EntityManager entityManager;

    private ComposableSpecification<T> baseSpecification;
    private final Class<T> clazz;

    protected JpaCrudRepository(final Class<T> clazz)
    {
        this.clazz = clazz;
    }

    protected JpaCrudRepository(final Class<T> clazz, final EntityManager entityManager)
    {
        this.clazz = clazz;
        this.entityManager = entityManager;
    }


    //    protected Class<T> getClazz()
    //    {
    //        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
    //        return (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), type.getClass());
    //    }

    //    public Class<T> getType()
    //    {
    //        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
    //        return (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), type.getClass());
    //    }


    public static QueryFilter.QueryFilterBuilder col(String field)
    {
        return new QueryFilter.QueryFilterBuilder().field(field);
    }

    protected Class<T> getType()
    {
        return clazz;
    }


    public SingularAttribute<? super T, ?> getIdAttribute()
    {
        IdentifiableType<T> of = (IdentifiableType<T>) entityManager.getMetamodel().managedType(clazz);
        return of.getId(of.getIdType().getJavaType());
    }


    JpaQuerySpecification<T> getIdSelectPath()
    {
        JpaQuerySpecification<T> idSelectPath = (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get(getIdAttribute()));
        return idSelectPath;
    }


    @Override
    public List<T> findAll()
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getType());
        Root<T> root = criteriaQuery.from(getType());

        Predicate predicate;
        if (baseSpecification == null)
        {
            predicate = getIdSelectPath().toPredicate(root, criteriaQuery, criteriaBuilder);
            //criteriaQuery.where(predicate);
        }
        else
        {
            predicate = baseSpecification.toPredicate(root, criteriaQuery, criteriaBuilder);
            criteriaQuery.where(predicate);
        }

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<T> findAll(QueryFilter... filters)
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getType());
        Root<T> root = criteriaQuery.from(getType());

        List<Predicate> predicates = new ArrayList<>();
        List<ComposableSpecification<T>> specificationList = new ArrayList<>();

        for (var filter : filters)
        {
            AndSpecification<T> specification = new AndSpecification<T>(filter);
            specificationList.add(specification);

            // get predicate from specification
            Predicate predicate = specification.toPredicate(root, criteriaQuery, criteriaBuilder);
            predicates.add(predicate);

        }

        var firstSpec = specificationList.remove(0);
        for (var nextSpec : specificationList)
        {
            firstSpec = firstSpec.or(nextSpec);
        }
        baseSpecification = firstSpec;

        // set predicate and execute query
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<T> findAll(ComposableSpecification<T> specification)
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        // use specification.getType() to create a Root<T> instance
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getType());
        Root<T> root = criteriaQuery.from(getType());
        // get predicate from specification
        Predicate predicate = specification.toPredicate(root, criteriaQuery, criteriaBuilder);
        // set predicate and execute query
        criteriaQuery.where(predicate);

        baseSpecification = specification;

        return entityManager.createQuery(criteriaQuery).getResultList();
    }


    @Override
    public List<T> orFindAll(QueryFilter... filters)
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getType());
        Root<T> root = criteriaQuery.from(getType());

        //List<Predicate> predicates = new ArrayList<>();

        List<ComposableSpecification<T>> specificationList = new ArrayList<>();

        for (var filter : filters)
        {
            OrSpecification<T> specification = new OrSpecification<T>(filter);
            specificationList.add(specification);

            //Predicate predicate = specification.toPredicate(root, criteriaQuery, criteriaBuilder);
            //predicates.add(predicate);
        }

        var firstSpec = specificationList.remove(0);
        for (var nextSpec : specificationList)
        {
            firstSpec = firstSpec.or(nextSpec);
        }
        baseSpecification = firstSpec;

        Predicate predicate = baseSpecification.toPredicate(root, criteriaQuery, criteriaBuilder);
        //var predicate = criteriaBuilder.or(predicates.toArray(new Predicate[0]));

        criteriaQuery.where(predicate);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }



    public JpaCrudRepository<T, ID> where(QueryFilter... filters)
    {
        if (baseSpecification != null)
        {
            throw new UnsupportedOperationException("base where expression has been set before");
        }

        List<ComposableSpecification<T>> specificationList = new ArrayList<>();

        for (var filter : filters)
        {
            AndSpecification<T> specification = new AndSpecification<T>(filter);
            specificationList.add(specification);
        }

        var baseSpec = specificationList.remove(0);
        for (var spec : specificationList)
        {
            baseSpec = baseSpec.and(spec);
        }

        if (baseSpecification == null)
        {
            baseSpecification = baseSpec;
        }

        return this;
    }


    public JpaCrudRepository<T, ID> and(QueryFilter... filters)
    {
        if (baseSpecification == null)
            return where(filters);

        List<ComposableSpecification<T>> specificationList = new ArrayList<>();

        for (var filter : filters)
        {
            AndSpecification<T> specification = new AndSpecification<T>(filter);
            specificationList.add(specification);
        }

        var currentSpecification = specificationList.remove(0);
        for (var spec : specificationList)
        {
            currentSpecification = currentSpecification.and(spec);
        }

        baseSpecification = baseSpecification.and(currentSpecification);
        return this;
    }


    public JpaCrudRepository<T, ID> or(QueryFilter... filters)
    {
        if (baseSpecification == null)
            return where(filters);

        List<ComposableSpecification<T>> specificationList = new ArrayList<>();

        for (var filter : filters)
        {
            AndSpecification<T> specification = new AndSpecification<T>(filter);
            specificationList.add(specification);
        }

        var currentSpecification = specificationList.remove(0);
        for (var spec : specificationList)
        {
            currentSpecification = currentSpecification.or(spec);
        }

        baseSpecification = baseSpecification.or(currentSpecification);
        return this;
    }


    public JpaCrudRepository<T, ID> not(QueryFilter... filters)
    {
        if (baseSpecification == null)
            return where(filters);

        List<ComposableSpecification<T>> specificationList = new ArrayList<>();

        for (var filter : filters)
        {
            OrSpecification<T> specification = new OrSpecification<T>(filter);
            specificationList.add(specification);
        }

        var currentSpecification = specificationList.remove(0);
        for (var spec : specificationList)
        {
            currentSpecification = currentSpecification.or(spec);
        }

        baseSpecification = baseSpecification.not(filters);
        return this;
    }


    @Override
    public <S extends T> S save(S entity)
    {
        return null;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities)
    {
        return null;
    }

    @Override
    public void flush()
    {

    }

    @Override
    public <S extends T> S saveAndFlush(S entity)
    {
        return null;
    }

    @Override
    public <S extends T> List<S> saveAllAndFlush(Iterable<S> entities)
    {
        return null;
    }

    @Override
    public Optional<T> findById(ID aID)
    {
        return Optional.empty();
    }

    @Override
    public boolean existsById(ID aID)
    {
        return false;
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> longs)
    {
        return null;
    }

    @Override
    public long count()
    {
        return 0;
    }

    @Override
    public void deleteById(ID aID)
    {

    }

    @Override
    public void delete(T entity)
    {

    }

    @Override
    public void deleteAllById(Iterable<? extends ID> longs)
    {

    }

    @Override
    public void deleteAll(Iterable<? extends T> entities)
    {

    }

    @Override
    public void deleteAll()
    {

    }

}
