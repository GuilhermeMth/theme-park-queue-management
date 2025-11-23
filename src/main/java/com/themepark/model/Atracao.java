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
        // Validações
        if (capacidadePorSessao <= 0) {
            throw new IllegalArgumentException("Capacidade deve ser maior que zero");
        }
        if (faixaEtariaMininima < 0) {
            throw new IllegalArgumentException("Faixa etária mínima não pode ser negativa");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da atração não pode ser vazio");
        }

        this.capacidadePorSessao = capacidadePorSessao;
        this.faixaEtariaMininima = faixaEtariaMininima;
        this.nome = nome;
        this.tipo = tipo;
        this.prioridadeAceita = prioridadeAceita;
        this.horariosSessao = new LinkedList<>();
    }

    public void adicionarHorario(LocalTime inicio, LocalTime fim) {
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Horário de início deve ser antes do horário de fim");
        }

        HorarioSessao novoHorario = new HorarioSessao(inicio, fim);

        // Verifica se já existe esse horário
        for (int i = 0; i < horariosSessao.getSize(); i++) {
            try {
                HorarioSessao existente = horariosSessao.get(i);
                if (existente.equals(novoHorario)) {
                    throw new IllegalArgumentException("Este horário já foi cadastrado");
                }
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }

        this.horariosSessao.addLast(novoHorario);
    }

    public boolean removerHorario(HorarioSessao horario) {
        return this.horariosSessao.remove(horario);
    }

    public boolean isDisponivelParaVisitante(Visitante visitante) {
        // Verifica idade
        if (visitante.calcularIdade() < this.faixaEtariaMininima) {
            return false;
        }

        // Verifica tipo de ingresso
        int nivelVisitante = visitante.getTipoIngresso().getNivelPrioridade();
        int nivelMinimo = convertePrioridadeParaNumero(this.prioridadeAceita);

        return nivelVisitante >= nivelMinimo;
    }

    private int convertePrioridadeParaNumero(NivelPrioridade nivel) {
        switch (nivel) {
            case PASSE_ELITE: return 3;
            case PASSE_PREMIUM: return 2;
            default: return 1;
        }
    }

    public String getMotivoIndisponibilidade(Visitante visitante) {
        if (visitante.calcularIdade() < this.faixaEtariaMininima) {
            return "Idade mínima requerida: " + this.faixaEtariaMininima + " anos";
        }

        int nivelVisitante = visitante.getTipoIngresso().getNivelPrioridade();
        int nivelMinimo = convertePrioridadeParaNumero(this.prioridadeAceita);

        if (nivelVisitante < nivelMinimo) {
            return "Requer ingresso de nível: " + this.prioridadeAceita;
        }

        return "Atração disponível";
    }

    // Getters e Setters
    public int getCapacidadePorSessao() {
        return capacidadePorSessao;
    }

    public void setCapacidadePorSessao(int capacidadePorSessao) {
        if (capacidadePorSessao <= 0) {
            throw new IllegalArgumentException("Capacidade deve ser maior que zero");
        }
        this.capacidadePorSessao = capacidadePorSessao;
    }

    public int getFaixaEtariaMininima() {
        return faixaEtariaMininima;
    }

    public void setFaixaEtariaMininima(int faixaEtariaMininima) {
        if (faixaEtariaMininima < 0) {
            throw new IllegalArgumentException("Faixa etária mínima não pode ser negativa");
        }
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
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da atração não pode ser vazio");
        }
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

    @Override
    public String toString() {
        return nome + " (" + tipo + ") - Capacidade: " + capacidadePorSessao;
    }
}