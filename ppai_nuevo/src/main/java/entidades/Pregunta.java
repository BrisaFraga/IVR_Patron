/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Brisa
 */
@Entity
@Table(name = "Pregunta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pregunta implements Serializable {
    @Id
    @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String pregunta;

    @OneToMany(mappedBy = "pregunta", fetch = FetchType.LAZY)
    private List<RespuestaPosible> respuestasPosibles;

    @ManyToOne
    @JoinColumn(name = "id_encuesta")
    private Encuesta encuesta;

    public String listarRespuestasPosibles() {
        StringBuilder respuestas = new StringBuilder("Respuestas posibles: ");

        for (RespuestaPosible respuestaPosible : respuestasPosibles) {
            respuestas.append(respuestaPosible.getDescripcion());
        }

        return respuestas.toString();
    }
/*
    public Boolean esEncuestaDeCliente(List<RespuestaPosible> respuestas)
    {
        for (RespuestaPosible respuesta : respuestas)
        {
            if (tieneRtaPosible(respuesta))
            {
                return true;
            }
        }
        return false;
    }

    public Boolean tieneRtaPosible(RespuestaPosible respuesta)
    {
        for (RespuestaPosible respuestaPosible : respuestaPosibles)
        {
            if (respuesta.getDescripcion() == respuestaPosible.getDescripcionRta())
            {
                return true;
            }
        }
        return false;
    }
 */
}
