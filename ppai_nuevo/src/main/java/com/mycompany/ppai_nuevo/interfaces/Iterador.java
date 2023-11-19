/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.ppai_nuevo.interfaces;

/**
 *
 * @author Brisa
 */
import java.util.List;

public interface Iterador<T, W> {
    void primero();
    void siguiente();
    T actual();
    boolean haTerminado();
    boolean cumpleFiltro(List<W> filtros);
}
