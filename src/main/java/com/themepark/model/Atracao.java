package com.themepark.model;

import com.themepark.model.datastructures.LinkedList;

import java.time.LocalTime;


public class Atracao {
    private String nome;
    private TipoAtracao tipo;
    private int capacidadePorSessao;
    private int faixaEtariaMininima;
    private LinkedList<HorarioSessao> horariosSessao;
    private NivelPrioridade prioridadeAceita;

    public Atracao(int capacidadePorSessao, int faixaEtariaMininima, String nome, TipoAtracao tipo, NivelPrioridade prioridadeAceita) {
        this.capacidadePorSessao = capacidadePorSessao;
        this.faixaEtariaMininima = faixaEtariaMininima;
        this.nome = nome;
        this.tipo = tipo;
        this.prioridadeAceita = prioridadeAceita;
        this.horariosSessao = new LinkedList<>();
    }

    public void adicionarHorario(LocalTime inicio, LocalTime fim) {
        this.horariosSessao.addLast(new HorarioSessao(inicio, fim));
    }

    public int getCapacidadePorSessao() {
        return capacidadePorSessao;
    }

    public void setCapacidadePorSessao(int capacidadePorSessao) {
        this.capacidadePorSessao = capacidadePorSessao;
    }

    public int getFaixaEtariaMininima() {
        return faixaEtariaMininima;
    }

    public void setFaixaEtariaMininima(int faixaEtariaMininima) {
        this.faixaEtariaMininima = faixaEtariaMininima;
    }

    public LinkedList<HorarioSessao> getHorariosSessao() {
        return horariosSessao;
    }

    public void setHorariosSessao(LinkedList<HorarioSessao> horariosSessao) {
        this.horariosSessao = horariosSessao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public NivelPrioridade getPrioridadeAceita() {
        return prioridadeAceita;
    }

    public void setPrioridadeAceita(NivelPrioridade prioridadeAceita) {
        this.prioridadeAceita = prioridadeAceita;
    }

    public TipoAtracao getTipo() {
        return tipo;
    }

    public void setTipo(TipoAtracao tipo) {
        this.tipo = tipo;
    }

    public int getDuracaoSessaoMinutos() {
        if (this.horariosSessao.getSize() > 0) {

            try {
                HorarioSessao primeiraSessao = this.horariosSessao.get(0); 

                return primeiraSessao.calcularDuracaoMinutos(); 
            } catch (IndexOutOfBoundsException e) {
                return 10; 
            }
        }
        return 10; 
    }
}
