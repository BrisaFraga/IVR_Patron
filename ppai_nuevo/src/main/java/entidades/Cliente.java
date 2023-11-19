/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.io.Serializable;
import javax.persistence.*;
import lombok.*;

/**
 *
 * @author Brisa
 */
@Entity
@Table(name = "Cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente implements Serializable {
    @Id
    @Column(name = "dni")
    private int dni;

    @Column(name = "nombre_completo")
    private String nombreCompleto;

    @Column(name = "nro_celular")
    private String nroCelular;
}
