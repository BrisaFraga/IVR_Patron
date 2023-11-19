/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ppai_nuevo.persistencia;

import com.mycompany.ppai_nuevo.persistencia.exceptions.NonexistentEntityException;
import entidades.Encuesta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Pregunta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Brisa
 */
public class EncuestaJpaController implements Serializable {

     public EncuestaJpaController() {
        emf = Persistence.createEntityManagerFactory("encuestaJPAPU");
    }
    public EncuestaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Encuesta encuesta) {
        if (encuesta.getPreguntas() == null) {
            encuesta.setPreguntas(new ArrayList<Pregunta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Pregunta> attachedPreguntas = new ArrayList<Pregunta>();
            for (Pregunta preguntasPreguntaToAttach : encuesta.getPreguntas()) {
                preguntasPreguntaToAttach = em.getReference(preguntasPreguntaToAttach.getClass(), preguntasPreguntaToAttach.getId());
                attachedPreguntas.add(preguntasPreguntaToAttach);
            }
            encuesta.setPreguntas(attachedPreguntas);
            em.persist(encuesta);
            for (Pregunta preguntasPregunta : encuesta.getPreguntas()) {
                Encuesta oldEncuestaOfPreguntasPregunta = preguntasPregunta.getEncuesta();
                preguntasPregunta.setEncuesta(encuesta);
                preguntasPregunta = em.merge(preguntasPregunta);
                if (oldEncuestaOfPreguntasPregunta != null) {
                    oldEncuestaOfPreguntasPregunta.getPreguntas().remove(preguntasPregunta);
                    oldEncuestaOfPreguntasPregunta = em.merge(oldEncuestaOfPreguntasPregunta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Encuesta encuesta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Encuesta persistentEncuesta = em.find(Encuesta.class, encuesta.getId());
            List<Pregunta> preguntasOld = persistentEncuesta.getPreguntas();
            List<Pregunta> preguntasNew = encuesta.getPreguntas();
            List<Pregunta> attachedPreguntasNew = new ArrayList<Pregunta>();
            for (Pregunta preguntasNewPreguntaToAttach : preguntasNew) {
                preguntasNewPreguntaToAttach = em.getReference(preguntasNewPreguntaToAttach.getClass(), preguntasNewPreguntaToAttach.getId());
                attachedPreguntasNew.add(preguntasNewPreguntaToAttach);
            }
            preguntasNew = attachedPreguntasNew;
            encuesta.setPreguntas(preguntasNew);
            encuesta = em.merge(encuesta);
            for (Pregunta preguntasOldPregunta : preguntasOld) {
                if (!preguntasNew.contains(preguntasOldPregunta)) {
                    preguntasOldPregunta.setEncuesta(null);
                    preguntasOldPregunta = em.merge(preguntasOldPregunta);
                }
            }
            for (Pregunta preguntasNewPregunta : preguntasNew) {
                if (!preguntasOld.contains(preguntasNewPregunta)) {
                    Encuesta oldEncuestaOfPreguntasNewPregunta = preguntasNewPregunta.getEncuesta();
                    preguntasNewPregunta.setEncuesta(encuesta);
                    preguntasNewPregunta = em.merge(preguntasNewPregunta);
                    if (oldEncuestaOfPreguntasNewPregunta != null && !oldEncuestaOfPreguntasNewPregunta.equals(encuesta)) {
                        oldEncuestaOfPreguntasNewPregunta.getPreguntas().remove(preguntasNewPregunta);
                        oldEncuestaOfPreguntasNewPregunta = em.merge(oldEncuestaOfPreguntasNewPregunta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = encuesta.getId();
                if (findEncuesta(id) == null) {
                    throw new NonexistentEntityException("The encuesta with id " + id + " no longer exists.");
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
            Encuesta encuesta;
            try {
                encuesta = em.getReference(Encuesta.class, id);
                encuesta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The encuesta with id " + id + " no longer exists.", enfe);
            }
            List<Pregunta> preguntas = encuesta.getPreguntas();
            for (Pregunta preguntasPregunta : preguntas) {
                preguntasPregunta.setEncuesta(null);
                preguntasPregunta = em.merge(preguntasPregunta);
            }
            em.remove(encuesta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Encuesta> findEncuestaEntities() {
        return findEncuestaEntities(true, -1, -1);
    }

    public List<Encuesta> findEncuestaEntities(int maxResults, int firstResult) {
        return findEncuestaEntities(false, maxResults, firstResult);
    }

    private List<Encuesta> findEncuestaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Encuesta.class));
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

    public Encuesta findEncuesta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Encuesta.class, id);
        } finally {
            em.close();
        }
    }

    public int getEncuestaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Encuesta> rt = cq.from(Encuesta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
