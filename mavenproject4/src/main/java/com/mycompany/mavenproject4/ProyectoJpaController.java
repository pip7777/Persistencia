/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 * @author administrador
 */
public class ProyectoJpaController implements Serializable {

    public ProyectoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proyecto proyecto) {
        if (proyecto.getProyectoSedeCollection() == null) {
            proyecto.setProyectoSedeCollection(new ArrayList<ProyectoSede>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ProyectoSede> attachedProyectoSedeCollection = new ArrayList<ProyectoSede>();
            for (ProyectoSede proyectoSedeCollectionProyectoSedeToAttach : proyecto.getProyectoSedeCollection()) {
                proyectoSedeCollectionProyectoSedeToAttach = em.getReference(proyectoSedeCollectionProyectoSedeToAttach.getClass(), proyectoSedeCollectionProyectoSedeToAttach.getProyectoSedePK());
                attachedProyectoSedeCollection.add(proyectoSedeCollectionProyectoSedeToAttach);
            }
            proyecto.setProyectoSedeCollection(attachedProyectoSedeCollection);
            em.persist(proyecto);
            for (ProyectoSede proyectoSedeCollectionProyectoSede : proyecto.getProyectoSedeCollection()) {
                Proyecto oldProyectoOfProyectoSedeCollectionProyectoSede = proyectoSedeCollectionProyectoSede.getProyecto();
                proyectoSedeCollectionProyectoSede.setProyecto(proyecto);
                proyectoSedeCollectionProyectoSede = em.merge(proyectoSedeCollectionProyectoSede);
                if (oldProyectoOfProyectoSedeCollectionProyectoSede != null) {
                    oldProyectoOfProyectoSedeCollectionProyectoSede.getProyectoSedeCollection().remove(proyectoSedeCollectionProyectoSede);
                    oldProyectoOfProyectoSedeCollectionProyectoSede = em.merge(oldProyectoOfProyectoSedeCollectionProyectoSede);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proyecto proyecto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proyecto persistentProyecto = em.find(Proyecto.class, proyecto.getIdProy());
            Collection<ProyectoSede> proyectoSedeCollectionOld = persistentProyecto.getProyectoSedeCollection();
            Collection<ProyectoSede> proyectoSedeCollectionNew = proyecto.getProyectoSedeCollection();
            List<String> illegalOrphanMessages = null;
            for (ProyectoSede proyectoSedeCollectionOldProyectoSede : proyectoSedeCollectionOld) {
                if (!proyectoSedeCollectionNew.contains(proyectoSedeCollectionOldProyectoSede)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProyectoSede " + proyectoSedeCollectionOldProyectoSede + " since its proyecto field is not nullable.");
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
            proyecto.setProyectoSedeCollection(proyectoSedeCollectionNew);
            proyecto = em.merge(proyecto);
            for (ProyectoSede proyectoSedeCollectionNewProyectoSede : proyectoSedeCollectionNew) {
                if (!proyectoSedeCollectionOld.contains(proyectoSedeCollectionNewProyectoSede)) {
                    Proyecto oldProyectoOfProyectoSedeCollectionNewProyectoSede = proyectoSedeCollectionNewProyectoSede.getProyecto();
                    proyectoSedeCollectionNewProyectoSede.setProyecto(proyecto);
                    proyectoSedeCollectionNewProyectoSede = em.merge(proyectoSedeCollectionNewProyectoSede);
                    if (oldProyectoOfProyectoSedeCollectionNewProyectoSede != null && !oldProyectoOfProyectoSedeCollectionNewProyectoSede.equals(proyecto)) {
                        oldProyectoOfProyectoSedeCollectionNewProyectoSede.getProyectoSedeCollection().remove(proyectoSedeCollectionNewProyectoSede);
                        oldProyectoOfProyectoSedeCollectionNewProyectoSede = em.merge(oldProyectoOfProyectoSedeCollectionNewProyectoSede);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = proyecto.getIdProy();
                if (findProyecto(id) == null) {
                    throw new NonexistentEntityException("The proyecto with id " + id + " no longer exists.");
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
            Proyecto proyecto;
            try {
                proyecto = em.getReference(Proyecto.class, id);
                proyecto.getIdProy();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proyecto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ProyectoSede> proyectoSedeCollectionOrphanCheck = proyecto.getProyectoSedeCollection();
            for (ProyectoSede proyectoSedeCollectionOrphanCheckProyectoSede : proyectoSedeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proyecto (" + proyecto + ") cannot be destroyed since the ProyectoSede " + proyectoSedeCollectionOrphanCheckProyectoSede + " in its proyectoSedeCollection field has a non-nullable proyecto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(proyecto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proyecto> findProyectoEntities() {
        return findProyectoEntities(true, -1, -1);
    }

    public List<Proyecto> findProyectoEntities(int maxResults, int firstResult) {
        return findProyectoEntities(false, maxResults, firstResult);
    }

    private List<Proyecto> findProyectoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proyecto.class));
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

    public Proyecto findProyecto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proyecto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProyectoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proyecto> rt = cq.from(Proyecto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
