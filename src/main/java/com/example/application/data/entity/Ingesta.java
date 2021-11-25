package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Ingesta extends AbstractEntity {

    @NotNull
    @ManyToOne
    private Alimento alimento;

//    @NotEmpty
    private int raciones;

    public Ingesta() {
    }
    public Ingesta(Alimento alimento) {
        this.alimento = alimento;
    }

    public Ingesta(Alimento alimento, int raciones) {
        this.alimento = alimento;
        this.raciones = raciones;
    }
}
