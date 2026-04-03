package unlp.info.bd2.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import unlp.info.bd2.model.*;
import unlp.info.bd2.repositories.*;
import unlp.info.bd2.utils.ToursException;

import java.util.Date;
import java.util.List;

public class TourseServiceImpl implements ToursService {
    private SessionFactory sessionFactory;

    private UserRepository userRepo;
    private RouteRepository routeRepo;
    private PurchaseRepository purchaseRepo;
    private ServiceRepository serviceRepo;
    private SupplierRepository supplierRepo;
    private ReviewRepository reviewRepo;

    public void ToursServiceImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

        this.userRepo = new UserRepository(sessionFactory);
        this.routeRepo = new RouteRepository(sessionFactory);
        this.purchaseRepo = new PurchaseRepository(sessionFactory);
        this.serviceRepo = new ServiceRepository(sessionFactory);
        this.supplierRepo = new SupplierRepository(sessionFactory);
        this.reviewRepo = new ReviewRepository(sessionFactory);
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public User createUser(String username, String password, String name,
                           String email, Date birthdate, String phoneNumber) throws ToursException {

        Session session = getSession();
        session.beginTransaction();

        try {
            User user = new User(username, password, name, email, birthdate, phoneNumber);

            userRepo.save(user);

            session.getTransaction().commit();
            return user;

        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new ToursException("Error creating user", e);
        }
    }

    @Override
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {

        Session session = getSession();
        session.beginTransaction();

        try {
            Purchase purchase = new Purchase(code, new Date(), route, user);

            purchaseRepo.save(purchase);

            session.getTransaction().commit();
            return purchase;

        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new ToursException("Error creating purchase", e);
        }
    }

    @Override
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException {

        Session session = getSession();
        session.beginTransaction();

        try {
            Service service = serviceRepo.findById(id);

            if (service == null) {
                throw new ToursException("Service not found");
            }

            service.setPrice(newPrice);

            session.getTransaction().commit(); // dirty checking

            return service;

        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new ToursException("Error updating service price", e);
        }
    }

    @Override
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {

        Session session = getSession();
        session.beginTransaction();

        try {
            Purchase managedPurchase = purchaseRepo.findById(purchase.getId());
            Service managedService = serviceRepo.findById(service.getId());

            if (managedPurchase == null || managedService == null) {
                throw new ToursException("Purchase or Service not found");
            }

            ItemService item = new ItemService(quantity, managedService);

            managedPurchase.addItem(item); // importante mantener consistencia

            session.getTransaction().commit();

            return item;

        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new ToursException("Error adding item to purchase", e);
        }
    }

    // ---------- EJERCICIO 46 ----------

    // Inciso A
    @Override 
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        return getSession().createQuery(
                        "SELECT p FROM Purchase p WHERE p.user.username = :username",
                        Purchase.class
                ).setParameter("username", username)
                .getResultList();
    }

    // Inciso B
    @Override
    public List<User> getUserSpendingMoreThan(float amount) {
        return getSession().createQuery(
                        "SELECT DISTINCT p.user FROM Purchase p " +
                                "JOIN p.itemServiceList i " +
                                "WHERE (p.route.price + SUM(i.quantity * i.service.price)) >= :amount " +
                                "GROUP BY p.id, p.user",
                        User.class
                ).setParameter("amount", amount)
                .getResultList();
    }

    // Inciso C
    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        return getSession().createQuery(
                        "SELECT s FROM Supplier s " +
                                "JOIN s.services serv " +
                                "JOIN serv.itemServiceList i " +
                                "GROUP BY s " +
                                "ORDER BY SUM(i.quantity) DESC",
                        Supplier.class
                ).setMaxResults(n)
                .getResultList();
    }

    // Inciso D
    @Override
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        Long count = getSession().createQuery(
                        "SELECT COUNT(p) FROM Purchase p " +
                                "WHERE p.date BETWEEN :start AND :end",
                        Long.class
                ).setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();

        return count.intValue();
    }

    // Inciso E
    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        return getSession().createQuery(
                        "SELECT r FROM Route r JOIN r.stops s WHERE s = :stop",
                        Route.class
                ).setParameter("stop", stop)
                .getResultList();
    }

    // Inciso F
    @Override
    public int getMaxStopOfRoutes() {
        Integer max = getSession().createQuery(
                "SELECT MAX(SIZE(r.stops)) FROM Route r",
                Integer.class
        ).getSingleResult();

        return max != null ? max : 0;
    }

    // Inciso G
    @Override
    public List<Route> getRoutsNotSell() {
        return getSession().createQuery(
                "SELECT r FROM Route r " +
                        "LEFT JOIN Purchase p ON p.route = r " +
                        "WHERE p.id IS NULL",
                Route.class
        ).getResultList();
    }

    // Inciso H
    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        return getSession().createQuery(
                        "SELECT p.route FROM Purchase p " +
                                "WHERE p.review IS NOT NULL " +
                                "GROUP BY p.route " +
                                "ORDER BY AVG(p.review.rating) DESC",
                        Route.class
                ).setMaxResults(3)
                .getResultList();
    }

    // Inciso I
    @Override
    public Service getMostDemandedService() {
        return getSession().createQuery(
                        "SELECT i.service FROM ItemService i " +
                                "GROUP BY i.service " +
                                "ORDER BY SUM(i.quantity) DESC",
                        Service.class
                ).setMaxResults(1)
                .getSingleResult();
    }

    // Inciso J
    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return getSession().createQuery(
                "SELECT DISTINCT g FROM Purchase p " +
                        "JOIN p.route r " +
                        "JOIN r.tourGuideList g " +
                        "JOIN p.review rev " +
                        "WHERE rev.rating = 1",
                TourGuideUser.class
        ).getResultList();
    }
}