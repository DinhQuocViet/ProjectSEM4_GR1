/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Entity.CarforRent;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.Rentcar;
import java.util.ArrayList;
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
        if (carforRent.getRentcarList() == null) {
            carforRent.setRentcarList(new ArrayList<Rentcar>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Rentcar> attachedRentcarList = new ArrayList<Rentcar>();
            for (Rentcar rentcarListRentcarToAttach : carforRent.getRentcarList()) {
                rentcarListRentcarToAttach = em.getReference(rentcarListRentcarToAttach.getClass(), rentcarListRentcarToAttach.getRId());
                attachedRentcarList.add(rentcarListRentcarToAttach);
            }
            carforRent.setRentcarList(attachedRentcarList);
            em.persist(carforRent);
            for (Rentcar rentcarListRentcar : carforRent.getRentcarList()) {
                CarforRent oldCIdOfRentcarListRentcar = rentcarListRentcar.getCId();
                rentcarListRentcar.setCId(carforRent);
                rentcarListRentcar = em.merge(rentcarListRentcar);
                if (oldCIdOfRentcarListRentcar != null) {
                    oldCIdOfRentcarListRentcar.getRentcarList().remove(rentcarListRentcar);
                    oldCIdOfRentcarListRentcar = em.merge(oldCIdOfRentcarListRentcar);
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
            List<Rentcar> rentcarListOld = persistentCarforRent.getRentcarList();
            List<Rentcar> rentcarListNew = carforRent.getRentcarList();
            List<Rentcar> attachedRentcarListNew = new ArrayList<Rentcar>();
            for (Rentcar rentcarListNewRentcarToAttach : rentcarListNew) {
                rentcarListNewRentcarToAttach = em.getReference(rentcarListNewRentcarToAttach.getClass(), rentcarListNewRentcarToAttach.getRId());
                attachedRentcarListNew.add(rentcarListNewRentcarToAttach);
            }
            rentcarListNew = attachedRentcarListNew;
            carforRent.setRentcarList(rentcarListNew);
            carforRent = em.merge(carforRent);
            for (Rentcar rentcarListOldRentcar : rentcarListOld) {
                if (!rentcarListNew.contains(rentcarListOldRentcar)) {
                    rentcarListOldRentcar.setCId(null);
                    rentcarListOldRentcar = em.merge(rentcarListOldRentcar);
                }
            }
            for (Rentcar rentcarListNewRentcar : rentcarListNew) {
                if (!rentcarListOld.contains(rentcarListNewRentcar)) {
                    CarforRent oldCIdOfRentcarListNewRentcar = rentcarListNewRentcar.getCId();
                    rentcarListNewRentcar.setCId(carforRent);
                    rentcarListNewRentcar = em.merge(rentcarListNewRentcar);
                    if (oldCIdOfRentcarListNewRentcar != null && !oldCIdOfRentcarListNewRentcar.equals(carforRent)) {
                        oldCIdOfRentcarListNewRentcar.getRentcarList().remove(rentcarListNewRentcar);
                        oldCIdOfRentcarListNewRentcar = em.merge(oldCIdOfRentcarListNewRentcar);
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
            List<Rentcar> rentcarList = carforRent.getRentcarList();
            for (Rentcar rentcarListRentcar : rentcarList) {
                rentcarListRentcar.setCId(null);
                rentcarListRentcar = em.merge(rentcarListRentcar);
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
