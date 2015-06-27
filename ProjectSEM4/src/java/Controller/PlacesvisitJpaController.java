/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Entity.Placesvisit;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Administrator
 */
public class PlacesvisitJpaController implements Serializable {

    public PlacesvisitJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Placesvisit placesvisit) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(placesvisit);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPlacesvisit(placesvisit.getVId()) != null) {
                throw new PreexistingEntityException("Placesvisit " + placesvisit + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Placesvisit placesvisit) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            placesvisit = em.merge(placesvisit);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = placesvisit.getVId();
                if (findPlacesvisit(id) == null) {
                    throw new NonexistentEntityException("The placesvisit with id " + id + " no longer exists.");
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
            Placesvisit placesvisit;
            try {
                placesvisit = em.getReference(Placesvisit.class, id);
                placesvisit.getVId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The placesvisit with id " + id + " no longer exists.", enfe);
            }
            em.remove(placesvisit);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Placesvisit> findPlacesvisitEntities() {
        return findPlacesvisitEntities(true, -1, -1);
    }

    public List<Placesvisit> findPlacesvisitEntities(int maxResults, int firstResult) {
        return findPlacesvisitEntities(false, maxResults, firstResult);
    }

    private List<Placesvisit> findPlacesvisitEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Placesvisit.class));
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

    public Placesvisit findPlacesvisit(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Placesvisit.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlacesvisitCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Placesvisit> rt = cq.from(Placesvisit.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
