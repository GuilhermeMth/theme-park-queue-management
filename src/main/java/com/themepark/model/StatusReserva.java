package com.themepark.model;

public enum StatusReserva {
    ATIVA("Ativa - Aguardando na fila"),
    CONCLUIDA("Concluída - Atração já foi aproveitada"),
    CANCELADA("Cancelada - Visitante saiu da fila");

    private final String descricao;

    StatusReserva(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}