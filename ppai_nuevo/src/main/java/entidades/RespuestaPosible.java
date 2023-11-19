/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.io.Serializable;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Brisa
 */
@Entity
@Table(name = "RespuestaPosible")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaPosible implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String descripcion;
    private int valor;

    @ManyToOne
    @JoinColumn(name = "id_pregunta")
    private Pregunta pregunta;
}
