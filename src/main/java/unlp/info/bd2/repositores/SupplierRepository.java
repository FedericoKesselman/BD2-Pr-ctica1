package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import unlp.info.bd2.model.Supplier;

public class SupplierRepository extends GenericRepository<Supplier> {
    
    public SupplierRepository(SessionFactory sessionFactory) { 
        super(Supplier.class, sessionFactory); 
    }
}