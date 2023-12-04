package com.mycompany.mavenproject4;

import com.mycompany.mavenproject4.exceptions.IllegalOrphanException;
import com.mycompany.mavenproject4.exceptions.NonexistentEntityException;
import com.mycompany.mavenproject4.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Sarah Delgado Martin
 */
public class EmpleadoDatosProfJpaController implements Serializable {

    public EmpleadoDatosProfJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EmpleadoDatosProf empleadoDatosProf) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Empleado empleadoOrphanCheck = empleadoDatosProf.getEmpleado();
        if (empleadoOrphanCheck != null) {
            EmpleadoDatosProf oldEmpleadoDatosProfOfEmpleado = empleadoOrphanCheck.getEmpleadoDatosProf();
            if (oldEmpleadoDatosProfOfEmpleado != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Empleado " + empleadoOrphanCheck + " already has an item of type EmpleadoDatosProf whose empleado column cannot be null. Please make another selection for the empleado field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado empleado = empleadoDatosProf.getEmpleado();
            if (empleado != null) {
                empleado = em.getReference(empleado.getClass(), empleado.getDni());
                empleadoDatosProf.setEmpleado(empleado);
            }
            em.persist(empleadoDatosProf);
            if (empleado != null) {
                empleado.setEmpleadoDatosProf(empleadoDatosProf);
                empleado = em.merge(empleado);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpleadoDatosProf(empleadoDatosProf.getDni()) != null) {
                throw new PreexistingEntityException("EmpleadoDatosProf " + empleadoDatosProf + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EmpleadoDatosProf empleadoDatosProf) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EmpleadoDatosProf persistentEmpleadoDatosProf = em.find(EmpleadoDatosProf.class, empleadoDatosProf.getDni());
            Empleado empleadoOld = persistentEmpleadoDatosProf.getEmpleado();
            Empleado empleadoNew = empleadoDatosProf.getEmpleado();
            List<String> illegalOrphanMessages = null;
            if (empleadoNew != null && !empleadoNew.equals(empleadoOld)) {
                EmpleadoDatosProf oldEmpleadoDatosProfOfEmpleado = empleadoNew.getEmpleadoDatosProf();
                if (oldEmpleadoDatosProfOfEmpleado != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Empleado " + empleadoNew + " already has an item of type EmpleadoDatosProf whose empleado column cannot be null. Please make another selection for the empleado field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (empleadoNew != null) {
                empleadoNew = em.getReference(empleadoNew.getClass(), empleadoNew.getDni());
                empleadoDatosProf.setEmpleado(empleadoNew);
            }
            empleadoDatosProf = em.merge(empleadoDatosProf);
            if (empleadoOld != null && !empleadoOld.equals(empleadoNew)) {
                empleadoOld.setEmpleadoDatosProf(null);
                empleadoOld = em.merge(empleadoOld);
            }
            if (empleadoNew != null && !empleadoNew.equals(empleadoOld)) {
                empleadoNew.setEmpleadoDatosProf(empleadoDatosProf);
                empleadoNew = em.merge(empleadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = empleadoDatosProf.getDni();
                if (findEmpleadoDatosProf(id) == null) {
                    throw new NonexistentEntityException("The empleadoDatosProf with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EmpleadoDatosProf empleadoDatosProf;
            try {
                empleadoDatosProf = em.getReference(EmpleadoDatosProf.class, id);
                empleadoDatosProf.getDni();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleadoDatosProf with id " + id + " no longer exists.", enfe);
            }
            Empleado empleado = empleadoDatosProf.getEmpleado();
            if (empleado != null) {
                empleado.setEmpleadoDatosProf(null);
                empleado = em.merge(empleado);
            }
            em.remove(empleadoDatosProf);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EmpleadoDatosProf> findEmpleadoDatosProfEntities() {
        return findEmpleadoDatosProfEntities(true, -1, -1);
    }

    public List<EmpleadoDatosProf> findEmpleadoDatosProfEntities(int maxResults, int firstResult) {
        return findEmpleadoDatosProfEntities(false, maxResults, firstResult);
    }

    private List<EmpleadoDatosProf> findEmpleadoDatosProfEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EmpleadoDatosProf.class));
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

    public EmpleadoDatosProf findEmpleadoDatosProf(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EmpleadoDatosProf.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoDatosProfCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EmpleadoDatosProf> rt = cq.from(EmpleadoDatosProf.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
