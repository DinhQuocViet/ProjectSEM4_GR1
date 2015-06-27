/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Entity.BookingTour;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.Users;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Administrator
 */
public class BookingTourJpaController implements Serializable {

    public BookingTourJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BookingTour bookingTour) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users UId = bookingTour.getUId();
            if (UId != null) {
                UId = em.getReference(UId.getClass(), UId.getUId());
                bookingTour.setUId(UId);
            }
            em.persist(bookingTour);
            if (UId != null) {
                UId.getBookingTourList().add(bookingTour);
                UId = em.merge(UId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBookingTour(bookingTour.getBId()) != null) {
                throw new PreexistingEntityException("BookingTour " + bookingTour + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BookingTour bookingTour) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BookingTour persistentBookingTour = em.find(BookingTour.class, bookingTour.getBId());
            Users UIdOld = persistentBookingTour.getUId();
            Users UIdNew = bookingTour.getUId();
            if (UIdNew != null) {
                UIdNew = em.getReference(UIdNew.getClass(), UIdNew.getUId());
                bookingTour.setUId(UIdNew);
            }
            bookingTour = em.merge(bookingTour);
            if (UIdOld != null && !UIdOld.equals(UIdNew)) {
                UIdOld.getBookingTourList().remove(bookingTour);
                UIdOld = em.merge(UIdOld);
            }
            if (UIdNew != null && !UIdNew.equals(UIdOld)) {
                UIdNew.getBookingTourList().add(bookingTour);
                UIdNew = em.merge(UIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = bookingTour.getBId();
                if (findBookingTour(id) == null) {
                    throw new NonexistentEntityException("The bookingTour with id " + id + " no longer exists.");
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
            BookingTour bookingTour;
            try {
                bookingTour = em.getReference(BookingTour.class, id);
                bookingTour.getBId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bookingTour with id " + id + " no longer exists.", enfe);
            }
            Users UId = bookingTour.getUId();
            if (UId != null) {
                UId.getBookingTourList().remove(bookingTour);
                UId = em.merge(UId);
            }
            em.remove(bookingTour);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BookingTour> findBookingTourEntities() {
        return findBookingTourEntities(true, -1, -1);
    }

    public List<BookingTour> findBookingTourEntities(int maxResults, int firstResult) {
        return findBookingTourEntities(false, maxResults, firstResult);
    }

    private List<BookingTour> findBookingTourEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BookingTour.class));
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

    public BookingTour findBookingTour(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BookingTour.class, id);
        } finally {
            em.close();
        }
    }

    public int getBookingTourCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BookingTour> rt = cq.from(BookingTour.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
