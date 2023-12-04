package com.mycompany.mavenproject4;

import com.mycompany.mavenproject4.exceptions.IllegalOrphanException;
import com.mycompany.mavenproject4.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Sarah Delgado Martin
 */
public class SedeJpaController implements Serializable {

    public SedeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sede sede) {
        if (sede.getProyectoSedeCollection() == null) {
            sede.setProyectoSedeCollection(new ArrayList<ProyectoSede>());
        }
        if (sede.getDepartamentoCollection() == null) {
            sede.setDepartamentoCollection(new ArrayList<Departamento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ProyectoSede> attachedProyectoSedeCollection = new ArrayList<ProyectoSede>();
            for (ProyectoSede proyectoSedeCollectionProyectoSedeToAttach : sede.getProyectoSedeCollection()) {
                proyectoSedeCollectionProyectoSedeToAttach = em.getReference(proyectoSedeCollectionProyectoSedeToAttach.getClass(), proyectoSedeCollectionProyectoSedeToAttach.getProyectoSedePK());
                attachedProyectoSedeCollection.add(proyectoSedeCollectionProyectoSedeToAttach);
            }
            sede.setProyectoSedeCollection(attachedProyectoSedeCollection);
            Collection<Departamento> attachedDepartamentoCollection = new ArrayList<Departamento>();
            for (Departamento departamentoCollectionDepartamentoToAttach : sede.getDepartamentoCollection()) {
                departamentoCollectionDepartamentoToAttach = em.getReference(departamentoCollectionDepartamentoToAttach.getClass(), departamentoCollectionDepartamentoToAttach.getIdDepto());
                attachedDepartamentoCollection.add(departamentoCollectionDepartamentoToAttach);
            }
            sede.setDepartamentoCollection(attachedDepartamentoCollection);
            em.persist(sede);
            for (ProyectoSede proyectoSedeCollectionProyectoSede : sede.getProyectoSedeCollection()) {
                Sede oldSedeOfProyectoSedeCollectionProyectoSede = proyectoSedeCollectionProyectoSede.getSede();
                proyectoSedeCollectionProyectoSede.setSede(sede);
                proyectoSedeCollectionProyectoSede = em.merge(proyectoSedeCollectionProyectoSede);
                if (oldSedeOfProyectoSedeCollectionProyectoSede != null) {
                    oldSedeOfProyectoSedeCollectionProyectoSede.getProyectoSedeCollection().remove(proyectoSedeCollectionProyectoSede);
                    oldSedeOfProyectoSedeCollectionProyectoSede = em.merge(oldSedeOfProyectoSedeCollectionProyectoSede);
                }
            }
            for (Departamento departamentoCollectionDepartamento : sede.getDepartamentoCollection()) {
                Sede oldIdSedeOfDepartamentoCollectionDepartamento = departamentoCollectionDepartamento.getIdSede();
                departamentoCollectionDepartamento.setIdSede(sede);
                departamentoCollectionDepartamento = em.merge(departamentoCollectionDepartamento);
                if (oldIdSedeOfDepartamentoCollectionDepartamento != null) {
                    oldIdSedeOfDepartamentoCollectionDepartamento.getDepartamentoCollection().remove(departamentoCollectionDepartamento);
                    oldIdSedeOfDepartamentoCollectionDepartamento = em.merge(oldIdSedeOfDepartamentoCollectionDepartamento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sede sede) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sede persistentSede = em.find(Sede.class, sede.getIdSede());
            Collection<ProyectoSede> proyectoSedeCollectionOld = persistentSede.getProyectoSedeCollection();
            Collection<ProyectoSede> proyectoSedeCollectionNew = sede.getProyectoSedeCollection();
            Collection<Departamento> departamentoCollectionOld = persistentSede.getDepartamentoCollection();
            Collection<Departamento> departamentoCollectionNew = sede.getDepartamentoCollection();
            List<String> illegalOrphanMessages = null;
            for (ProyectoSede proyectoSedeCollectionOldProyectoSede : proyectoSedeCollectionOld) {
                if (!proyectoSedeCollectionNew.contains(proyectoSedeCollectionOldProyectoSede)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProyectoSede " + proyectoSedeCollectionOldProyectoSede + " since its sede field is not nullable.");
                }
            }
            for (Departamento departamentoCollectionOldDepartamento : departamentoCollectionOld) {
                if (!departamentoCollectionNew.contains(departamentoCollectionOldDepartamento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Departamento " + departamentoCollectionOldDepartamento + " since its idSede field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ProyectoSede> attachedProyectoSedeCollectionNew = new ArrayList<ProyectoSede>();
            for (ProyectoSede proyectoSedeCollectionNewProyectoSedeToAttach : proyectoSedeCollectionNew) {
                proyectoSedeCollectionNewProyectoSedeToAttach = em.getReference(proyectoSedeCollectionNewProyectoSedeToAttach.getClass(), proyectoSedeCollectionNewProyectoSedeToAttach.getProyectoSedePK());
                attachedProyectoSedeCollectionNew.add(proyectoSedeCollectionNewProyectoSedeToAttach);
            }
            proyectoSedeCollectionNew = attachedProyectoSedeCollectionNew;
            sede.setProyectoSedeCollection(proyectoSedeCollectionNew);
            Collection<Departamento> attachedDepartamentoCollectionNew = new ArrayList<Departamento>();
            for (Departamento departamentoCollectionNewDepartamentoToAttach : departamentoCollectionNew) {
                departamentoCollectionNewDepartamentoToAttach = em.getReference(departamentoCollectionNewDepartamentoToAttach.getClass(), departamentoCollectionNewDepartamentoToAttach.getIdDepto());
                attachedDepartamentoCollectionNew.add(departamentoCollectionNewDepartamentoToAttach);
            }
            departamentoCollectionNew = attachedDepartamentoCollectionNew;
            sede.setDepartamentoCollection(departamentoCollectionNew);
            sede = em.merge(sede);
            for (ProyectoSede proyectoSedeCollectionNewProyectoSede : proyectoSedeCollectionNew) {
                if (!proyectoSedeCollectionOld.contains(proyectoSedeCollectionNewProyectoSede)) {
                    Sede oldSedeOfProyectoSedeCollectionNewProyectoSede = proyectoSedeCollectionNewProyectoSede.getSede();
                    proyectoSedeCollectionNewProyectoSede.setSede(sede);
                    proyectoSedeCollectionNewProyectoSede = em.merge(proyectoSedeCollectionNewProyectoSede);
                    if (oldSedeOfProyectoSedeCollectionNewProyectoSede != null && !oldSedeOfProyectoSedeCollectionNewProyectoSede.equals(sede)) {
                        oldSedeOfProyectoSedeCollectionNewProyectoSede.getProyectoSedeCollection().remove(proyectoSedeCollectionNewProyectoSede);
                        oldSedeOfProyectoSedeCollectionNewProyectoSede = em.merge(oldSedeOfProyectoSedeCollectionNewProyectoSede);
                    }
                }
            }
            for (Departamento departamentoCollectionNewDepartamento : departamentoCollectionNew) {
                if (!departamentoCollectionOld.contains(departamentoCollectionNewDepartamento)) {
                    Sede oldIdSedeOfDepartamentoCollectionNewDepartamento = departamentoCollectionNewDepartamento.getIdSede();
                    departamentoCollectionNewDepartamento.setIdSede(sede);
                    departamentoCollectionNewDepartamento = em.merge(departamentoCollectionNewDepartamento);
                    if (oldIdSedeOfDepartamentoCollectionNewDepartamento != null && !oldIdSedeOfDepartamentoCollectionNewDepartamento.equals(sede)) {
                        oldIdSedeOfDepartamentoCollectionNewDepartamento.getDepartamentoCollection().remove(departamentoCollectionNewDepartamento);
                        oldIdSedeOfDepartamentoCollectionNewDepartamento = em.merge(oldIdSedeOfDepartamentoCollectionNewDepartamento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sede.getIdSede();
                if (findSede(id) == null) {
                    throw new NonexistentEntityException("The sede with id " + id + " no longer exists.");
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
            Sede sede;
            try {
                sede = em.getReference(Sede.class, id);
                sede.getIdSede();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sede with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ProyectoSede> proyectoSedeCollectionOrphanCheck = sede.getProyectoSedeCollection();
            for (ProyectoSede proyectoSedeCollectionOrphanCheckProyectoSede : proyectoSedeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Sede (" + sede + ") cannot be destroyed since the ProyectoSede " + proyectoSedeCollectionOrphanCheckProyectoSede + " in its proyectoSedeCollection field has a non-nullable sede field.");
            }
            Collection<Departamento> departamentoCollectionOrphanCheck = sede.getDepartamentoCollection();
            for (Departamento departamentoCollectionOrphanCheckDepartamento : departamentoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Sede (" + sede + ") cannot be destroyed since the Departamento " + departamentoCollectionOrphanCheckDepartamento + " in its departamentoCollection field has a non-nullable idSede field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(sede);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sede> findSedeEntities() {
        return findSedeEntities(true, -1, -1);
    }

    public List<Sede> findSedeEntities(int maxResults, int firstResult) {
        return findSedeEntities(false, maxResults, firstResult);
    }

    private List<Sede> findSedeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sede.class));
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

    public Sede findSede(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sede.class, id);
        } finally {
            em.close();
        }
    }

    public int getSedeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sede> rt = cq.from(Sede.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
