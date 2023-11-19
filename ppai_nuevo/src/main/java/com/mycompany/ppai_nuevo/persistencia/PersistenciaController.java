/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ppai_nuevo.persistencia;

import entidades.Llamada;
import java.util.List;

/**
 *
 * @author Brisa
 */
public class PersistenciaController {
    CambioEstadoJpaController  cambioEstado = new CambioEstadoJpaController();
    ClienteJpaController cliente = new ClienteJpaController();
    EncuestaJpaController encuesta = new EncuestaJpaController();
    EstadoJpaController estado = new EstadoJpaController();
    LlamadaJpaController llamadas = new LlamadaJpaController();
    PreguntaJpaController pregunta = new PreguntaJpaController();
    RespuestaDeClienteJpaController respuestaCliente= new RespuestaDeClienteJpaController();
    RespuestaPosibleJpaController respuestaPosible = new RespuestaPosibleJpaController();

    public List<Llamada> getLlamadas() {

    return llamadas.findLlamadaEntities();    }
}