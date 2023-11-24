/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Logica.Departamento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Logica.Sede;
import Logica.Empleado;
import com.mycompany.mavenproject4.exceptions.IllegalOrphanException;
import com.mycompany.mavenproject4.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo
 */
public class DepartamentoJpaController implements Serializable {

    public DepartamentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departamento departamento) {
        if (departamento.getEmpleadoCollection() == null) {
            departamento.setEmpleadoCollection(new ArrayList<Empleado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sede idSede = departamento.getIdSede();
            if (idSede != null) {
                idSede = em.getReference(idSede.getClass(), idSede.getIdSede());
                departamento.setIdSede(idSede);
            }
            Collection<Empleado> attachedEmpleadoCollection = new ArrayList<Empleado>();
            for (Empleado empleadoCollectionEmpleado_1ToAttach : departamento.getEmpleadoCollection()) {
                empleadoCollectionEmpleado_1ToAttach = em.getReference(empleadoCollectionEmpleado_1ToAttach.getClass(), empleadoCollectionEmpleado_1ToAttach.getDni());
                attachedEmpleadoCollection.add(empleadoCollectionEmpleado_1ToAttach);
            }
            departamento.setEmpleadoCollection(attachedEmpleadoCollection);
            em.persist(departamento);
            if (idSede != null) {
                idSede.getDepartamentoCollection().add(departamento);
                idSede = em.merge(idSede);
            }
            for (Empleado empleadoCollectionEmpleado_1 : departamento.getEmpleadoCollection()) {
                Departamento oldIdDeptoOfEmpleadoCollectionEmpleado_1 = empleadoCollectionEmpleado_1.getIdDepto();
                empleadoCollectionEmpleado_1.setIdDepto(departamento);
                empleadoCollectionEmpleado_1 = em.merge(empleadoCollectionEmpleado_1);
                if (oldIdDeptoOfEmpleadoCollectionEmpleado_1 != null) {
                    oldIdDeptoOfEmpleadoCollectionEmpleado_1.getEmpleadoCollection().remove(empleadoCollectionEmpleado_1);
                    oldIdDeptoOfEmpleadoCollectionEmpleado_1 = em.merge(oldIdDeptoOfEmpleadoCollectionEmpleado_1);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Departamento departamento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento persistentDepartamento = em.find(Departamento.class, departamento.getIdDepto());
            Sede idSedeOld = persistentDepartamento.getIdSede();
            Sede idSedeNew = departamento.getIdSede();
            Collection<Empleado> empleadoCollectionOld = persistentDepartamento.getEmpleadoCollection();
            Collection<Empleado> empleadoCollectionNew = departamento.getEmpleadoCollection();
            List<String> illegalOrphanMessages = null;
            for (Empleado empleadoCollectionOldEmpleado_1 : empleadoCollectionOld) {
                if (!empleadoCollectionNew.contains(empleadoCollectionOldEmpleado_1)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleado_1 " + empleadoCollectionOldEmpleado_1 + " since its idDepto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idSedeNew != null) {
                idSedeNew = em.getReference(idSedeNew.getClass(), idSedeNew.getIdSede());
                departamento.setIdSede(idSedeNew);
            }
            Collection<Empleado> attachedEmpleadoCollectionNew = new ArrayList<Empleado>();
            for (Empleado empleadoCollectionNewEmpleado_1ToAttach : empleadoCollectionNew) {
                empleadoCollectionNewEmpleado_1ToAttach = em.getReference(empleadoCollectionNewEmpleado_1ToAttach.getClass(), empleadoCollectionNewEmpleado_1ToAttach.getDni());
                attachedEmpleadoCollectionNew.add(empleadoCollectionNewEmpleado_1ToAttach);
            }
            empleadoCollectionNew = attachedEmpleadoCollectionNew;
            departamento.setEmpleadoCollection(empleadoCollectionNew);
            departamento = em.merge(departamento);
            if (idSedeOld != null && !idSedeOld.equals(idSedeNew)) {
                idSedeOld.getDepartamentoCollection().remove(departamento);
                idSedeOld = em.merge(idSedeOld);
            }
            if (idSedeNew != null && !idSedeNew.equals(idSedeOld)) {
                idSedeNew.getDepartamentoCollection().add(departamento);
                idSedeNew = em.merge(idSedeNew);
            }
            for (Empleado empleadoCollectionNewEmpleado_1 : empleadoCollectionNew) {
                if (!empleadoCollectionOld.contains(empleadoCollectionNewEmpleado_1)) {
                    Departamento oldIdDeptoOfEmpleadoCollectionNewEmpleado_1 = empleadoCollectionNewEmpleado_1.getIdDepto();
                    empleadoCollectionNewEmpleado_1.setIdDepto(departamento);
                    empleadoCollectionNewEmpleado_1 = em.merge(empleadoCollectionNewEmpleado_1);
                    if (oldIdDeptoOfEmpleadoCollectionNewEmpleado_1 != null && !oldIdDeptoOfEmpleadoCollectionNewEmpleado_1.equals(departamento)) {
                        oldIdDeptoOfEmpleadoCollectionNewEmpleado_1.getEmpleadoCollection().remove(empleadoCollectionNewEmpleado_1);
                        oldIdDeptoOfEmpleadoCollectionNewEmpleado_1 = em.merge(oldIdDeptoOfEmpleadoCollectionNewEmpleado_1);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = departamento.getIdDepto();
                if (findDepartamento(id) == null) {
                    throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento departamento;
            try {
                departamento = em.getReference(Departamento.class, id);
                departamento.getIdDepto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Empleado> empleadoCollectionOrphanCheck = departamento.getEmpleadoCollection();
            for (Empleado empleadoCollectionOrphanCheckEmpleado_1 : empleadoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Empleado_1 " + empleadoCollectionOrphanCheckEmpleado_1 + " in its empleadoCollection field has a non-nullable idDepto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Sede idSede = departamento.getIdSede();
            if (idSede != null) {
                idSede.getDepartamentoCollection().remove(departamento);
                idSede = em.merge(idSede);
            }
            em.remove(departamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Departamento> findDepartamentoEntities() {
        return findDepartamentoEntities(true, -1, -1);
    }

    public List<Departamento> findDepartamentoEntities(int maxResults, int firstResult) {
        return findDepartamentoEntities(false, maxResults, firstResult);
    }

    private List<Departamento> findDepartamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departamento.class));
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

    public Departamento findDepartamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departamento> rt = cq.from(Departamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
