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
import Model.PackageTour;
import Model.TourType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Administrator
 */
public class TourTypeJpaController implements Serializable {

    public TourTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TourType tourType) throws PreexistingEntityException, Exception {
        if (tourType.getPackageTourCollection() == null) {
            tourType.setPackageTourCollection(new ArrayList<PackageTour>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<PackageTour> attachedPackageTourCollection = new ArrayList<PackageTour>();
            for (PackageTour packageTourCollectionPackageTourToAttach : tourType.getPackageTourCollection()) {
                packageTourCollectionPackageTourToAttach = em.getReference(packageTourCollectionPackageTourToAttach.getClass(), packageTourCollectionPackageTourToAttach.getPId());
                attachedPackageTourCollection.add(packageTourCollectionPackageTourToAttach);
            }
            tourType.setPackageTourCollection(attachedPackageTourCollection);
            em.persist(tourType);
            for (PackageTour packageTourCollectionPackageTour : tourType.getPackageTourCollection()) {
                TourType oldPTypeOfPackageTourCollectionPackageTour = packageTourCollectionPackageTour.getPType();
                packageTourCollectionPackageTour.setPType(tourType);
                packageTourCollectionPackageTour = em.merge(packageTourCollectionPackageTour);
                if (oldPTypeOfPackageTourCollectionPackageTour != null) {
                    oldPTypeOfPackageTourCollectionPackageTour.getPackageTourCollection().remove(packageTourCollectionPackageTour);
                    oldPTypeOfPackageTourCollectionPackageTour = em.merge(oldPTypeOfPackageTourCollectionPackageTour);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTourType(tourType.getTId()) != null) {
                throw new PreexistingEntityException("TourType " + tourType + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TourType tourType) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TourType persistentTourType = em.find(TourType.class, tourType.getTId());
            Collection<PackageTour> packageTourCollectionOld = persistentTourType.getPackageTourCollection();
            Collection<PackageTour> packageTourCollectionNew = tourType.getPackageTourCollection();
            Collection<PackageTour> attachedPackageTourCollectionNew = new ArrayList<PackageTour>();
            for (PackageTour packageTourCollectionNewPackageTourToAttach : packageTourCollectionNew) {
                packageTourCollectionNewPackageTourToAttach = em.getReference(packageTourCollectionNewPackageTourToAttach.getClass(), packageTourCollectionNewPackageTourToAttach.getPId());
                attachedPackageTourCollectionNew.add(packageTourCollectionNewPackageTourToAttach);
            }
            packageTourCollectionNew = attachedPackageTourCollectionNew;
            tourType.setPackageTourCollection(packageTourCollectionNew);
            tourType = em.merge(tourType);
            for (PackageTour packageTourCollectionOldPackageTour : packageTourCollectionOld) {
                if (!packageTourCollectionNew.contains(packageTourCollectionOldPackageTour)) {
                    packageTourCollectionOldPackageTour.setPType(null);
                    packageTourCollectionOldPackageTour = em.merge(packageTourCollectionOldPackageTour);
                }
            }
            for (PackageTour packageTourCollectionNewPackageTour : packageTourCollectionNew) {
                if (!packageTourCollectionOld.contains(packageTourCollectionNewPackageTour)) {
                    TourType oldPTypeOfPackageTourCollectionNewPackageTour = packageTourCollectionNewPackageTour.getPType();
                    packageTourCollectionNewPackageTour.setPType(tourType);
                    packageTourCollectionNewPackageTour = em.merge(packageTourCollectionNewPackageTour);
                    if (oldPTypeOfPackageTourCollectionNewPackageTour != null && !oldPTypeOfPackageTourCollectionNewPackageTour.equals(tourType)) {
                        oldPTypeOfPackageTourCollectionNewPackageTour.getPackageTourCollection().remove(packageTourCollectionNewPackageTour);
                        oldPTypeOfPackageTourCollectionNewPackageTour = em.merge(oldPTypeOfPackageTourCollectionNewPackageTour);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tourType.getTId();
                if (findTourType(id) == null) {
                    throw new NonexistentEntityException("The tourType with id " + id + " no longer exists.");
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
            TourType tourType;
            try {
                tourType = em.getReference(TourType.class, id);
                tourType.getTId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tourType with id " + id + " no longer exists.", enfe);
            }
            Collection<PackageTour> packageTourCollection = tourType.getPackageTourCollection();
            for (PackageTour packageTourCollectionPackageTour : packageTourCollection) {
                packageTourCollectionPackageTour.setPType(null);
                packageTourCollectionPackageTour = em.merge(packageTourCollectionPackageTour);
            }
            em.remove(tourType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TourType> findTourTypeEntities() {
        return findTourTypeEntities(true, -1, -1);
    }

    public List<TourType> findTourTypeEntities(int maxResults, int firstResult) {
        return findTourTypeEntities(false, maxResults, firstResult);
    }

    private List<TourType> findTourTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TourType.class));
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

    public TourType findTourType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TourType.class, id);
        } finally {
            em.close();
        }
    }

    public int getTourTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TourType> rt = cq.from(TourType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
