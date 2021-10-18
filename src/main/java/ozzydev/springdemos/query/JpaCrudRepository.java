package ozzydev.springdemos.query;

import ozzydev.springdemos.transaction.JpaTransactionScope;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@EnableTransactionManagement
//@Repository
//@Transactional(
//        isolation = Isolation.READ_COMMITTED,
//        propagation = Propagation.REQUIRED,
//        readOnly = false,
//        timeout = 30)
@Transactional
public abstract class JpaCrudRepository<T, ID> implements BaseJpaRepository<T, ID>
{

    //@PersistenceContext(unitName = "primary")
    //@PersistenceContext
    protected EntityManagerFactory entityManagerFactory;
    //protected EntityManager entityManager;
    protected JpaTransactionScope transactionScope;

    private ComposableSpecification<T> baseSpecification;
    private final Class<T> entityClass;

    protected JpaCrudRepository(final Class<T> clazz)
    {
        this.entityClass = clazz;
    }

    protected JpaCrudRepository(final Class<T> clazz, final EntityManagerFactory entityManagerFactory)
    {
        this.entityClass = clazz;
        this.entityManagerFactory = entityManagerFactory;
        //this.entityManager = entityManagerFactory.createEntityManager();
        transactionScope = new JpaTransactionScope(entityManagerFactory);
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

    protected Class<T> getEntityType()
    {
        return entityClass;
    }


    public static <T> String getTableName(EntityManager em, Class<T> entityClass)
    {

        Metamodel meta = em.getMetamodel();
        EntityType<T> entityType = meta.entity(entityClass);

        //Check whether @Table annotation is present on the class.
        Table t = entityClass.getAnnotation(Table.class);

        String tableName = (t == null)
                ? entityType.getName().toUpperCase()
                : t.name();
        return tableName;
    }

    public <T> String getTableName(Class<T> entityClass)
    {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Metamodel meta = entityManager.getMetamodel();
        EntityType<T> entityType = meta.entity(entityClass);

        //Check whether @Table annotation is present on the class.
        Table t = entityClass.getAnnotation(Table.class);

        String tableName = (t == null)
                ? entityType.getName().toUpperCase()
                : t.name();
        return tableName;
    }

    public String getTableName()
    {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Metamodel meta = entityManager.getMetamodel();
        EntityType<T> entityType = meta.entity(entityClass);

        //Check whether @Table annotation is present on the class.
        Table t = entityClass.getAnnotation(Table.class);

        String tableName = (t == null) ? entityType.getName().toUpperCase() : t.name();
        return tableName;
    }


    public static <T> String getEntityName(EntityManager em, Class<T> entityClass)
    {

        Metamodel meta = em.getMetamodel();
        EntityType<T> entityType = meta.entity(entityClass);

        //Check whether @Entity annotation is present on the class..
        Entity entity = entityClass.getAnnotation(Entity.class);

        String entityName = (entity == null) ? entityType.getName().toUpperCase() : entityClass.getSimpleName();
        return entityName;
    }

    public <T> String getEntityName(Class<T> entityClass)
    {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Metamodel meta = entityManager.getMetamodel();
        EntityType<T> entityType = meta.entity(entityClass);

        //Check whether @Entity annotation is present on the class.
        Entity entity = entityClass.getAnnotation(Entity.class);

        String entityName = (entity == null) ? entityType.getName().toUpperCase() : entityClass.getSimpleName();
        return entityName;
    }

    public String getEntityName()
    {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Metamodel meta = entityManager.getMetamodel();
        EntityType<T> entityType = meta.entity(entityClass);

        //Check whether @Entity annotation is present on the class.
        Entity entity = entityClass.getAnnotation(Entity.class);

        String entityName = (entity == null) ? entityType.getName().toUpperCase() : entityClass.getSimpleName();
        return entityName;
    }


    public SingularAttribute<? super T, ?> getIdAttribute()
    {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        IdentifiableType<T> of = (IdentifiableType<T>) entityManager.getMetamodel().managedType(entityClass);
        return of.getId(of.getIdType().getJavaType());
    }


    JpaQuerySpecification<T> getIdSelectPath()
    {
        JpaQuerySpecification<T> idSelectPath = (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get(getIdAttribute()));
        return idSelectPath;
    }

    private List<T> executeQuery(CriteriaQuery<T> criteriaQuery)
    {
        LockModeType lockModeType = LockModeType.PESSIMISTIC_READ;

        var rows = transactionScope.executeAndReturn(entityManager ->
        {
            var data = entityManager.createQuery(criteriaQuery).getResultList();
            return data;
        });

        return rows;

    }

    @Override
    public List<T> findAll()
    {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getEntityType());
        Root<T> root = criteriaQuery.from(getEntityType());

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
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getEntityType());
        Root<T> root = criteriaQuery.from(getEntityType());

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
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        // use specification.getType() to create a Root<T> instance
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getEntityType());
        Root<T> root = criteriaQuery.from(getEntityType());
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
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getEntityType());
        Root<T> root = criteriaQuery.from(getEntityType());

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
    public Optional<T> findById(ID id)
    {
        Class<T> domainType = getEntityType();
        //QueryHints.HINT_FETCH_SIZE
        LockModeType lockModeType = LockModeType.PESSIMISTIC_READ;
        //return Optional.empty();

        var entity = transactionScope.executeAndReturn(em ->
        {
            return em.find(entityClass, id, lockModeType);
        });

        //var entity = entityManager.find(domainType, id, lockModeType);
        //var entity = entityManager.find(domainType, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public boolean existsById(ID id)
    {
        var exists = false;

        Class<T> domainType = getEntityType();
        //QueryHints.HINT_FETCH_SIZE
        LockModeType lockModeType = LockModeType.PESSIMISTIC_READ;
        //return Optional.empty();
        //var entity=entityManager.find(domainType, id, lockModeType);

        //var entity = entityManager.find(domainType, id);

        var entity = transactionScope.executeAndReturn(em ->
        {
            //return em.find(domainClass, id, lockModeType);
            return em.getReference(entityClass, id);
        });

        return Optional.ofNullable(entity).isPresent();

        //        var entityRef = entityManager.getReference(domainType, id);
        //        return entityRef == null;
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
