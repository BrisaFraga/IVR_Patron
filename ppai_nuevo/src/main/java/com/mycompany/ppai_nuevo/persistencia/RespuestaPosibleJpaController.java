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
import entidades.Pregunta;
import entidades.RespuestaPosible;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Brisa
 */
public class RespuestaPosibleJpaController implements Serializable {

     public RespuestaPosibleJpaController() {
        emf = Persistence.createEntityManagerFactory("encuestaJPAPU");
    }
    public RespuestaPosibleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RespuestaPosible respuestaPosible) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pregunta pregunta = respuestaPosible.getPregunta();
            if (pregunta != null) {
                pregunta = em.getReference(pregunta.getClass(), pregunta.getId());
                respuestaPosible.setPregunta(pregunta);
            }
            em.persist(respuestaPosible);
            if (pregunta != null) {
                pregunta.getRespuestasPosibles().add(respuestaPosible);
                pregunta = em.merge(pregunta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RespuestaPosible respuestaPosible) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RespuestaPosible persistentRespuestaPosible = em.find(RespuestaPosible.class, respuestaPosible.getId());
            Pregunta preguntaOld = persistentRespuestaPosible.getPregunta();
            Pregunta preguntaNew = respuestaPosible.getPregunta();
            if (preguntaNew != null) {
                preguntaNew = em.getReference(preguntaNew.getClass(), preguntaNew.getId());
                respuestaPosible.setPregunta(preguntaNew);
            }
            respuestaPosible = em.merge(respuestaPosible);
            if (preguntaOld != null && !preguntaOld.equals(preguntaNew)) {
                preguntaOld.getRespuestasPosibles().remove(respuestaPosible);
                preguntaOld = em.merge(preguntaOld);
            }
            if (preguntaNew != null && !preguntaNew.equals(preguntaOld)) {
                preguntaNew.getRespuestasPosibles().add(respuestaPosible);
                preguntaNew = em.merge(preguntaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = respuestaPosible.getId();
                if (findRespuestaPosible(id) == null) {
                    throw new NonexistentEntityException("The respuestaPosible with id " + id + " no longer exists.");
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
            RespuestaPosible respuestaPosible;
            try {
                respuestaPosible = em.getReference(RespuestaPosible.class, id);
                respuestaPosible.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The respuestaPosible with id " + id + " no longer exists.", enfe);
            }
            Pregunta pregunta = respuestaPosible.getPregunta();
            if (pregunta != null) {
                pregunta.getRespuestasPosibles().remove(respuestaPosible);
                pregunta = em.merge(pregunta);
            }
            em.remove(respuestaPosible);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RespuestaPosible> findRespuestaPosibleEntities() {
        return findRespuestaPosibleEntities(true, -1, -1);
    }

    public List<RespuestaPosible> findRespuestaPosibleEntities(int maxResults, int firstResult) {
        return findRespuestaPosibleEntities(false, maxResults, firstResult);
    }

    private List<RespuestaPosible> findRespuestaPosibleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RespuestaPosible.class));
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

    public RespuestaPosible findRespuestaPosible(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RespuestaPosible.class, id);
        } finally {
            em.close();
        }
    }

    public int getRespuestaPosibleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RespuestaPosible> rt = cq.from(RespuestaPosible.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
