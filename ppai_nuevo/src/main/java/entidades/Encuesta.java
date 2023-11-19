/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import lombok.*;

/**
 *
 * @author Brisa
 */
@Entity
@Table(name = "Encuesta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Encuesta implements Serializable {

    @Id
    @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String descripcion;
    private LocalDateTime fechaFinVigencia;

    @OneToMany(mappedBy = "encuesta", fetch = FetchType.LAZY)
    private List<Pregunta> preguntas;

    public boolean esVigente(LocalDateTime fechaVig) {
        return fechaFinVigencia.isBefore(fechaVig);
    }
}
