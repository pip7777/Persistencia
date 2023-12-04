package com.mycompany.mavenproject4;

import com.mycompany.mavenproject4.exceptions.NonexistentEntityException;
import com.mycompany.mavenproject4.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Sarah Delgado Martin
 */
public class ProyectoSedeJpaController implements Serializable {

    public ProyectoSedeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProyectoSede proyectoSede) throws PreexistingEntityException, Exception {
        if (proyectoSede.getProyectoSedePK() == null) {
            proyectoSede.setProyectoSedePK(new ProyectoSedePK());
        }
        proyectoSede.getProyectoSedePK().setIdSede(proyectoSede.getSede().getIdSede());
        proyectoSede.getProyectoSedePK().setIdProy(proyectoSede.getProyecto().getIdProy());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proyecto proyecto = proyectoSede.getProyecto();
            if (proyecto != null) {
                proyecto = em.getReference(proyecto.getClass(), proyecto.getIdProy());
                proyectoSede.setProyecto(proyecto);
            }
            Sede sede = proyectoSede.getSede();
            if (sede != null) {
                sede = em.getReference(sede.getClass(), sede.getIdSede());
                proyectoSede.setSede(sede);
            }
            em.persist(proyectoSede);
            if (proyecto != null) {
                proyecto.getProyectoSedeCollection().add(proyectoSede);
                proyecto = em.merge(proyecto);
            }
            if (sede != null) {
                sede.getProyectoSedeCollection().add(proyectoSede);
                sede = em.merge(sede);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProyectoSede(proyectoSede.getProyectoSedePK()) != null) {
                throw new PreexistingEntityException("ProyectoSede " + proyectoSede + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProyectoSede proyectoSede) throws NonexistentEntityException, Exception {
        proyectoSede.getProyectoSedePK().setIdSede(proyectoSede.getSede().getIdSede());
        proyectoSede.getProyectoSedePK().setIdProy(proyectoSede.getProyecto().getIdProy());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProyectoSede persistentProyectoSede = em.find(ProyectoSede.class, proyectoSede.getProyectoSedePK());
            Proyecto proyectoOld = persistentProyectoSede.getProyecto();
            Proyecto proyectoNew = proyectoSede.getProyecto();
            Sede sedeOld = persistentProyectoSede.getSede();
            Sede sedeNew = proyectoSede.getSede();
            if (proyectoNew != null) {
                proyectoNew = em.getReference(proyectoNew.getClass(), proyectoNew.getIdProy());
                proyectoSede.setProyecto(proyectoNew);
            }
            if (sedeNew != null) {
                sedeNew = em.getReference(sedeNew.getClass(), sedeNew.getIdSede());
                proyectoSede.setSede(sedeNew);
            }
            proyectoSede = em.merge(proyectoSede);
            if (proyectoOld != null && !proyectoOld.equals(proyectoNew)) {
                proyectoOld.getProyectoSedeCollection().remove(proyectoSede);
                proyectoOld = em.merge(proyectoOld);
            }
            if (proyectoNew != null && !proyectoNew.equals(proyectoOld)) {
                proyectoNew.getProyectoSedeCollection().add(proyectoSede);
                proyectoNew = em.merge(proyectoNew);
            }
            if (sedeOld != null && !sedeOld.equals(sedeNew)) {
                sedeOld.getProyectoSedeCollection().remove(proyectoSede);
                sedeOld = em.merge(sedeOld);
            }
            if (sedeNew != null && !sedeNew.equals(sedeOld)) {
                sedeNew.getProyectoSedeCollection().add(proyectoSede);
                sedeNew = em.merge(sedeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ProyectoSedePK id = proyectoSede.getProyectoSedePK();
                if (findProyectoSede(id) == null) {
                    throw new NonexistentEntityException("The proyectoSede with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ProyectoSedePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProyectoSede proyectoSede;
            try {
                proyectoSede = em.getReference(ProyectoSede.class, id);
                proyectoSede.getProyectoSedePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proyectoSede with id " + id + " no longer exists.", enfe);
            }
            Proyecto proyecto = proyectoSede.getProyecto();
            if (proyecto != null) {
                proyecto.getProyectoSedeCollection().remove(proyectoSede);
                proyecto = em.merge(proyecto);
            }
            Sede sede = proyectoSede.getSede();
            if (sede != null) {
                sede.getProyectoSedeCollection().remove(proyectoSede);
                sede = em.merge(sede);
            }
            em.remove(proyectoSede);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProyectoSede> findProyectoSedeEntities() {
        return findProyectoSedeEntities(true, -1, -1);
    }

    public List<ProyectoSede> findProyectoSedeEntities(int maxResults, int firstResult) {
        return findProyectoSedeEntities(false, maxResults, firstResult);
    }

    private List<ProyectoSede> findProyectoSedeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProyectoSede.class));
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

    public ProyectoSede findProyectoSede(ProyectoSedePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProyectoSede.class, id);
        } finally {
            em.close();
        }
    }

    public int getProyectoSedeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProyectoSede> rt = cq.from(ProyectoSede.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
