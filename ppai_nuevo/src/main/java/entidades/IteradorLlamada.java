/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import com.mycompany.ppai_nuevo.interfaces.Iterador;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Brisa
 */
public class IteradorLlamada implements Iterador<Llamada, Object> {

    private int posicionActual;
    private final List<Llamada> llamadas;

    public IteradorLlamada(List<Object> elementos) {
        this.llamadas = new ArrayList<>();
        for (Object elemento : elementos) {
            if (elemento instanceof Llamada) {
                this.llamadas.add((Llamada) elemento);
            }
        }
    }

    @Override
    public void primero() {
        posicionActual = 0;
    }

    @Override
    public void siguiente() {
        posicionActual++;
    }

    @Override
    public Llamada actual() {
        return llamadas.get(posicionActual);
    }

    @Override
    public boolean haTerminado() {
        return posicionActual == llamadas.size() - 1;
    }

    @Override
    public boolean cumpleFiltro(List<Object> filtros) {
        if (filtros.size() < 3) {
            return false;
        }

        LocalDateTime fechaInicioPeriodo = (LocalDateTime) filtros.get(0);
        LocalDateTime fechaFinPeriodo = (LocalDateTime) filtros.get(1);
        Llamada llamada = (Llamada) filtros.get(2);

        return llamada.esDePeriodo(fechaInicioPeriodo, fechaFinPeriodo) && llamada.tieneEncuestaRespondida();
    }
}
