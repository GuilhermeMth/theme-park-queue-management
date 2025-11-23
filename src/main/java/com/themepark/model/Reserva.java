package com.themepark.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Reserva {
    private static int contadorId = 1;

    private int id;
    private Visitante visitante;
    private Atracao atracao;
    private HorarioSessao horarioEscolhido;
    private LocalDateTime dataHoraCriacao;
    private LocalDateTime dataHoraConclusao;
    private StatusReserva status;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Reserva(Visitante visitante, Atracao atracao, HorarioSessao horarioEscolhido) {
        if (visitante == null) {
            throw new IllegalArgumentException("Visitante não pode ser nulo");
        }
        if (atracao == null) {
            throw new IllegalArgumentException("Atração não pode ser nula");
        }

        this.id = contadorId++;
        this.visitante = visitante;
        this.atracao = atracao;
        this.horarioEscolhido = horarioEscolhido;
        this.dataHoraCriacao = LocalDateTime.now();
        this.status = StatusReserva.ATIVA;
        this.dataHoraConclusao = null;
    }

    // Construtor sem horário específico (para filas virtuais sem horário marcado)
    public Reserva(Visitante visitante, Atracao atracao) {
        this(visitante, atracao, null);
    }

    public boolean podeEntrarNaAtracao() {
        // Verifica se a reserva está ativa
        if (this.status != StatusReserva.ATIVA) {
            return false;
        }

        // Verifica idade mínima
        if (visitante.calcularIdade() < atracao.getFaixaEtariaMininima()) {
            return false;
        }

        // Verifica tipo de ingresso
        return visitante.temPrioridadeParaAtracao(atracao);
    }

    public String getMotivoRecusa() {
        if (this.status != StatusReserva.ATIVA) {
            return "Reserva não está ativa";
        }

        return atracao.getMotivoIndisponibilidade(visitante);
    }

    public void concluir() {
        if (this.status != StatusReserva.ATIVA) {
            throw new IllegalStateException("Apenas reservas ativas podem ser concluídas");
        }

        this.status = StatusReserva.CONCLUIDA;
        this.dataHoraConclusao = LocalDateTime.now();
    }

    public void cancelar() {
        if (this.status != StatusReserva.ATIVA) {
            throw new IllegalStateException("Apenas reservas ativas podem ser canceladas");
        }

        this.status = StatusReserva.CANCELADA;
        this.dataHoraConclusao = LocalDateTime.now();
    }

    public boolean isAtiva() {
        return this.status == StatusReserva.ATIVA;
    }

    public boolean isConcluida() {
        return this.status == StatusReserva.CONCLUIDA;
    }

    public boolean isCancelada() {
        return this.status == StatusReserva.CANCELADA;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public Visitante getVisitante() {
        return visitante;
    }

    public void setVisitante(Visitante visitante) {
        this.visitante = visitante;
    }

    public Atracao getAtracao() {
        return atracao;
    }

    public void setAtracao(Atracao atracao) {
        this.atracao = atracao;
    }

    public HorarioSessao getHorarioEscolhido() {
        return horarioEscolhido;
    }

    public void setHorarioEscolhido(HorarioSessao horarioEscolhido) {
        this.horarioEscolhido = horarioEscolhido;
    }

    public LocalDateTime getDataHoraCriacao() {
        return dataHoraCriacao;
    }

    public LocalDateTime getDataHoraConclusao() {
        return dataHoraConclusao;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public String getDataHoraCriacaoFormatada() {
        return dataHoraCriacao.format(FORMATTER);
    }

    public String getDataHoraConclusaoFormatada() {
        return dataHoraConclusao != null ? dataHoraConclusao.format(FORMATTER) : "N/A";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reserva #").append(id)
                .append(" - ").append(visitante.getNome())
                .append(" -> ").append(atracao.getNome())
                .append(" [").append(status).append("]")
                .append(" em ").append(getDataHoraCriacaoFormatada());

        if (horarioEscolhido != null) {
            sb.append(" para ").append(horarioEscolhido.getHoraInicio());
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return id == reserva.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}