/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.ppai_nuevo.controllers;
import com.opencsv.CSVWriter;

import com.mycompany.ppai_nuevo.interfaces.IAgregado;
import com.mycompany.ppai_nuevo.persistencia.PersistenciaController;
import entidades.Cliente;
import entidades.IteradorLlamada;
import entidades.Llamada;
import entidades.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Brisa
 */


@Data
public class GestorConsultarEncuesta implements IAgregado {

    private Llamada llamadaSeleccionada;
    private List<Llamada> llamadasFiltradas;
    private Cliente cliente;
    private Estado estadoActual;
    private String nombreEstadoActual;
    private Encuesta encuesta;
    private List<Pregunta> preguntas;
    private List<RespuestaDeCliente> respuestas;
    private double duracion;
    private LocalDateTime fechaInicioPeriodo;
    private LocalDateTime fechaFinPeriodo;
          PersistenciaController persistencia = new         PersistenciaController(); 



    public GestorConsultarEncuesta() {
    }

    public IteradorLlamada crearIterador(List<Object> elementos) {
        if (elementos != null && !elementos.isEmpty()) {
            Object primerElemento = elementos.get(0);

            if (primerElemento != null) {
                Class<?> tipoElemento = elementos.get(0).getClass();

                if (tipoElemento.equals(Llamada.class)) {
                    return new IteradorLlamada(elementos);
                }
            } else {
                throw new IllegalArgumentException("El primer elemento es nulo");
            }
        }
        throw new IllegalArgumentException("Tipo de elemento no compatible.");
    }

    public void consultarEncuesta() {
        // PantallaEncuesta.pedirFechasFiltroPeriodo();
    }

    public List<Llamada> tomarPeriodoLlamada(Date fechaInicioPeriodo, Date fechaFinPeriodo) {
        Instant instant1 = Instant.ofEpochMilli(fechaInicioPeriodo.getTime());
        Instant instant2 = Instant.ofEpochMilli(fechaFinPeriodo.getTime());

        this.fechaInicioPeriodo = LocalDateTime.ofInstant(instant1, ZoneId.systemDefault());
        this.fechaFinPeriodo = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());

        return getLlamadasConEncuesta();
    }

    public List<Llamada> getLlamadasConEncuesta() {
        List<Llamada> llamadas = persistencia.getLlamadas();
        IteradorLlamada iterador = crearIterador(Collections.singletonList(llamadas));
        ArrayList<Llamada> filtradas = new ArrayList<>();

        iterador.primero();

        while (!iterador.haTerminado()) {
            Llamada llamadaActual = iterador.actual();
            List<Object> filtros = List.of(this.fechaInicioPeriodo, this.fechaFinPeriodo, llamadaActual);

            if (iterador.cumpleFiltro(filtros)) {
                filtradas.add(llamadaActual);
            }

            iterador.siguiente();
        }
        this.llamadasFiltradas = filtradas;
        return filtradas;
    }

    public void tomarSeleccionLlamada(Llamada llamadaElegida) {
        this.llamadaSeleccionada = llamadaElegida;
        getDatosLlamadaSeleccionada();
    }

    public void getDatosLlamadaSeleccionada() {
        this.cliente = llamadaSeleccionada.getCliente();
        this.estadoActual = llamadaSeleccionada.getEstadoActual();
        this.nombreEstadoActual = estadoActual.getNombreEstado();
        this.duracion = llamadaSeleccionada.getDuracion();
        this.respuestas = llamadaSeleccionada.getRespuestasDeCliente();
        this.encuesta = llamadaSeleccionada.getEncuesta();
        this.preguntas = encuesta.getPreguntas();
    }

    public void tomarSeleccionOpcion(int seleccion) {
        if (seleccion == 0) {
            generarCsv();
        }
        if (seleccion == 1) {
            // Imprimir
        }
    }

    public void generarCsv() {
        // Ruta del archivo CSV a generar
        // String archivoCSV = "C:/Users/" + nombreArchivo + ".csv";
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yy");
        String fechaInicioStr = sdf.format(llamadaSeleccionada.getFechaHoraInicioLlamada());
        String archivoCSV = "C:/Users/Llamada_" + cliente.getDni()+"_Fecha_"+fechaInicioStr+".csv";

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(archivoCSV), '|', CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
            // Escribir encabezados
            String[] encabezados = {"Nombre del cliente", "Estado actual de la llamada", "Duración de la llamada"};
            csvWriter.writeNext(encabezados);

            // Escribir fila de datos de la llamada
            String[] filaLlamada = {cliente.getNombreCompleto(), estadoActual.getNombreEstado(), String.valueOf(duracion)};
            csvWriter.writeNext(filaLlamada);

            // Obtener las preguntas y respuestas asociadas a la llamada
            int cont = 0;

            // Escribir las preguntas y respuestas en filas separadas
            System.out.println(preguntas);
            for (RespuestaDeCliente respuesta : respuestas) {
                String descripcionPregunta = respuesta.getRespuestaSeleccionada().getPregunta().getPregunta();
                String descripcionRespuesta = respuesta.getRespuestaSeleccionada().getDescripcion();

                cont += 1;

                // Escribir fila de datos de la pregunta y respuesta
                String[] filaPregunta = {descripcionPregunta, descripcionRespuesta};
                csvWriter.writeNext(filaPregunta);
            }

            // Flushing y cerrando el escritor de CSV
            csvWriter.flush();
        } catch (IOException ignored) {
        }
    }
/*Muestra los datos de la llamada: cliente, estado actual, duración de la llamada,
    y los datos de las respuestas del cliente asociados a la llamada: Respuestas seleccionadas, 
    descripción de las preguntas y descripción de la encuesta que incluye las preguntas. 
    Permite generar un csv o imprimir el resultado de la encuesta asociada a la llamada seleccionada. */
    public String mostrarLlamadasString(Llamada llamada) {
        String llamadita = "";
        
        llamadita += "Cliente:"+ llamada.getCliente().getNombreCompleto();
        llamadita += " | Estado: "+ llamada.getEstadoActual().getNombre();
        
        llamadita += " | Duracion: " + llamada.getDuracion();
      
        return llamadita;
        
        
    }

    public List<Llamada> getLlamadasFiltradas() {
return llamadasFiltradas;    }

}
