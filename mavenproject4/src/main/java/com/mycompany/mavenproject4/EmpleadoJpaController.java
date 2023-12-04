/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 * @author nuria
 */
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EmpleadoDatosProf empleadoDatosProf = empleado.getEmpleadoDatosProf();
            if (empleadoDatosProf != null) {
                empleadoDatosProf = em.getReference(empleadoDatosProf.getClass(), empleadoDatosProf.getDni());
                empleado.setEmpleadoDatosProf(empleadoDatosProf);
            }
            Departamento idDepto = empleado.getIdDepto();
            if (idDepto != null) {
                idDepto = em.getReference(idDepto.getClass(), idDepto.getIdDepto());
                empleado.setIdDepto(idDepto);
            }
            em.persist(empleado);
            if (empleadoDatosProf != null) {
                Empleado oldEmpleadoOfEmpleadoDatosProf = empleadoDatosProf.getEmpleado();
                if (oldEmpleadoOfEmpleadoDatosProf != null) {
                    oldEmpleadoOfEmpleadoDatosProf.setEmpleadoDatosProf(null);
                    oldEmpleadoOfEmpleadoDatosProf = em.merge(oldEmpleadoOfEmpleadoDatosProf);
                }
                empleadoDatosProf.setEmpleado(empleado);
                empleadoDatosProf = em.merge(empleadoDatosProf);
            }
            if (idDepto != null) {
                idDepto.getEmpleadoCollection().add(empleado);
                idDepto = em.merge(idDepto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpleado(empleado.getDni()) != null) {
                throw new PreexistingEntityException("Empleado " + empleado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleado empleado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getDni());
            EmpleadoDatosProf empleadoDatosProfOld = persistentEmpleado.getEmpleadoDatosProf();
            EmpleadoDatosProf empleadoDatosProfNew = empleado.getEmpleadoDatosProf();
            Departamento idDeptoOld = persistentEmpleado.getIdDepto();
            Departamento idDeptoNew = empleado.getIdDepto();
            List<String> illegalOrphanMessages = null;
            if (empleadoDatosProfOld != null && !empleadoDatosProfOld.equals(empleadoDatosProfNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain EmpleadoDatosProf " + empleadoDatosProfOld + " since its empleado field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (empleadoDatosProfNew != null) {
                empleadoDatosProfNew = em.getReference(empleadoDatosProfNew.getClass(), empleadoDatosProfNew.getDni());
                empleado.setEmpleadoDatosProf(empleadoDatosProfNew);
            }
            if (idDeptoNew != null) {
                idDeptoNew = em.getReference(idDeptoNew.getClass(), idDeptoNew.getIdDepto());
                empleado.setIdDepto(idDeptoNew);
            }
            empleado = em.merge(empleado);
            if (empleadoDatosProfNew != null && !empleadoDatosProfNew.equals(empleadoDatosProfOld)) {
                Empleado oldEmpleadoOfEmpleadoDatosProf = empleadoDatosProfNew.getEmpleado();
                if (oldEmpleadoOfEmpleadoDatosProf != null) {
                    oldEmpleadoOfEmpleadoDatosProf.setEmpleadoDatosProf(null);
                    oldEmpleadoOfEmpleadoDatosProf = em.merge(oldEmpleadoOfEmpleadoDatosProf);
                }
                empleadoDatosProfNew.setEmpleado(empleado);
                empleadoDatosProfNew = em.merge(empleadoDatosProfNew);
            }
            if (idDeptoOld != null && !idDeptoOld.equals(idDeptoNew)) {
                idDeptoOld.getEmpleadoCollection().remove(empleado);
                idDeptoOld = em.merge(idDeptoOld);
            }
            if (idDeptoNew != null && !idDeptoNew.equals(idDeptoOld)) {
                idDeptoNew.getEmpleadoCollection().add(empleado);
                idDeptoNew = em.merge(idDeptoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = empleado.getDni();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getDni();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            EmpleadoDatosProf empleadoDatosProfOrphanCheck = empleado.getEmpleadoDatosProf();
            if (empleadoDatosProfOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the EmpleadoDatosProf " + empleadoDatosProfOrphanCheck + " in its empleadoDatosProf field has a non-nullable empleado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Departamento idDepto = empleado.getIdDepto();
            if (idDepto != null) {
                idDepto.getEmpleadoCollection().remove(empleado);
                idDepto = em.merge(idDepto);
            }
            em.remove(empleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
