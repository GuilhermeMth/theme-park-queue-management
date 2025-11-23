package com.themepark.model;

import com.themepark.model.datastructures.LinkedList;
import com.themepark.model.datastructures.Node;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
                "atracao=" + atracao.getNome() +
                ", tamanhoFila=" + fila.getSize() +
                ", tempoEsperaMinutos=" + tempoEsperaMinutos +
                ", ultimaSessao=" + ultimaSessao +
                '}';
    }

    // CORRIGIDO: Método agora consistente - usa apenas valores numéricos
    private int convertePrioridadeParaNumero(NivelPrioridade nivel) {
        switch (nivel) {
            case PASSE_ELITE:
                return 3;
            case PASSE_PREMIUM:
                return 2;
            default:
                return 1;
        }
    }

    // CORRIGIDO: Lógica de adição agora completamente consistente
    public void adicionarVisitante(Visitante visitante) {
        int nivelVisitante = visitante.getTipoIngresso().getNivelPrioridade();

        NivelPrioridade minimoAceitoEnum = this.atracao.getPrioridadeAceita();
        int nivelMinimo = convertePrioridadeParaNumero(minimoAceitoEnum);

        if (nivelVisitante < nivelMinimo) {
            throw new IllegalArgumentException("Ingresso (" + visitante.getTipoIngresso() +
                    ") não atende ao mínimo (" + minimoAceitoEnum + ") para esta fila.");
        }

        // Se a fila está vazia ou é ingresso comum, adiciona no final
        if (this.fila.getSize() == 0 || nivelVisitante == TipoIngresso.COMUM.getNivelPrioridade()) {
            this.fila.addLast(visitante);
            return;
        }

        // Procura a posição correta baseada na prioridade
        int index = 0;
        Node<Visitante> current = this.fila.getHead();

        while (current != null) {
            int nivelAtual = current.getElement().getTipoIngresso().getNivelPrioridade();

            // Se o novo visitante tem maior prioridade, insere aqui
            if (nivelVisitante > nivelAtual) {
                break;
            }

            current = current.getNext();
            index++;
        }

        this.fila.add(index, visitante);
    }

    public List<Visitante> atenderProximaSessao() {
        int capacidade = this.atracao.getCapacidadePorSessao();
        List<Visitante> atendidos = new ArrayList<>();

        for (int i = 0; i < capacidade; i++) {
            if (this.fila.getSize() > 0) {
                try {
                    Visitante atendido = this.fila.removeFirst();
                    atendidos.add(atendido);
                } catch (NoSuchElementException e) {
                    break;
                }
            } else {
                break;
            }
        }

        this.ultimaSessao = LocalTime.now();
        return atendidos;
    }

    public int estimarTempoEspera(Visitante v) {
        int index = this.fila.getIndexOf(v);

        if (index == -1) {
            return 0;
        }

        double posicao = index + 1;
        double capacidade = (double) this.atracao.getCapacidadePorSessao();
        double tempoSessao = (double) this.atracao.getDuracaoSessaoMinutos();
        double numSessoes = Math.ceil(posicao / capacidade);

        return (int) (numSessoes * tempoSessao);
    }

    public int consultarPosicao(Visitante v) {
        int index = this.fila.getIndexOf(v);

        if (index >= 0) {
            return index + 1;
        }
        return -1;
    }
}