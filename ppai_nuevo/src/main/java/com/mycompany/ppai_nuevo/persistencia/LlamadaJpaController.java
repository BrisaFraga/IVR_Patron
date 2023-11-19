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
import entidades.RespuestaDeCliente;
import java.util.ArrayList;
import java.util.List;
import entidades.CambioEstado;
import entidades.Llamada;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Brisa
 */
public class LlamadaJpaController implements Serializable {
     public LlamadaJpaController() {
        emf = Persistence.createEntityManagerFactory("encuestaJPAPU");
    }

    public LlamadaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Llamada llamada) {
        if (llamada.getRespuestasDeCliente() == null) {
            llamada.setRespuestasDeEncuesta(new ArrayList<RespuestaDeCliente>());
        }
        if (llamada.getCambiosEstados() == null) {
            llamada.setCambiosEstados(new ArrayList<CambioEstado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<RespuestaDeCliente> attachedRespuestasDeCliente = new ArrayList<RespuestaDeCliente>();
            for (RespuestaDeCliente respuestasDeClienteRespuestaDeClienteToAttach : llamada.getRespuestasDeCliente()) {
                respuestasDeClienteRespuestaDeClienteToAttach = em.getReference(respuestasDeClienteRespuestaDeClienteToAttach.getClass(), respuestasDeClienteRespuestaDeClienteToAttach.getId());
                attachedRespuestasDeCliente.add(respuestasDeClienteRespuestaDeClienteToAttach);
            }
            llamada.setRespuestasDeEncuesta(attachedRespuestasDeCliente);
            List<CambioEstado> attachedCambiosEstado = new ArrayList<CambioEstado>();
            for (CambioEstado cambiosEstadoCambioEstadoToAttach : llamada.getCambiosEstados()) {
                cambiosEstadoCambioEstadoToAttach = em.getReference(cambiosEstadoCambioEstadoToAttach.getClass(), cambiosEstadoCambioEstadoToAttach.getId());
                attachedCambiosEstado.add(cambiosEstadoCambioEstadoToAttach);
            }
            llamada.setCambiosEstados(attachedCambiosEstado);
            em.persist(llamada);
            for (RespuestaDeCliente respuestasDeClienteRespuestaDeCliente : llamada.getRespuestasDeCliente()) {
                Llamada oldLlamadaOfRespuestasDeClienteRespuestaDeCliente = respuestasDeClienteRespuestaDeCliente.getLlamada();
                respuestasDeClienteRespuestaDeCliente.setLlamada(llamada);
                respuestasDeClienteRespuestaDeCliente = em.merge(respuestasDeClienteRespuestaDeCliente);
                if (oldLlamadaOfRespuestasDeClienteRespuestaDeCliente != null) {
                    oldLlamadaOfRespuestasDeClienteRespuestaDeCliente.getRespuestasDeCliente().remove(respuestasDeClienteRespuestaDeCliente);
                    oldLlamadaOfRespuestasDeClienteRespuestaDeCliente = em.merge(oldLlamadaOfRespuestasDeClienteRespuestaDeCliente);
                }
            }
            for (CambioEstado cambiosEstadoCambioEstado : llamada.getCambiosEstados()) {
                Llamada oldLlamadaOfCambiosEstadoCambioEstado = cambiosEstadoCambioEstado.getLlamada();
                cambiosEstadoCambioEstado.setLlamada(llamada);
                cambiosEstadoCambioEstado = em.merge(cambiosEstadoCambioEstado);
                if (oldLlamadaOfCambiosEstadoCambioEstado != null) {
                    oldLlamadaOfCambiosEstadoCambioEstado.getCambiosEstados().remove(cambiosEstadoCambioEstado);
                    oldLlamadaOfCambiosEstadoCambioEstado = em.merge(oldLlamadaOfCambiosEstadoCambioEstado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Llamada llamada) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Llamada persistentLlamada = em.find(Llamada.class, llamada.getId());
            List<RespuestaDeCliente> respuestasDeClienteOld = persistentLlamada.getRespuestasDeCliente();
            List<RespuestaDeCliente> respuestasDeClienteNew = llamada.getRespuestasDeCliente();
            List<CambioEstado> cambiosEstadoOld = persistentLlamada.getCambiosEstados();
            List<CambioEstado> cambiosEstadoNew = llamada.getCambiosEstados();
            List<RespuestaDeCliente> attachedRespuestasDeClienteNew = new ArrayList<RespuestaDeCliente>();
            for (RespuestaDeCliente respuestasDeClienteNewRespuestaDeClienteToAttach : respuestasDeClienteNew) {
                respuestasDeClienteNewRespuestaDeClienteToAttach = em.getReference(respuestasDeClienteNewRespuestaDeClienteToAttach.getClass(), respuestasDeClienteNewRespuestaDeClienteToAttach.getId());
                attachedRespuestasDeClienteNew.add(respuestasDeClienteNewRespuestaDeClienteToAttach);
            }
            respuestasDeClienteNew = attachedRespuestasDeClienteNew;
            llamada.setRespuestasDeEncuesta(respuestasDeClienteNew);
            List<CambioEstado> attachedCambiosEstadoNew = new ArrayList<CambioEstado>();
            for (CambioEstado cambiosEstadoNewCambioEstadoToAttach : cambiosEstadoNew) {
                cambiosEstadoNewCambioEstadoToAttach = em.getReference(cambiosEstadoNewCambioEstadoToAttach.getClass(), cambiosEstadoNewCambioEstadoToAttach.getId());
                attachedCambiosEstadoNew.add(cambiosEstadoNewCambioEstadoToAttach);
            }
            cambiosEstadoNew = attachedCambiosEstadoNew;
            llamada.setCambiosEstados(cambiosEstadoNew);
            llamada = em.merge(llamada);
            for (RespuestaDeCliente respuestasDeClienteOldRespuestaDeCliente : respuestasDeClienteOld) {
                if (!respuestasDeClienteNew.contains(respuestasDeClienteOldRespuestaDeCliente)) {
                    respuestasDeClienteOldRespuestaDeCliente.setLlamada(null);
                    respuestasDeClienteOldRespuestaDeCliente = em.merge(respuestasDeClienteOldRespuestaDeCliente);
                }
            }
            for (RespuestaDeCliente respuestasDeClienteNewRespuestaDeCliente : respuestasDeClienteNew) {
                if (!respuestasDeClienteOld.contains(respuestasDeClienteNewRespuestaDeCliente)) {
                    Llamada oldLlamadaOfRespuestasDeClienteNewRespuestaDeCliente = respuestasDeClienteNewRespuestaDeCliente.getLlamada();
                    respuestasDeClienteNewRespuestaDeCliente.setLlamada(llamada);
                    respuestasDeClienteNewRespuestaDeCliente = em.merge(respuestasDeClienteNewRespuestaDeCliente);
                    if (oldLlamadaOfRespuestasDeClienteNewRespuestaDeCliente != null && !oldLlamadaOfRespuestasDeClienteNewRespuestaDeCliente.equals(llamada)) {
                        oldLlamadaOfRespuestasDeClienteNewRespuestaDeCliente.getRespuestasDeCliente().remove(respuestasDeClienteNewRespuestaDeCliente);
                        oldLlamadaOfRespuestasDeClienteNewRespuestaDeCliente = em.merge(oldLlamadaOfRespuestasDeClienteNewRespuestaDeCliente);
                    }
                }
            }
            for (CambioEstado cambiosEstadoOldCambioEstado : cambiosEstadoOld) {
                if (!cambiosEstadoNew.contains(cambiosEstadoOldCambioEstado)) {
                    cambiosEstadoOldCambioEstado.setLlamada(null);
                    cambiosEstadoOldCambioEstado = em.merge(cambiosEstadoOldCambioEstado);
                }
            }
            for (CambioEstado cambiosEstadoNewCambioEstado : cambiosEstadoNew) {
                if (!cambiosEstadoOld.contains(cambiosEstadoNewCambioEstado)) {
                    Llamada oldLlamadaOfCambiosEstadoNewCambioEstado = cambiosEstadoNewCambioEstado.getLlamada();
                    cambiosEstadoNewCambioEstado.setLlamada(llamada);
                    cambiosEstadoNewCambioEstado = em.merge(cambiosEstadoNewCambioEstado);
                    if (oldLlamadaOfCambiosEstadoNewCambioEstado != null && !oldLlamadaOfCambiosEstadoNewCambioEstado.equals(llamada)) {
                        oldLlamadaOfCambiosEstadoNewCambioEstado.getCambiosEstados().remove(cambiosEstadoNewCambioEstado);
                        oldLlamadaOfCambiosEstadoNewCambioEstado = em.merge(oldLlamadaOfCambiosEstadoNewCambioEstado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = llamada.getId();
                if (findLlamada(id) == null) {
                    throw new NonexistentEntityException("The llamada with id " + id + " no longer exists.");
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
            Llamada llamada;
            try {
                llamada = em.getReference(Llamada.class, id);
                llamada.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The llamada with id " + id + " no longer exists.", enfe);
            }
            List<RespuestaDeCliente> respuestasDeCliente = llamada.getRespuestasDeCliente();
            for (RespuestaDeCliente respuestasDeClienteRespuestaDeCliente : respuestasDeCliente) {
                respuestasDeClienteRespuestaDeCliente.setLlamada(null);
                respuestasDeClienteRespuestaDeCliente = em.merge(respuestasDeClienteRespuestaDeCliente);
            }
            List<CambioEstado> cambiosEstado = llamada.getCambiosEstados();
            for (CambioEstado cambiosEstadoCambioEstado : cambiosEstado) {
                cambiosEstadoCambioEstado.setLlamada(null);
                cambiosEstadoCambioEstado = em.merge(cambiosEstadoCambioEstado);
            }
            em.remove(llamada);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Llamada> findLlamadaEntities() {
        return findLlamadaEntities(true, -1, -1);
    }

    public List<Llamada> findLlamadaEntities(int maxResults, int firstResult) {
        return findLlamadaEntities(false, maxResults, firstResult);
    }

    private List<Llamada> findLlamadaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Llamada.class));
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

    public Llamada findLlamada(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Llamada.class, id);
        } finally {
            em.close();
        }
    }

    public int getLlamadaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Llamada> rt = cq.from(Llamada.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
