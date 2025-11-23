package com.themepark.model;

import com.themepark.model.datastructures.LinkedList;
import com.themepark.model.datastructures.Node;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SistemaParque {
    private static SistemaParque instance;

    private LinkedList<Atracao> atracoes;
    private LinkedList<Visitante> visitantes;
    private LinkedList<FilaVirtual> filasVirtuais;
    private LinkedList<Reserva> reservas;
    private Estatisticas estatisticas;

    private SistemaParque() {
        this.atracoes = new LinkedList<>();
        this.visitantes = new LinkedList<>();
        this.filasVirtuais = new LinkedList<>();
        this.reservas = new LinkedList<>();
        this.estatisticas = new Estatisticas(this);
    }

    // Singleton pattern
    public static SistemaParque getInstance() {
        if (instance == null) {
            instance = new SistemaParque();
        }
        return instance;
    }

    // ========== GERENCIAMENTO DE ATRAÇÕES ==========

    public void cadastrarAtracao(Atracao atracao) {
        if (buscarAtracao(atracao.getNome()) != null) {
            throw new IllegalArgumentException("Já existe uma atração com este nome");
        }

        this.atracoes.addLast(atracao);

        // Cria automaticamente uma fila virtual para a atração
        FilaVirtual filaVirtual = new FilaVirtual(atracao);
        this.filasVirtuais.addLast(filaVirtual);
    }

    public Atracao buscarAtracao(String nome) {
        Node<Atracao> current = this.atracoes.getHead();

        while (current != null) {
            if (current.getElement().getNome().equalsIgnoreCase(nome)) {
                return current.getElement();
            }
            current = current.getNext();
        }

        return null;
    }

    public boolean removerAtracao(String nome) {
        Atracao atracao = buscarAtracao(nome);

        if (atracao == null) {
            return false;
        }

        // Remove a fila virtual associada
        FilaVirtual fila = obterFilaVirtual(atracao);
        if (fila != null) {
            this.filasVirtuais.remove(fila);
        }

        return this.atracoes.remove(atracao);
    }

    public List<Atracao> listarAtracoes() {
        List<Atracao> lista = new ArrayList<>();
        Node<Atracao> current = this.atracoes.getHead();

        while (current != null) {
            lista.add(current.getElement());
            current = current.getNext();
        }

        return lista;
    }

    public List<Atracao> listarAtracoesDisponiveisParaVisitante(Visitante visitante) {
        List<Atracao> disponiveis = new ArrayList<>();
        Node<Atracao> current = this.atracoes.getHead();

        while (current != null) {
            Atracao atracao = current.getElement();
            if (atracao.isDisponivelParaVisitante(visitante)) {
                disponiveis.add(atracao);
            }
            current = current.getNext();
        }

        return disponiveis;
    }

    // ========== GERENCIAMENTO DE VISITANTES ==========

    public void cadastrarVisitante(Visitante visitante) {
        if (buscarVisitante(visitante.getCpf()) != null) {
            throw new IllegalArgumentException("Visitante já cadastrado");
        }

        this.visitantes.addLast(visitante);
    }

    public Visitante buscarVisitante(String cpf) {
        Node<Visitante> current = this.visitantes.getHead();

        while (current != null) {
            if (current.getElement().getCpf().equals(cpf)) {
                return current.getElement();
            }
            current = current.getNext();
        }

        return null;
    }

    public boolean removerVisitante(String cpf) {
        Visitante visitante = buscarVisitante(cpf);
        return visitante != null && this.visitantes.remove(visitante);
    }

    public List<Visitante> listarVisitantes() {
        List<Visitante> lista = new ArrayList<>();
        Node<Visitante> current = this.visitantes.getHead();

        while (current != null) {
            lista.add(current.getElement());
            current = current.getNext();
        }

        return lista;
    }

    // ========== GERENCIAMENTO DE FILAS VIRTUAIS ==========

    public FilaVirtual obterFilaVirtual(Atracao atracao) {
        Node<FilaVirtual> current = this.filasVirtuais.getHead();

        while (current != null) {
            FilaVirtual fila = current.getElement();
            if (fila.getAtracao().equals(atracao)) {
                return fila;
            }
            current = current.getNext();
        }

        return null;
    }

    public void adicionarVisitanteNaFila(Visitante visitante, Atracao atracao) {
        FilaVirtual fila = obterFilaVirtual(atracao);

        if (fila == null) {
            throw new IllegalArgumentException("Atração não encontrada no sistema");
        }

        if (!atracao.isDisponivelParaVisitante(visitante)) {
            throw new IllegalArgumentException(atracao.getMotivoIndisponibilidade(visitante));
        }

        // Cria a reserva
        Reserva reserva = new Reserva(visitante, atracao);
        this.reservas.addLast(reserva);

        // Adiciona na fila
        fila.adicionarVisitante(visitante);
    }

    public void removerVisitanteDaFila(Visitante visitante, Atracao atracao) {
        FilaVirtual fila = obterFilaVirtual(atracao);

        if (fila == null) {
            throw new IllegalArgumentException("Atração não encontrada");
        }

        fila.getFila().remove(visitante);

        // Cancela a reserva ativa
        cancelarReservaAtiva(visitante, atracao);
    }

    public int consultarPosicaoNaFila(Visitante visitante, Atracao atracao) {
        FilaVirtual fila = obterFilaVirtual(atracao);

        if (fila == null) {
            return -1;
        }

        return fila.consultarPosicao(visitante);
    }

    public int estimarTempoEspera(Visitante visitante, Atracao atracao) {
        FilaVirtual fila = obterFilaVirtual(atracao);

        if (fila == null) {
            return 0;
        }

        return fila.estimarTempoEspera(visitante);
    }

    public List<FilaVirtual> listarFilasVirtuais() {
        List<FilaVirtual> lista = new ArrayList<>();
        Node<FilaVirtual> current = this.filasVirtuais.getHead();

        while (current != null) {
            lista.add(current.getElement());
            current = current.getNext();
        }

        return lista;
    }

    // ========== GERENCIAMENTO DE RESERVAS ==========

    public List<Reserva> obterReservasVisitante(Visitante visitante) {
        List<Reserva> reservasVisitante = new ArrayList<>();
        Node<Reserva> current = this.reservas.getHead();

        while (current != null) {
            Reserva reserva = current.getElement();
            if (reserva.getVisitante().equals(visitante)) {
                reservasVisitante.add(reserva);
            }
            current = current.getNext();
        }

        return reservasVisitante;
    }

    public List<Reserva> obterReservasAtivasVisitante(Visitante visitante) {
        List<Reserva> ativas = new ArrayList<>();
        Node<Reserva> current = this.reservas.getHead();

        while (current != null) {
            Reserva reserva = current.getElement();
            if (reserva.getVisitante().equals(visitante) && reserva.isAtiva()) {
                ativas.add(reserva);
            }
            current = current.getNext();
        }

        return ativas;
    }

    private void cancelarReservaAtiva(Visitante visitante, Atracao atracao) {
        Node<Reserva> current = this.reservas.getHead();

        while (current != null) {
            Reserva reserva = current.getElement();
            if (reserva.getVisitante().equals(visitante) &&
                    reserva.getAtracao().equals(atracao) &&
                    reserva.isAtiva()) {
                reserva.cancelar();
                return;
            }
            current = current.getNext();
        }
    }

    public List<Reserva> listarTodasReservas() {
        List<Reserva> lista = new ArrayList<>();
        Node<Reserva> current = this.reservas.getHead();

        while (current != null) {
            lista.add(current.getElement());
            current = current.getNext();
        }

        return lista;
    }

    // ========== PROCESSAMENTO DE SESSÕES ==========

    public List<Visitante> processarProximaSessao(Atracao atracao) {
        FilaVirtual fila = obterFilaVirtual(atracao);

        if (fila == null) {
            throw new IllegalArgumentException("Atração não encontrada");
        }

        List<Visitante> atendidos = fila.atenderProximaSessao();

        // Marca as reservas como concluídas
        for (Visitante visitante : atendidos) {
            Node<Reserva> current = this.reservas.getHead();

            while (current != null) {
                Reserva reserva = current.getElement();
                if (reserva.getVisitante().equals(visitante) &&
                        reserva.getAtracao().equals(atracao) &&
                        reserva.isAtiva()) {
                    reserva.concluir();
                    break;
                }
                current = current.getNext();
            }
        }

        return atendidos;
    }

    // ========== GETTERS ==========

    public LinkedList<Atracao> getAtracoes() {
        return atracoes;
    }

    public LinkedList<Visitante> getVisitantes() {
        return visitantes;
    }

    public LinkedList<FilaVirtual> getFilasVirtuais() {
        return filasVirtuais;
    }

    public LinkedList<Reserva> getReservas() {
        return reservas;
    }

    public Estatisticas getEstatisticas() {
        return estatisticas;
    }
}