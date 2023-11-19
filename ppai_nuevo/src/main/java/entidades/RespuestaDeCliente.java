/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*
        ;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Brisa
 */
@Entity
@Table(name = "RespuestasDeCliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaDeCliente implements Serializable {
    @Id
    @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private LocalDateTime fechaEncuesta;

    @OneToOne
    @JoinColumn(name = "id_respuesta_posible", nullable = false)
    private RespuestaPosible respuestaSeleccionada;

    @ManyToOne
    @JoinColumn(name = "id_llamada")
    private Llamada llamada;
}
