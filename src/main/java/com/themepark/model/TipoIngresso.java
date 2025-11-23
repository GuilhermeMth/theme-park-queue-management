package com.themepark.model;

public enum TipoIngresso {
    COMUM(1),
    PREMIUM(2),
    ELITE(3);

    private final int nivelPrioridade;

    TipoIngresso(int nivelPrioridade) {
        this.nivelPrioridade = nivelPrioridade;
    }

    public int getNivelPrioridade() {
        return nivelPrioridade;
    }
}
