/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

/**
 *
 * @author Brisa
 */
@Entity
@Table(name = "Llamada")
@Data
@NoArgsConstructor
public class Llamada implements Serializable {
    @Id
    @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
 @Column(name = "descripcionOperador")
    private String descripcionOperador;

    @Column(name = "detalleAccionRequerida")
    private String detalleAccionRequerida;

    @Column(name = "duracion")
    private float duracion;

    @Column(name = "encuestaEnviada")
    private int encuestaEnviada;

    @Column(name = "observacionAuditor")
    private String observacionAuditor;

    @JsonIgnore
    @OneToMany(mappedBy = "llamada", fetch = FetchType.EAGER)
    private List<RespuestaDeCliente> respuestasDeEncuesta;

    @JsonIgnore
    @OneToMany(mappedBy = "llamada", fetch = FetchType.EAGER)
    private List<CambioEstado> cambiosEstados;


    
    @OneToOne
    @JoinColumn(name = "dni_cliente", nullable = false)
    private Cliente cliente;

    public float calcularDuracion() {
        CambioEstado inicial = null;
        CambioEstado finalC = null;

        for (int i = 0; i < cambiosEstados.size(); i++) {
            if (cambiosEstados.get(i).esEstadoInicial()) {
                inicial = cambiosEstados.get(i);
            }
            if (cambiosEstados.get(i).esEstadoFinal()) {
                finalC = cambiosEstados.get(i);
            }
        }

        assert inicial != null;
        assert finalC != null;
        Duration duracion = Duration.between(inicial.getFechaHoraInicio(), finalC.getFechaHoraInicio());
        return duracion.toMinutes();
    }

    public boolean esDePeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        CambioEstado inicial = null;
        CambioEstado finalC = null;

        for (CambioEstado cambio : cambiosEstados) {
            if (cambio.esEstadoInicial()) inicial = cambio;
            if (cambio.esEstadoFinal()) finalC = cambio;
        }

        assert inicial != null;
        assert finalC != null;
        return inicial.getFechaHoraInicio().isAfter(fechaInicio) && finalC.getFechaHoraInicio().isBefore(fechaFin);

    }

    public boolean tieneEncuestaRespondida() {
        return respuestasDeEncuesta != null;
    }

    public Cliente getCliente(){
        return this.cliente;
    }

    public Estado getEstadoActual() {
        cambiosEstados.sort(Comparator.comparing(CambioEstado::getFechaHoraInicio).reversed());

        if (!cambiosEstados.isEmpty()) {
            return cambiosEstados.get(0).getEstado();
        } else {
            return null;
        }
    }

    public LocalDateTime getFechaHoraInicioLlamada() {
        cambiosEstados.sort(Comparator.comparing(CambioEstado::getFechaHoraInicio));

        if (!cambiosEstados.isEmpty()) {
            return cambiosEstados.get(0).getFechaHoraInicio();
        } else {
            return null;
        }
    }

    /*
    public ArrayList<String> getNombreClienteYEstado()
    {
        String nombreCompleto = cliente.getNombreCompleto();
        String estado = CambioEstado.esEstadoActual(cambiosEstados);
        ArrayList<String> lista = new ArrayList<String>();

        if (estado != null)
        {
            lista.add(nombreCompleto);
            lista.add(estado);
            return lista;
        }
        lista.add(nombreCompleto);
        lista.add("No se encontro estado actual");
        return lista;
    }
    */

    public Encuesta getEncuesta() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<RespuestaDeCliente> getRespuestasDeCliente() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
