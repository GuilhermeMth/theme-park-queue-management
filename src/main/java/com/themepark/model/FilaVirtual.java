package com.themepark.model;

import com.themepark.model.datastructures.LinkedList;

import java.time.LocalTime;

public class FilaVirtual {
    private LinkedList<Visitante> fila;
    private Atracao atracao;
    private int tempoEsperaMinutos;
    private LocalTime ultimaSessao;

    public FilaVirtual(Atracao atracao) {
        this.atracao = atracao;
        this.fila = new LinkedList<>();
        this.tempoEsperaMinutos = 0;
        this.ultimaSessao = LocalTime.now();
    }

    public void addVisitante(Visitante visitante) {

    }

    public Atracao getAtracao() {
        return atracao;
    }

    public void setAtracao(Atracao atracao) {
        this.atracao = atracao;
    }

    public LinkedList<Visitante> getFila() {
        return fila;
    }

    public void setFila(LinkedList<Visitante> fila) {
        this.fila = fila;
    }

    public int getTempoEsperaMinutos() {
        return tempoEsperaMinutos;
    }

    public void setTempoEsperaMinutos(int tempoEsperaMinutos) {
        this.tempoEsperaMinutos = tempoEsperaMinutos;
    }

    public LocalTime getUltimaSessao() {
        return ultimaSessao;
    }

    public void setUltimaSessao(LocalTime ultimaSessao) {
        this.ultimaSessao = ultimaSessao;
    }

    @Override
    public String toString() {
        return "FilaVirtual{" +
                "atracao=" + atracao +
                ", fila=" + fila +
                ", tempoEsperaMinutos=" + tempoEsperaMinutos +
                ", ultimaSessao=" + ultimaSessao +
                '}';
    }


}
