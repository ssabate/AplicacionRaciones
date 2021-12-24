package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

//Como hacer la clave compuesta
//https://vladmihalcea.com/the-best-way-to-map-a-composite-primary-key-with-jpa-and-hibernate/
@Entity
@Data
public class Ingesta extends AbstractEntity {


//    @NotNull
    private LocalDate date;

//    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoComida comida;

    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Alimento alimento;


    //    @NotEmpty
    private double raciones;

    public Ingesta() {
    }
    public Ingesta(Alimento alimento) {
        this.alimento = alimento;
    }

    public Ingesta(Alimento alimento, double raciones) {
        this.alimento = alimento;
        this.raciones = raciones;
    }

    public Ingesta(LocalDate date, TipoComida comida, Alimento alimento, double raciones) {
        this(alimento, raciones);
        this.date = date;
        this.comida = comida;
    }
}
