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
@Table(name = "Estado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estado implements Serializable {
    @Id
    @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String nombre;

    public Boolean esFinalizada() {
        return nombre.equalsIgnoreCase("Finalizada");
    }

    public Boolean EsIniciada() {
        return nombre.equalsIgnoreCase("Iniciada");
    }

    public String getNombreEstado() {

    return nombre;    }

   
}
