package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import unlp.info.bd2.model.Route;

public class RouteRepository extends GenericRepository<Route> {
    
    public RouteRepository(SessionFactory sessionFactory) { 
        super(Route.class, sessionFactory); 
    }
}