package com.example.application.data.filter;

import com.example.application.data.entity.TipoComida;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IngestaFiltro {

    private LocalDate date;
    private TipoComida comida;

    public IngestaFiltro() {
    }

    public IngestaFiltro(LocalDate date, TipoComida comida) {
        this.date = date;
        this.comida = comida;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TipoComida getComida() {
        return comida;
    }

    public void setComida(TipoComida comida) {
        this.comida = comida;
    }
}
