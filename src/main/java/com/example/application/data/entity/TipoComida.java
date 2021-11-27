package com.example.application.data.entity;

public enum TipoComida {
    DESAYUNO("Desayuno"),
    COMIDA("Almuerzo"),
    MERIENDA("Merienda"),
    CENA("Cena"),
    ENTREHORAS("Entre horas");

    private String name;

    TipoComida(String nom){
        this.name=nom;

    }
    public static String[] names() {
        TipoComida[] values = values();
        String[] names = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            names[i] = values[i].getName();
        }

        return names;
    }

    public String getName(){
        return name;
    }
}
