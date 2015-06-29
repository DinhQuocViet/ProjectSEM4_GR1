/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Model.UserRole;
import Model.BookingTour;
import java.util.ArrayList;
import java.util.Collection;
import Model.Rentcar;
import Model.Users;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Administrator
 */
public class UsersJpaController implements Serializable {

    public UsersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) throws PreexistingEntityException, Exception {
        if (users.getBookingTourCollection() == null) {
            users.setBookingTourCollection(new ArrayList<BookingTour>());
        }
        if (users.getRentcarCollection() == null) {
            users.setRentcarCollection(new ArrayList<Rentcar>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserRole URole = users.getURole();
            if (URole != null) {
                URole = em.getReference(URole.getClass(), URole.getUrId());
                users.setURole(URole);
            }
            Collection<BookingTour> attachedBookingTourCollection = new ArrayList<BookingTour>();
            for (BookingTour bookingTourCollectionBookingTourToAttach : users.getBookingTourCollection()) {
                bookingTourCollectionBookingTourToAttach = em.getReference(bookingTourCollectionBookingTourToAttach.getClass(), bookingTourCollectionBookingTourToAttach.getBId());
                attachedBookingTourCollection.add(bookingTourCollectionBookingTourToAttach);
            }
            users.setBookingTourCollection(attachedBookingTourCollection);
            Collection<Rentcar> attachedRentcarCollection = new ArrayList<Rentcar>();
            for (Rentcar rentcarCollectionRentcarToAttach : users.getRentcarCollection()) {
                rentcarCollectionRentcarToAttach = em.getReference(rentcarCollectionRentcarToAttach.getClass(), rentcarCollectionRentcarToAttach.getRId());
                attachedRentcarCollection.add(rentcarCollectionRentcarToAttach);
            }
            users.setRentcarCollection(attachedRentcarCollection);
            em.persist(users);
            if (URole != null) {
                URole.getUsersCollection().add(users);
                URole = em.merge(URole);
            }
            for (BookingTour bookingTourCollectionBookingTour : users.getBookingTourCollection()) {
                Users oldUIdOfBookingTourCollectionBookingTour = bookingTourCollectionBookingTour.getUId();
                bookingTourCollectionBookingTour.setUId(users);
                bookingTourCollectionBookingTour = em.merge(bookingTourCollectionBookingTour);
                if (oldUIdOfBookingTourCollectionBookingTour != null) {
                    oldUIdOfBookingTourCollectionBookingTour.getBookingTourCollection().remove(bookingTourCollectionBookingTour);
                    oldUIdOfBookingTourCollectionBookingTour = em.merge(oldUIdOfBookingTourCollectionBookingTour);
                }
            }
            for (Rentcar rentcarCollectionRentcar : users.getRentcarCollection()) {
                Users oldUIdOfRentcarCollectionRentcar = rentcarCollectionRentcar.getUId();
                rentcarCollectionRentcar.setUId(users);
                rentcarCollectionRentcar = em.merge(rentcarCollectionRentcar);
                if (oldUIdOfRentcarCollectionRentcar != null) {
                    oldUIdOfRentcarCollectionRentcar.getRentcarCollection().remove(rentcarCollectionRentcar);
                    oldUIdOfRentcarCollectionRentcar = em.merge(oldUIdOfRentcarCollectionRentcar);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsers(users.getUId()) != null) {
                throw new PreexistingEntityException("Users " + users + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getUId());
            UserRole URoleOld = persistentUsers.getURole();
            UserRole URoleNew = users.getURole();
            Collection<BookingTour> bookingTourCollectionOld = persistentUsers.getBookingTourCollection();
            Collection<BookingTour> bookingTourCollectionNew = users.getBookingTourCollection();
            Collection<Rentcar> rentcarCollectionOld = persistentUsers.getRentcarCollection();
            Collection<Rentcar> rentcarCollectionNew = users.getRentcarCollection();
            if (URoleNew != null) {
                URoleNew = em.getReference(URoleNew.getClass(), URoleNew.getUrId());
                users.setURole(URoleNew);
            }
            Collection<BookingTour> attachedBookingTourCollectionNew = new ArrayList<BookingTour>();
            for (BookingTour bookingTourCollectionNewBookingTourToAttach : bookingTourCollectionNew) {
                bookingTourCollectionNewBookingTourToAttach = em.getReference(bookingTourCollectionNewBookingTourToAttach.getClass(), bookingTourCollectionNewBookingTourToAttach.getBId());
                attachedBookingTourCollectionNew.add(bookingTourCollectionNewBookingTourToAttach);
            }
            bookingTourCollectionNew = attachedBookingTourCollectionNew;
            users.setBookingTourCollection(bookingTourCollectionNew);
            Collection<Rentcar> attachedRentcarCollectionNew = new ArrayList<Rentcar>();
            for (Rentcar rentcarCollectionNewRentcarToAttach : rentcarCollectionNew) {
                rentcarCollectionNewRentcarToAttach = em.getReference(rentcarCollectionNewRentcarToAttach.getClass(), rentcarCollectionNewRentcarToAttach.getRId());
                attachedRentcarCollectionNew.add(rentcarCollectionNewRentcarToAttach);
            }
            rentcarCollectionNew = attachedRentcarCollectionNew;
            users.setRentcarCollection(rentcarCollectionNew);
            users = em.merge(users);
            if (URoleOld != null && !URoleOld.equals(URoleNew)) {
                URoleOld.getUsersCollection().remove(users);
                URoleOld = em.merge(URoleOld);
            }
            if (URoleNew != null && !URoleNew.equals(URoleOld)) {
                URoleNew.getUsersCollection().add(users);
                URoleNew = em.merge(URoleNew);
            }
            for (BookingTour bookingTourCollectionOldBookingTour : bookingTourCollectionOld) {
                if (!bookingTourCollectionNew.contains(bookingTourCollectionOldBookingTour)) {
                    bookingTourCollectionOldBookingTour.setUId(null);
                    bookingTourCollectionOldBookingTour = em.merge(bookingTourCollectionOldBookingTour);
                }
            }
            for (BookingTour bookingTourCollectionNewBookingTour : bookingTourCollectionNew) {
                if (!bookingTourCollectionOld.contains(bookingTourCollectionNewBookingTour)) {
                    Users oldUIdOfBookingTourCollectionNewBookingTour = bookingTourCollectionNewBookingTour.getUId();
                    bookingTourCollectionNewBookingTour.setUId(users);
                    bookingTourCollectionNewBookingTour = em.merge(bookingTourCollectionNewBookingTour);
                    if (oldUIdOfBookingTourCollectionNewBookingTour != null && !oldUIdOfBookingTourCollectionNewBookingTour.equals(users)) {
                        oldUIdOfBookingTourCollectionNewBookingTour.getBookingTourCollection().remove(bookingTourCollectionNewBookingTour);
                        oldUIdOfBookingTourCollectionNewBookingTour = em.merge(oldUIdOfBookingTourCollectionNewBookingTour);
                    }
                }
            }
            for (Rentcar rentcarCollectionOldRentcar : rentcarCollectionOld) {
                if (!rentcarCollectionNew.contains(rentcarCollectionOldRentcar)) {
                    rentcarCollectionOldRentcar.setUId(null);
                    rentcarCollectionOldRentcar = em.merge(rentcarCollectionOldRentcar);
                }
            }
            for (Rentcar rentcarCollectionNewRentcar : rentcarCollectionNew) {
                if (!rentcarCollectionOld.contains(rentcarCollectionNewRentcar)) {
                    Users oldUIdOfRentcarCollectionNewRentcar = rentcarCollectionNewRentcar.getUId();
                    rentcarCollectionNewRentcar.setUId(users);
                    rentcarCollectionNewRentcar = em.merge(rentcarCollectionNewRentcar);
                    if (oldUIdOfRentcarCollectionNewRentcar != null && !oldUIdOfRentcarCollectionNewRentcar.equals(users)) {
                        oldUIdOfRentcarCollectionNewRentcar.getRentcarCollection().remove(rentcarCollectionNewRentcar);
                        oldUIdOfRentcarCollectionNewRentcar = em.merge(oldUIdOfRentcarCollectionNewRentcar);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = users.getUId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getUId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            UserRole URole = users.getURole();
            if (URole != null) {
                URole.getUsersCollection().remove(users);
                URole = em.merge(URole);
            }
            Collection<BookingTour> bookingTourCollection = users.getBookingTourCollection();
            for (BookingTour bookingTourCollectionBookingTour : bookingTourCollection) {
                bookingTourCollectionBookingTour.setUId(null);
                bookingTourCollectionBookingTour = em.merge(bookingTourCollectionBookingTour);
            }
            Collection<Rentcar> rentcarCollection = users.getRentcarCollection();
            for (Rentcar rentcarCollectionRentcar : rentcarCollection) {
                rentcarCollectionRentcar.setUId(null);
                rentcarCollectionRentcar = em.merge(rentcarCollectionRentcar);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Users findUsers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
