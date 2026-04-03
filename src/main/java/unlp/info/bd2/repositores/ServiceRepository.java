package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import unlp.info.bd2.model.Service;

public class ServiceRepository extends GenericRepository<Service> {
    
    public ServiceRepository(SessionFactory sessionFactory) { 
        super(Service.class, sessionFactory); 
    }
}