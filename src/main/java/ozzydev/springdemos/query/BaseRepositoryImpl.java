//package ozzydev.springdemos.query;
//
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.support.JpaEntityInformation;
//import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//
//public class BaseRepositoryImpl<T, ID>
//        extends SimpleJpaRepository<T, ID>
//        //implements JpaSpecificationExecutor<T>
//{
//
//    private final EntityManager entityManager;
//
//    BaseRepositoryImpl(JpaEntityInformation entityInformation,
//                       EntityManager entityManager)
//    {
//        super(entityInformation, entityManager);
//
//        // Keep the EntityManager around to used from the newly introduced methods.
//        this.entityManager = entityManager;
//    }
//
//    @Transactional
//    public <S extends T> S save(S entity)
//    {
//        // implementation goes here
//        return null;
//    }
//}
