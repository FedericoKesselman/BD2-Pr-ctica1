package unlp.info.bd2.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public class GenericRepository<T> {
    private final Class<T> clazz;
    protected SessionFactory sessionFactory;
    
    public GenericRepository(Clas<T> clazz, SessionFactory sessionFactory) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    public void save(T entity) {
        this.getSession().get(clazz,id);
    }

    public List<T> findAll() {
        return getSession()
                .createQuery("from " + clazz.getName(), clazz)
                .getResultList();
    }

    public void delete(T entity) { getSession().remove(entity); }
}