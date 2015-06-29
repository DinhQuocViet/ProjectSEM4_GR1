/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Model.CarforRent;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Model.Rentcar;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Administrator
 */
public class CarforRentJpaController implements Serializable {

    public CarforRentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CarforRent carforRent) throws PreexistingEntityException, Exception {
        if (carforRent.getRentcarCollection() == null) {
            carforRent.setRentcarCollection(new ArrayList<Rentcar>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Rentcar> attachedRentcarCollection = new ArrayList<Rentcar>();
            for (Rentcar rentcarCollectionRentcarToAttach : carforRent.getRentcarCollection()) {
                rentcarCollectionRentcarToAttach = em.getReference(rentcarCollectionRentcarToAttach.getClass(), rentcarCollectionRentcarToAttach.getRId());
                attachedRentcarCollection.add(rentcarCollectionRentcarToAttach);
            }
            carforRent.setRentcarCollection(attachedRentcarCollection);
            em.persist(carforRent);
            for (Rentcar rentcarCollectionRentcar : carforRent.getRentcarCollection()) {
                CarforRent oldCIdOfRentcarCollectionRentcar = rentcarCollectionRentcar.getCId();
                rentcarCollectionRentcar.setCId(carforRent);
                rentcarCollectionRentcar = em.merge(rentcarCollectionRentcar);
                if (oldCIdOfRentcarCollectionRentcar != null) {
                    oldCIdOfRentcarCollectionRentcar.getRentcarCollection().remove(rentcarCollectionRentcar);
                    oldCIdOfRentcarCollectionRentcar = em.merge(oldCIdOfRentcarCollectionRentcar);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCarforRent(carforRent.getCId()) != null) {
                throw new PreexistingEntityException("CarforRent " + carforRent + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CarforRent carforRent) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CarforRent persistentCarforRent = em.find(CarforRent.class, carforRent.getCId());
            Collection<Rentcar> rentcarCollectionOld = persistentCarforRent.getRentcarCollection();
            Collection<Rentcar> rentcarCollectionNew = carforRent.getRentcarCollection();
            Collection<Rentcar> attachedRentcarCollectionNew = new ArrayList<Rentcar>();
            for (Rentcar rentcarCollectionNewRentcarToAttach : rentcarCollectionNew) {
                rentcarCollectionNewRentcarToAttach = em.getReference(rentcarCollectionNewRentcarToAttach.getClass(), rentcarCollectionNewRentcarToAttach.getRId());
                attachedRentcarCollectionNew.add(rentcarCollectionNewRentcarToAttach);
            }
            rentcarCollectionNew = attachedRentcarCollectionNew;
            carforRent.setRentcarCollection(rentcarCollectionNew);
            carforRent = em.merge(carforRent);
            for (Rentcar rentcarCollectionOldRentcar : rentcarCollectionOld) {
                if (!rentcarCollectionNew.contains(rentcarCollectionOldRentcar)) {
                    rentcarCollectionOldRentcar.setCId(null);
                    rentcarCollectionOldRentcar = em.merge(rentcarCollectionOldRentcar);
                }
            }
            for (Rentcar rentcarCollectionNewRentcar : rentcarCollectionNew) {
                if (!rentcarCollectionOld.contains(rentcarCollectionNewRentcar)) {
                    CarforRent oldCIdOfRentcarCollectionNewRentcar = rentcarCollectionNewRentcar.getCId();
                    rentcarCollectionNewRentcar.setCId(carforRent);
                    rentcarCollectionNewRentcar = em.merge(rentcarCollectionNewRentcar);
                    if (oldCIdOfRentcarCollectionNewRentcar != null && !oldCIdOfRentcarCollectionNewRentcar.equals(carforRent)) {
                        oldCIdOfRentcarCollectionNewRentcar.getRentcarCollection().remove(rentcarCollectionNewRentcar);
                        oldCIdOfRentcarCollectionNewRentcar = em.merge(oldCIdOfRentcarCollectionNewRentcar);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = carforRent.getCId();
                if (findCarforRent(id) == null) {
                    throw new NonexistentEntityException("The carforRent with id " + id + " no longer exists.");
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
            CarforRent carforRent;
            try {
                carforRent = em.getReference(CarforRent.class, id);
                carforRent.getCId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carforRent with id " + id + " no longer exists.", enfe);
            }
            Collection<Rentcar> rentcarCollection = carforRent.getRentcarCollection();
            for (Rentcar rentcarCollectionRentcar : rentcarCollection) {
                rentcarCollectionRentcar.setCId(null);
                rentcarCollectionRentcar = em.merge(rentcarCollectionRentcar);
            }
            em.remove(carforRent);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CarforRent> findCarforRentEntities() {
        return findCarforRentEntities(true, -1, -1);
    }

    public List<CarforRent> findCarforRentEntities(int maxResults, int firstResult) {
        return findCarforRentEntities(false, maxResults, firstResult);
    }

    private List<CarforRent> findCarforRentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CarforRent.class));
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

    public CarforRent findCarforRent(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CarforRent.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarforRentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CarforRent> rt = cq.from(CarforRent.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
