/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Model.PackageTour;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Model.TourType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Administrator
 */
public class PackageTourJpaController implements Serializable {

    public PackageTourJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PackageTour packageTour) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TourType PType = packageTour.getPType();
            if (PType != null) {
                PType = em.getReference(PType.getClass(), PType.getTId());
                packageTour.setPType(PType);
            }
            em.persist(packageTour);
            if (PType != null) {
                PType.getPackageTourCollection().add(packageTour);
                PType = em.merge(PType);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPackageTour(packageTour.getPId()) != null) {
                throw new PreexistingEntityException("PackageTour " + packageTour + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PackageTour packageTour) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PackageTour persistentPackageTour = em.find(PackageTour.class, packageTour.getPId());
            TourType PTypeOld = persistentPackageTour.getPType();
            TourType PTypeNew = packageTour.getPType();
            if (PTypeNew != null) {
                PTypeNew = em.getReference(PTypeNew.getClass(), PTypeNew.getTId());
                packageTour.setPType(PTypeNew);
            }
            packageTour = em.merge(packageTour);
            if (PTypeOld != null && !PTypeOld.equals(PTypeNew)) {
                PTypeOld.getPackageTourCollection().remove(packageTour);
                PTypeOld = em.merge(PTypeOld);
            }
            if (PTypeNew != null && !PTypeNew.equals(PTypeOld)) {
                PTypeNew.getPackageTourCollection().add(packageTour);
                PTypeNew = em.merge(PTypeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = packageTour.getPId();
                if (findPackageTour(id) == null) {
                    throw new NonexistentEntityException("The packageTour with id " + id + " no longer exists.");
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
            PackageTour packageTour;
            try {
                packageTour = em.getReference(PackageTour.class, id);
                packageTour.getPId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The packageTour with id " + id + " no longer exists.", enfe);
            }
            TourType PType = packageTour.getPType();
            if (PType != null) {
                PType.getPackageTourCollection().remove(packageTour);
                PType = em.merge(PType);
            }
            em.remove(packageTour);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PackageTour> findPackageTourEntities() {
        return findPackageTourEntities(true, -1, -1);
    }

    public List<PackageTour> findPackageTourEntities(int maxResults, int firstResult) {
        return findPackageTourEntities(false, maxResults, firstResult);
    }

    private List<PackageTour> findPackageTourEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PackageTour.class));
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

    public PackageTour findPackageTour(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PackageTour.class, id);
        } finally {
            em.close();
        }
    }

    public int getPackageTourCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PackageTour> rt = cq.from(PackageTour.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
