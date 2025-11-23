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
                "atracao=" + atracao +
                ", fila=" + fila +
                ", tempoEsperaMinutos=" + tempoEsperaMinutos +
                ", ultimaSessao=" + ultimaSessao +
                '}';
    }

    private int getNivelPrioridadeNumerico(NivelPrioridade nivel) {

        switch (nivel) {
            case PASSE_ELITE: 
                return 3;
            case PASSE_PREMIUM: 
                return 2;
            case PASSE_COMUM: 
                return 1;
            default:
                return 1; 
        }
    }

    public void adicionarVisitante(Visitante visitante) {
        int nivelNovo = visitante.getTipoIngresso().getNivelPrioridade();
        
        NivelPrioridade minimoAceitoEnum = this.atracao.getPrioridadeAceita();
        int nivelMinimo = getNivelPrioridadeNumerico(minimoAceitoEnum);

        if (nivelNovo < nivelMinimo) {
            throw new IllegalArgumentException("Ingresso (" + visitante.getTipoIngresso() + 
                ") não atende ao mínimo (" + minimoAceitoEnum + ") para esta fila.");
        }

        if (this.fila.getSize() == 0 || nivelNovo == TipoIngresso.COMUM.getNivelPrioridade()) {
            this.fila.addLast(visitante);
            return;
        }

        int index = 0;
        Node<Visitante> current = this.fila.getHead(); 

        while (current != null) {
            int nivelAtual = current.getElement().getTipoIngresso().getNivelPrioridade();
            
            if (nivelNovo > nivelAtual) { 
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
}
