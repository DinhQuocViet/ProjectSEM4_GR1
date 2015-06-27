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
import Entity.UserRole;
import Entity.BookingTour;
import java.util.ArrayList;
import java.util.List;
import Entity.Rentcar;
import Entity.Users;
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
        if (users.getBookingTourList() == null) {
            users.setBookingTourList(new ArrayList<BookingTour>());
        }
        if (users.getRentcarList() == null) {
            users.setRentcarList(new ArrayList<Rentcar>());
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
            List<BookingTour> attachedBookingTourList = new ArrayList<BookingTour>();
            for (BookingTour bookingTourListBookingTourToAttach : users.getBookingTourList()) {
                bookingTourListBookingTourToAttach = em.getReference(bookingTourListBookingTourToAttach.getClass(), bookingTourListBookingTourToAttach.getBId());
                attachedBookingTourList.add(bookingTourListBookingTourToAttach);
            }
            users.setBookingTourList(attachedBookingTourList);
            List<Rentcar> attachedRentcarList = new ArrayList<Rentcar>();
            for (Rentcar rentcarListRentcarToAttach : users.getRentcarList()) {
                rentcarListRentcarToAttach = em.getReference(rentcarListRentcarToAttach.getClass(), rentcarListRentcarToAttach.getRId());
                attachedRentcarList.add(rentcarListRentcarToAttach);
            }
            users.setRentcarList(attachedRentcarList);
            em.persist(users);
            if (URole != null) {
                URole.getUsersList().add(users);
                URole = em.merge(URole);
            }
            for (BookingTour bookingTourListBookingTour : users.getBookingTourList()) {
                Users oldUIdOfBookingTourListBookingTour = bookingTourListBookingTour.getUId();
                bookingTourListBookingTour.setUId(users);
                bookingTourListBookingTour = em.merge(bookingTourListBookingTour);
                if (oldUIdOfBookingTourListBookingTour != null) {
                    oldUIdOfBookingTourListBookingTour.getBookingTourList().remove(bookingTourListBookingTour);
                    oldUIdOfBookingTourListBookingTour = em.merge(oldUIdOfBookingTourListBookingTour);
                }
            }
            for (Rentcar rentcarListRentcar : users.getRentcarList()) {
                Users oldUIdOfRentcarListRentcar = rentcarListRentcar.getUId();
                rentcarListRentcar.setUId(users);
                rentcarListRentcar = em.merge(rentcarListRentcar);
                if (oldUIdOfRentcarListRentcar != null) {
                    oldUIdOfRentcarListRentcar.getRentcarList().remove(rentcarListRentcar);
                    oldUIdOfRentcarListRentcar = em.merge(oldUIdOfRentcarListRentcar);
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
            List<BookingTour> bookingTourListOld = persistentUsers.getBookingTourList();
            List<BookingTour> bookingTourListNew = users.getBookingTourList();
            List<Rentcar> rentcarListOld = persistentUsers.getRentcarList();
            List<Rentcar> rentcarListNew = users.getRentcarList();
            if (URoleNew != null) {
                URoleNew = em.getReference(URoleNew.getClass(), URoleNew.getUrId());
                users.setURole(URoleNew);
            }
            List<BookingTour> attachedBookingTourListNew = new ArrayList<BookingTour>();
            for (BookingTour bookingTourListNewBookingTourToAttach : bookingTourListNew) {
                bookingTourListNewBookingTourToAttach = em.getReference(bookingTourListNewBookingTourToAttach.getClass(), bookingTourListNewBookingTourToAttach.getBId());
                attachedBookingTourListNew.add(bookingTourListNewBookingTourToAttach);
            }
            bookingTourListNew = attachedBookingTourListNew;
            users.setBookingTourList(bookingTourListNew);
            List<Rentcar> attachedRentcarListNew = new ArrayList<Rentcar>();
            for (Rentcar rentcarListNewRentcarToAttach : rentcarListNew) {
                rentcarListNewRentcarToAttach = em.getReference(rentcarListNewRentcarToAttach.getClass(), rentcarListNewRentcarToAttach.getRId());
                attachedRentcarListNew.add(rentcarListNewRentcarToAttach);
            }
            rentcarListNew = attachedRentcarListNew;
            users.setRentcarList(rentcarListNew);
            users = em.merge(users);
            if (URoleOld != null && !URoleOld.equals(URoleNew)) {
                URoleOld.getUsersList().remove(users);
                URoleOld = em.merge(URoleOld);
            }
            if (URoleNew != null && !URoleNew.equals(URoleOld)) {
                URoleNew.getUsersList().add(users);
                URoleNew = em.merge(URoleNew);
            }
            for (BookingTour bookingTourListOldBookingTour : bookingTourListOld) {
                if (!bookingTourListNew.contains(bookingTourListOldBookingTour)) {
                    bookingTourListOldBookingTour.setUId(null);
                    bookingTourListOldBookingTour = em.merge(bookingTourListOldBookingTour);
                }
            }
            for (BookingTour bookingTourListNewBookingTour : bookingTourListNew) {
                if (!bookingTourListOld.contains(bookingTourListNewBookingTour)) {
                    Users oldUIdOfBookingTourListNewBookingTour = bookingTourListNewBookingTour.getUId();
                    bookingTourListNewBookingTour.setUId(users);
                    bookingTourListNewBookingTour = em.merge(bookingTourListNewBookingTour);
                    if (oldUIdOfBookingTourListNewBookingTour != null && !oldUIdOfBookingTourListNewBookingTour.equals(users)) {
                        oldUIdOfBookingTourListNewBookingTour.getBookingTourList().remove(bookingTourListNewBookingTour);
                        oldUIdOfBookingTourListNewBookingTour = em.merge(oldUIdOfBookingTourListNewBookingTour);
                    }
                }
            }
            for (Rentcar rentcarListOldRentcar : rentcarListOld) {
                if (!rentcarListNew.contains(rentcarListOldRentcar)) {
                    rentcarListOldRentcar.setUId(null);
                    rentcarListOldRentcar = em.merge(rentcarListOldRentcar);
                }
            }
            for (Rentcar rentcarListNewRentcar : rentcarListNew) {
                if (!rentcarListOld.contains(rentcarListNewRentcar)) {
                    Users oldUIdOfRentcarListNewRentcar = rentcarListNewRentcar.getUId();
                    rentcarListNewRentcar.setUId(users);
                    rentcarListNewRentcar = em.merge(rentcarListNewRentcar);
                    if (oldUIdOfRentcarListNewRentcar != null && !oldUIdOfRentcarListNewRentcar.equals(users)) {
                        oldUIdOfRentcarListNewRentcar.getRentcarList().remove(rentcarListNewRentcar);
                        oldUIdOfRentcarListNewRentcar = em.merge(oldUIdOfRentcarListNewRentcar);
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
                URole.getUsersList().remove(users);
                URole = em.merge(URole);
            }
            List<BookingTour> bookingTourList = users.getBookingTourList();
            for (BookingTour bookingTourListBookingTour : bookingTourList) {
                bookingTourListBookingTour.setUId(null);
                bookingTourListBookingTour = em.merge(bookingTourListBookingTour);
            }
            List<Rentcar> rentcarList = users.getRentcarList();
            for (Rentcar rentcarListRentcar : rentcarList) {
                rentcarListRentcar.setUId(null);
                rentcarListRentcar = em.merge(rentcarListRentcar);
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
