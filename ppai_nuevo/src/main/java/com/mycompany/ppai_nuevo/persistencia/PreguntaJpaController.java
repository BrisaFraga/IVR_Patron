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
import entidades.Encuesta;
import entidades.Pregunta;
import entidades.RespuestaPosible;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Brisa
 */
public class PreguntaJpaController implements Serializable {

     public PreguntaJpaController() {
        emf = Persistence.createEntityManagerFactory("encuestaJPAPU");
    }
    public PreguntaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pregunta pregunta) {
        if (pregunta.getRespuestasPosibles() == null) {
            pregunta.setRespuestasPosibles(new ArrayList<RespuestaPosible>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Encuesta encuesta = pregunta.getEncuesta();
            if (encuesta != null) {
                encuesta = em.getReference(encuesta.getClass(), encuesta.getId());
                pregunta.setEncuesta(encuesta);
            }
            List<RespuestaPosible> attachedRespuestasPosibles = new ArrayList<RespuestaPosible>();
            for (RespuestaPosible respuestasPosiblesRespuestaPosibleToAttach : pregunta.getRespuestasPosibles()) {
                respuestasPosiblesRespuestaPosibleToAttach = em.getReference(respuestasPosiblesRespuestaPosibleToAttach.getClass(), respuestasPosiblesRespuestaPosibleToAttach.getId());
                attachedRespuestasPosibles.add(respuestasPosiblesRespuestaPosibleToAttach);
            }
            pregunta.setRespuestasPosibles(attachedRespuestasPosibles);
            em.persist(pregunta);
            if (encuesta != null) {
                encuesta.getPreguntas().add(pregunta);
                encuesta = em.merge(encuesta);
            }
            for (RespuestaPosible respuestasPosiblesRespuestaPosible : pregunta.getRespuestasPosibles()) {
                Pregunta oldPreguntaOfRespuestasPosiblesRespuestaPosible = respuestasPosiblesRespuestaPosible.getPregunta();
                respuestasPosiblesRespuestaPosible.setPregunta(pregunta);
                respuestasPosiblesRespuestaPosible = em.merge(respuestasPosiblesRespuestaPosible);
                if (oldPreguntaOfRespuestasPosiblesRespuestaPosible != null) {
                    oldPreguntaOfRespuestasPosiblesRespuestaPosible.getRespuestasPosibles().remove(respuestasPosiblesRespuestaPosible);
                    oldPreguntaOfRespuestasPosiblesRespuestaPosible = em.merge(oldPreguntaOfRespuestasPosiblesRespuestaPosible);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pregunta pregunta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pregunta persistentPregunta = em.find(Pregunta.class, pregunta.getId());
            Encuesta encuestaOld = persistentPregunta.getEncuesta();
            Encuesta encuestaNew = pregunta.getEncuesta();
            List<RespuestaPosible> respuestasPosiblesOld = persistentPregunta.getRespuestasPosibles();
            List<RespuestaPosible> respuestasPosiblesNew = pregunta.getRespuestasPosibles();
            if (encuestaNew != null) {
                encuestaNew = em.getReference(encuestaNew.getClass(), encuestaNew.getId());
                pregunta.setEncuesta(encuestaNew);
            }
            List<RespuestaPosible> attachedRespuestasPosiblesNew = new ArrayList<RespuestaPosible>();
            for (RespuestaPosible respuestasPosiblesNewRespuestaPosibleToAttach : respuestasPosiblesNew) {
                respuestasPosiblesNewRespuestaPosibleToAttach = em.getReference(respuestasPosiblesNewRespuestaPosibleToAttach.getClass(), respuestasPosiblesNewRespuestaPosibleToAttach.getId());
                attachedRespuestasPosiblesNew.add(respuestasPosiblesNewRespuestaPosibleToAttach);
            }
            respuestasPosiblesNew = attachedRespuestasPosiblesNew;
            pregunta.setRespuestasPosibles(respuestasPosiblesNew);
            pregunta = em.merge(pregunta);
            if (encuestaOld != null && !encuestaOld.equals(encuestaNew)) {
                encuestaOld.getPreguntas().remove(pregunta);
                encuestaOld = em.merge(encuestaOld);
            }
            if (encuestaNew != null && !encuestaNew.equals(encuestaOld)) {
                encuestaNew.getPreguntas().add(pregunta);
                encuestaNew = em.merge(encuestaNew);
            }
            for (RespuestaPosible respuestasPosiblesOldRespuestaPosible : respuestasPosiblesOld) {
                if (!respuestasPosiblesNew.contains(respuestasPosiblesOldRespuestaPosible)) {
                    respuestasPosiblesOldRespuestaPosible.setPregunta(null);
                    respuestasPosiblesOldRespuestaPosible = em.merge(respuestasPosiblesOldRespuestaPosible);
                }
            }
            for (RespuestaPosible respuestasPosiblesNewRespuestaPosible : respuestasPosiblesNew) {
                if (!respuestasPosiblesOld.contains(respuestasPosiblesNewRespuestaPosible)) {
                    Pregunta oldPreguntaOfRespuestasPosiblesNewRespuestaPosible = respuestasPosiblesNewRespuestaPosible.getPregunta();
                    respuestasPosiblesNewRespuestaPosible.setPregunta(pregunta);
                    respuestasPosiblesNewRespuestaPosible = em.merge(respuestasPosiblesNewRespuestaPosible);
                    if (oldPreguntaOfRespuestasPosiblesNewRespuestaPosible != null && !oldPreguntaOfRespuestasPosiblesNewRespuestaPosible.equals(pregunta)) {
                        oldPreguntaOfRespuestasPosiblesNewRespuestaPosible.getRespuestasPosibles().remove(respuestasPosiblesNewRespuestaPosible);
                        oldPreguntaOfRespuestasPosiblesNewRespuestaPosible = em.merge(oldPreguntaOfRespuestasPosiblesNewRespuestaPosible);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = pregunta.getId();
                if (findPregunta(id) == null) {
                    throw new NonexistentEntityException("The pregunta with id " + id + " no longer exists.");
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
            Pregunta pregunta;
            try {
                pregunta = em.getReference(Pregunta.class, id);
                pregunta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pregunta with id " + id + " no longer exists.", enfe);
            }
            Encuesta encuesta = pregunta.getEncuesta();
            if (encuesta != null) {
                encuesta.getPreguntas().remove(pregunta);
                encuesta = em.merge(encuesta);
            }
            List<RespuestaPosible> respuestasPosibles = pregunta.getRespuestasPosibles();
            for (RespuestaPosible respuestasPosiblesRespuestaPosible : respuestasPosibles) {
                respuestasPosiblesRespuestaPosible.setPregunta(null);
                respuestasPosiblesRespuestaPosible = em.merge(respuestasPosiblesRespuestaPosible);
            }
            em.remove(pregunta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pregunta> findPreguntaEntities() {
        return findPreguntaEntities(true, -1, -1);
    }

    public List<Pregunta> findPreguntaEntities(int maxResults, int firstResult) {
        return findPreguntaEntities(false, maxResults, firstResult);
    }

    private List<Pregunta> findPreguntaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pregunta.class));
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

    public Pregunta findPregunta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pregunta.class, id);
        } finally {
            em.close();
        }
    }

    public int getPreguntaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pregunta> rt = cq.from(Pregunta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
