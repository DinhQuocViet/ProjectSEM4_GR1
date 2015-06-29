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
import Model.Users;
import Model.CarforRent;
import Model.Rentcar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Administrator
 */
public class RentcarJpaController implements Serializable {

    public RentcarJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rentcar rentcar) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users UId = rentcar.getUId();
            if (UId != null) {
                UId = em.getReference(UId.getClass(), UId.getUId());
                rentcar.setUId(UId);
            }
            CarforRent CId = rentcar.getCId();
            if (CId != null) {
                CId = em.getReference(CId.getClass(), CId.getCId());
                rentcar.setCId(CId);
            }
            em.persist(rentcar);
            if (UId != null) {
                UId.getRentcarCollection().add(rentcar);
                UId = em.merge(UId);
            }
            if (CId != null) {
                CId.getRentcarCollection().add(rentcar);
                CId = em.merge(CId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRentcar(rentcar.getRId()) != null) {
                throw new PreexistingEntityException("Rentcar " + rentcar + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rentcar rentcar) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rentcar persistentRentcar = em.find(Rentcar.class, rentcar.getRId());
            Users UIdOld = persistentRentcar.getUId();
            Users UIdNew = rentcar.getUId();
            CarforRent CIdOld = persistentRentcar.getCId();
            CarforRent CIdNew = rentcar.getCId();
            if (UIdNew != null) {
                UIdNew = em.getReference(UIdNew.getClass(), UIdNew.getUId());
                rentcar.setUId(UIdNew);
            }
            if (CIdNew != null) {
                CIdNew = em.getReference(CIdNew.getClass(), CIdNew.getCId());
                rentcar.setCId(CIdNew);
            }
            rentcar = em.merge(rentcar);
            if (UIdOld != null && !UIdOld.equals(UIdNew)) {
                UIdOld.getRentcarCollection().remove(rentcar);
                UIdOld = em.merge(UIdOld);
            }
            if (UIdNew != null && !UIdNew.equals(UIdOld)) {
                UIdNew.getRentcarCollection().add(rentcar);
                UIdNew = em.merge(UIdNew);
            }
            if (CIdOld != null && !CIdOld.equals(CIdNew)) {
                CIdOld.getRentcarCollection().remove(rentcar);
                CIdOld = em.merge(CIdOld);
            }
            if (CIdNew != null && !CIdNew.equals(CIdOld)) {
                CIdNew.getRentcarCollection().add(rentcar);
                CIdNew = em.merge(CIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rentcar.getRId();
                if (findRentcar(id) == null) {
                    throw new NonexistentEntityException("The rentcar with id " + id + " no longer exists.");
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
            Rentcar rentcar;
            try {
                rentcar = em.getReference(Rentcar.class, id);
                rentcar.getRId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rentcar with id " + id + " no longer exists.", enfe);
            }
            Users UId = rentcar.getUId();
            if (UId != null) {
                UId.getRentcarCollection().remove(rentcar);
                UId = em.merge(UId);
            }
            CarforRent CId = rentcar.getCId();
            if (CId != null) {
                CId.getRentcarCollection().remove(rentcar);
                CId = em.merge(CId);
            }
            em.remove(rentcar);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rentcar> findRentcarEntities() {
        return findRentcarEntities(true, -1, -1);
    }

    public List<Rentcar> findRentcarEntities(int maxResults, int firstResult) {
        return findRentcarEntities(false, maxResults, firstResult);
    }

    private List<Rentcar> findRentcarEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rentcar.class));
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

    public Rentcar findRentcar(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rentcar.class, id);
        } finally {
            em.close();
        }
    }

    public int getRentcarCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rentcar> rt = cq.from(Rentcar.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
