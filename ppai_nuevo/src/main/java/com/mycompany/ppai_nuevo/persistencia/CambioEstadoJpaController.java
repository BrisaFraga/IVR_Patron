/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ppai_nuevo.persistencia;

import com.mycompany.ppai_nuevo.persistencia.exceptions.NonexistentEntityException;
import entidades.CambioEstado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Llamada;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Brisa
 */
public class CambioEstadoJpaController implements Serializable {

    public CambioEstadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public CambioEstadoJpaController() {
        emf = Persistence.createEntityManagerFactory("encuestaJPAPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CambioEstado cambioEstado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Llamada llamada = cambioEstado.getLlamada();
            if (llamada != null) {
                llamada = em.getReference(llamada.getClass(), llamada.getId());
                cambioEstado.setLlamada(llamada);
            }
            em.persist(cambioEstado);
            if (llamada != null) {
                llamada.getCambiosEstados().add(cambioEstado);
                llamada = em.merge(llamada);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CambioEstado cambioEstado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CambioEstado persistentCambioEstado = em.find(CambioEstado.class, cambioEstado.getId());
            Llamada llamadaOld = persistentCambioEstado.getLlamada();
            Llamada llamadaNew = cambioEstado.getLlamada();
            if (llamadaNew != null) {
                llamadaNew = em.getReference(llamadaNew.getClass(), llamadaNew.getId());
                cambioEstado.setLlamada(llamadaNew);
            }
            cambioEstado = em.merge(cambioEstado);
            if (llamadaOld != null && !llamadaOld.equals(llamadaNew)) {
                llamadaOld.getCambiosEstados().remove(cambioEstado);
                llamadaOld = em.merge(llamadaOld);
            }
            if (llamadaNew != null && !llamadaNew.equals(llamadaOld)) {
                llamadaNew.getCambiosEstados().add(cambioEstado);
                llamadaNew = em.merge(llamadaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cambioEstado.getId();
                if (findCambioEstado(id) == null) {
                    throw new NonexistentEntityException("The cambioEstado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CambioEstado cambioEstado;
            try {
                cambioEstado = em.getReference(CambioEstado.class, id);
                cambioEstado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cambioEstado with id " + id + " no longer exists.", enfe);
            }
            Llamada llamada = cambioEstado.getLlamada();
            if (llamada != null) {
                llamada.getCambiosEstados().remove(cambioEstado);
                llamada = em.merge(llamada);
            }
            em.remove(cambioEstado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CambioEstado> findCambioEstadoEntities() {
        return findCambioEstadoEntities(true, -1, -1);
    }

    public List<CambioEstado> findCambioEstadoEntities(int maxResults, int firstResult) {
        return findCambioEstadoEntities(false, maxResults, firstResult);
    }

    private List<CambioEstado> findCambioEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CambioEstado.class));
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

    public CambioEstado findCambioEstado(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CambioEstado.class, id);
        } finally {
            em.close();
        }
    }

    public int getCambioEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CambioEstado> rt = cq.from(CambioEstado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
