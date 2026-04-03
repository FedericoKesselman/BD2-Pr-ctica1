package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import unlp.info.bd2.model.User;

public class UserRepository extends GenericRepository<User> {
    
    public UserRepository(SessionFactory sessionFactory) { 
        super(User.class, sessionFactory); 
    }
}