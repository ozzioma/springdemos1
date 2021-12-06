package ozzydev.springdemos.query;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface BaseJpaRepository<T, ID>
{
    <S extends T> S save(S entity);


    <S extends T> Iterable<S> saveAll(Iterable<S> entities);


    void flush();


    <S extends T> S saveAndFlush(S entity);


    <S extends T> List<S> saveAllAndFlush(Iterable<S> entities);


    Optional<T> findById(ID id);


    boolean existsById(ID id);


    Iterable<T> findAll();

    Iterable<T> findAll(Pageable pageable);

    List<T> findAll(QueryFilter... filters);

    List<T> findAll(Pageable pageable, QueryFilter... filters);

    List<T> orFindAll(QueryFilter... filters);

    List<T> orFindAll(Pageable pageable, QueryFilter... filters);

    List<T> findAll(ComposableSpecification<T> specification);

    List<T> findAll(Pageable pageable, ComposableSpecification<T> specification);

    Iterable<T> findAllById(Iterable<ID> ids);

    long count();

    void deleteById(ID id);


    void delete(T entity);


    void deleteAllById(Iterable<? extends ID> ids);


    void deleteAll(Iterable<? extends T> entities);


    void deleteAll();
}
