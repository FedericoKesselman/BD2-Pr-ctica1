package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import unlp.info.bd2.model.Review;

public class ReviewRepository extends GenericRepository<Review> {
    
    public ReviewRepository(SessionFactory sessionFactory) { 
        super(Review.class, sessionFactory); 
    }
}