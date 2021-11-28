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

}
