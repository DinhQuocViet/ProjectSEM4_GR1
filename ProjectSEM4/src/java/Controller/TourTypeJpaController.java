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
import Entity.PackageTour;
import Entity.TourType;
import java.util.ArrayList;
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
        if (tourType.getPackageTourList() == null) {
            tourType.setPackageTourList(new ArrayList<PackageTour>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PackageTour> attachedPackageTourList = new ArrayList<PackageTour>();
            for (PackageTour packageTourListPackageTourToAttach : tourType.getPackageTourList()) {
                packageTourListPackageTourToAttach = em.getReference(packageTourListPackageTourToAttach.getClass(), packageTourListPackageTourToAttach.getPId());
                attachedPackageTourList.add(packageTourListPackageTourToAttach);
            }
            tourType.setPackageTourList(attachedPackageTourList);
            em.persist(tourType);
            for (PackageTour packageTourListPackageTour : tourType.getPackageTourList()) {
                TourType oldPTypeOfPackageTourListPackageTour = packageTourListPackageTour.getPType();
                packageTourListPackageTour.setPType(tourType);
                packageTourListPackageTour = em.merge(packageTourListPackageTour);
                if (oldPTypeOfPackageTourListPackageTour != null) {
                    oldPTypeOfPackageTourListPackageTour.getPackageTourList().remove(packageTourListPackageTour);
                    oldPTypeOfPackageTourListPackageTour = em.merge(oldPTypeOfPackageTourListPackageTour);
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
            List<PackageTour> packageTourListOld = persistentTourType.getPackageTourList();
            List<PackageTour> packageTourListNew = tourType.getPackageTourList();
            List<PackageTour> attachedPackageTourListNew = new ArrayList<PackageTour>();
            for (PackageTour packageTourListNewPackageTourToAttach : packageTourListNew) {
                packageTourListNewPackageTourToAttach = em.getReference(packageTourListNewPackageTourToAttach.getClass(), packageTourListNewPackageTourToAttach.getPId());
                attachedPackageTourListNew.add(packageTourListNewPackageTourToAttach);
            }
            packageTourListNew = attachedPackageTourListNew;
            tourType.setPackageTourList(packageTourListNew);
            tourType = em.merge(tourType);
            for (PackageTour packageTourListOldPackageTour : packageTourListOld) {
                if (!packageTourListNew.contains(packageTourListOldPackageTour)) {
                    packageTourListOldPackageTour.setPType(null);
                    packageTourListOldPackageTour = em.merge(packageTourListOldPackageTour);
                }
            }
            for (PackageTour packageTourListNewPackageTour : packageTourListNew) {
                if (!packageTourListOld.contains(packageTourListNewPackageTour)) {
                    TourType oldPTypeOfPackageTourListNewPackageTour = packageTourListNewPackageTour.getPType();
                    packageTourListNewPackageTour.setPType(tourType);
                    packageTourListNewPackageTour = em.merge(packageTourListNewPackageTour);
                    if (oldPTypeOfPackageTourListNewPackageTour != null && !oldPTypeOfPackageTourListNewPackageTour.equals(tourType)) {
                        oldPTypeOfPackageTourListNewPackageTour.getPackageTourList().remove(packageTourListNewPackageTour);
                        oldPTypeOfPackageTourListNewPackageTour = em.merge(oldPTypeOfPackageTourListNewPackageTour);
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
            List<PackageTour> packageTourList = tourType.getPackageTourList();
            for (PackageTour packageTourListPackageTour : packageTourList) {
                packageTourListPackageTour.setPType(null);
                packageTourListPackageTour = em.merge(packageTourListPackageTour);
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
