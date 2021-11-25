package com.example.application.data.entity;

public enum TipoComida {
    DESAYUNO, COMIDA, MERIENDA, CENA;

    public static String[] names() {
        TipoComida[] states = values();
        String[] names = new String[states.length];

        for (int i = 0; i < states.length; i++) {
            names[i] = states[i].name();
        }

        return names;
    }
}
