/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;

/**
 *
 * @author Brisa
 */

@Entity
@Table(name = "CambioEstado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambioEstado implements Serializable {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Column(name = "fechaHoraInicio")
    private LocalDateTime fechaHoraInicio;

    @OneToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "id_llamada", nullable = false)
    private Llamada llamada;

    public boolean esEstadoInicial() {
        return estado.EsIniciada();
    }

    public boolean esEstadoFinal() {
        return estado.esFinalizada();
    }

    public String getNombreEstado()
    {
        return estado.getNombreEstado();
    }
}
