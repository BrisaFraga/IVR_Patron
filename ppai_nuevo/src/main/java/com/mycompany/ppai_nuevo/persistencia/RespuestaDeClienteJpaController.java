/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ppai_nuevo.persistencia;

import com.mycompany.ppai_nuevo.persistencia.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Llamada;
import entidades.RespuestaDeCliente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Brisa
 */
public class RespuestaDeClienteJpaController implements Serializable {
     public RespuestaDeClienteJpaController() {
        emf = Persistence.createEntityManagerFactory("encuestaJPAPU");
    }

    public RespuestaDeClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RespuestaDeCliente respuestaDeCliente) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Llamada llamada = respuestaDeCliente.getLlamada();
            if (llamada != null) {
                llamada = em.getReference(llamada.getClass(), llamada.getId());
                respuestaDeCliente.setLlamada(llamada);
            }
            em.persist(respuestaDeCliente);
            if (llamada != null) {
                llamada.getRespuestasDeCliente().add(respuestaDeCliente);
                llamada = em.merge(llamada);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RespuestaDeCliente respuestaDeCliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RespuestaDeCliente persistentRespuestaDeCliente = em.find(RespuestaDeCliente.class, respuestaDeCliente.getId());
            Llamada llamadaOld = persistentRespuestaDeCliente.getLlamada();
            Llamada llamadaNew = respuestaDeCliente.getLlamada();
            if (llamadaNew != null) {
                llamadaNew = em.getReference(llamadaNew.getClass(), llamadaNew.getId());
                respuestaDeCliente.setLlamada(llamadaNew);
            }
            respuestaDeCliente = em.merge(respuestaDeCliente);
            if (llamadaOld != null && !llamadaOld.equals(llamadaNew)) {
                llamadaOld.getRespuestasDeCliente().remove(respuestaDeCliente);
                llamadaOld = em.merge(llamadaOld);
            }
            if (llamadaNew != null && !llamadaNew.equals(llamadaOld)) {
                llamadaNew.getRespuestasDeCliente().add(respuestaDeCliente);
                llamadaNew = em.merge(llamadaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = respuestaDeCliente.getId();
                if (findRespuestaDeCliente(id) == null) {
                    throw new NonexistentEntityException("The respuestaDeCliente with id " + id + " no longer exists.");
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
            RespuestaDeCliente respuestaDeCliente;
            try {
                respuestaDeCliente = em.getReference(RespuestaDeCliente.class, id);
                respuestaDeCliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The respuestaDeCliente with id " + id + " no longer exists.", enfe);
            }
            Llamada llamada = respuestaDeCliente.getLlamada();
            if (llamada != null) {
                llamada.getRespuestasDeCliente().remove(respuestaDeCliente);
                llamada = em.merge(llamada);
            }
            em.remove(respuestaDeCliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RespuestaDeCliente> findRespuestaDeClienteEntities() {
        return findRespuestaDeClienteEntities(true, -1, -1);
    }

    public List<RespuestaDeCliente> findRespuestaDeClienteEntities(int maxResults, int firstResult) {
        return findRespuestaDeClienteEntities(false, maxResults, firstResult);
    }

    private List<RespuestaDeCliente> findRespuestaDeClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RespuestaDeCliente.class));
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

    public RespuestaDeCliente findRespuestaDeCliente(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RespuestaDeCliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getRespuestaDeClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RespuestaDeCliente> rt = cq.from(RespuestaDeCliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
