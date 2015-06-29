/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Model.UserRole;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Model.Users;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Administrator
 */
public class UserRoleJpaController implements Serializable {

    public UserRoleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserRole userRole) throws PreexistingEntityException, Exception {
        if (userRole.getUsersCollection() == null) {
            userRole.setUsersCollection(new ArrayList<Users>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Users> attachedUsersCollection = new ArrayList<Users>();
            for (Users usersCollectionUsersToAttach : userRole.getUsersCollection()) {
                usersCollectionUsersToAttach = em.getReference(usersCollectionUsersToAttach.getClass(), usersCollectionUsersToAttach.getUId());
                attachedUsersCollection.add(usersCollectionUsersToAttach);
            }
            userRole.setUsersCollection(attachedUsersCollection);
            em.persist(userRole);
            for (Users usersCollectionUsers : userRole.getUsersCollection()) {
                UserRole oldURoleOfUsersCollectionUsers = usersCollectionUsers.getURole();
                usersCollectionUsers.setURole(userRole);
                usersCollectionUsers = em.merge(usersCollectionUsers);
                if (oldURoleOfUsersCollectionUsers != null) {
                    oldURoleOfUsersCollectionUsers.getUsersCollection().remove(usersCollectionUsers);
                    oldURoleOfUsersCollectionUsers = em.merge(oldURoleOfUsersCollectionUsers);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUserRole(userRole.getUrId()) != null) {
                throw new PreexistingEntityException("UserRole " + userRole + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserRole userRole) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserRole persistentUserRole = em.find(UserRole.class, userRole.getUrId());
            Collection<Users> usersCollectionOld = persistentUserRole.getUsersCollection();
            Collection<Users> usersCollectionNew = userRole.getUsersCollection();
            Collection<Users> attachedUsersCollectionNew = new ArrayList<Users>();
            for (Users usersCollectionNewUsersToAttach : usersCollectionNew) {
                usersCollectionNewUsersToAttach = em.getReference(usersCollectionNewUsersToAttach.getClass(), usersCollectionNewUsersToAttach.getUId());
                attachedUsersCollectionNew.add(usersCollectionNewUsersToAttach);
            }
            usersCollectionNew = attachedUsersCollectionNew;
            userRole.setUsersCollection(usersCollectionNew);
            userRole = em.merge(userRole);
            for (Users usersCollectionOldUsers : usersCollectionOld) {
                if (!usersCollectionNew.contains(usersCollectionOldUsers)) {
                    usersCollectionOldUsers.setURole(null);
                    usersCollectionOldUsers = em.merge(usersCollectionOldUsers);
                }
            }
            for (Users usersCollectionNewUsers : usersCollectionNew) {
                if (!usersCollectionOld.contains(usersCollectionNewUsers)) {
                    UserRole oldURoleOfUsersCollectionNewUsers = usersCollectionNewUsers.getURole();
                    usersCollectionNewUsers.setURole(userRole);
                    usersCollectionNewUsers = em.merge(usersCollectionNewUsers);
                    if (oldURoleOfUsersCollectionNewUsers != null && !oldURoleOfUsersCollectionNewUsers.equals(userRole)) {
                        oldURoleOfUsersCollectionNewUsers.getUsersCollection().remove(usersCollectionNewUsers);
                        oldURoleOfUsersCollectionNewUsers = em.merge(oldURoleOfUsersCollectionNewUsers);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = userRole.getUrId();
                if (findUserRole(id) == null) {
                    throw new NonexistentEntityException("The userRole with id " + id + " no longer exists.");
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
            UserRole userRole;
            try {
                userRole = em.getReference(UserRole.class, id);
                userRole.getUrId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userRole with id " + id + " no longer exists.", enfe);
            }
            Collection<Users> usersCollection = userRole.getUsersCollection();
            for (Users usersCollectionUsers : usersCollection) {
                usersCollectionUsers.setURole(null);
                usersCollectionUsers = em.merge(usersCollectionUsers);
            }
            em.remove(userRole);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UserRole> findUserRoleEntities() {
        return findUserRoleEntities(true, -1, -1);
    }

    public List<UserRole> findUserRoleEntities(int maxResults, int firstResult) {
        return findUserRoleEntities(false, maxResults, firstResult);
    }

    private List<UserRole> findUserRoleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserRole.class));
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

    public UserRole findUserRole(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserRole.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserRoleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserRole> rt = cq.from(UserRole.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
