package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
public class Alimento extends AbstractEntity {

//    @NotBlank
    private String nombre;

//    @NotBlank
    private int grRacion;

    public Alimento(String nombre, int grRacion) {
        this.nombre = nombre;
        this.grRacion = grRacion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getGrRacion() {
        return grRacion;
    }

    public void setGrRacion(int grRacion) {
        this.grRacion = grRacion;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public Alimento() {
    }
    public Alimento(String nombre) {
        this.nombre = nombre;
    }
}
