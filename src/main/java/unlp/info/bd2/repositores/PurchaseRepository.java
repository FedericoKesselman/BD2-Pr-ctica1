package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import unlp.info.bd2.model.Purchase;

public class PurchaseRepository extends GenericRepository<Purchase> {
    
    public PurchaseRepository(SessionFactory sessionFactory) { 
        super(Purchase.class, sessionFactory); 
    }
}