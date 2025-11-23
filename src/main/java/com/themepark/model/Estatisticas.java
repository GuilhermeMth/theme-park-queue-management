package com.themepark.model;

import com.themepark.model.datastructures.LinkedList;
import com.themepark.model.datastructures.Node;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Estatisticas {
    private SistemaParque sistema;

    public Estatisticas(SistemaParque sistema) {
        this.sistema = sistema;
    }

    /**
     * Retorna o total de reservas feitas em uma data específica
     */
    public int getTotalReservasDia(LocalDate data) {
        int total = 0;
        LinkedList<Reserva> reservas = sistema.getReservas();
        Node<Reserva> current = reservas.getHead();

        while (current != null) {
            Reserva reserva = current.getElement();
            LocalDate dataReserva = reserva.getDataHoraCriacao().toLocalDate();

            if (dataReserva.equals(data)) {
                total++;
            }

            current = current.getNext();
        }

        return total;
    }

    /**
     * Retorna o total de reservas feitas hoje
     */
    public int getTotalReservasHoje() {
        return getTotalReservasDia(LocalDate.now());
    }

    /**
     * Retorna a atração mais disputada (com mais reservas) do dia
     */
    public Atracao getAtracaoMaisDisputadaDia(LocalDate data) {
        Map<Atracao, Integer> contagemPorAtracao = new HashMap<>();
        LinkedList<Reserva> reservas = sistema.getReservas();
        Node<Reserva> current = reservas.getHead();

        // Conta reservas por atração
        while (current != null) {
            Reserva reserva = current.getElement();
            LocalDate dataReserva = reserva.getDataHoraCriacao().toLocalDate();

            if (dataReserva.equals(data)) {
                Atracao atracao = reserva.getAtracao();
                contagemPorAtracao.put(atracao, contagemPorAtracao.getOrDefault(atracao, 0) + 1);
            }

            current = current.getNext();
        }

        // Encontra a atração com mais reservas
        Atracao maisDisputa = null;
        int maxReservas = 0;

        for (Map.Entry<Atracao, Integer> entry : contagemPorAtracao.entrySet()) {
            if (entry.getValue() > maxReservas) {
                maxReservas = entry.getValue();
                maisDisputa = entry.getKey();
            }
        }

        return maisDisputa;
    }

    /**
     * Retorna a atração mais disputada de hoje
     */
    public Atracao getAtracaoMaisDisputadaHoje() {
        return getAtracaoMaisDisputadaDia(LocalDate.now());
    }

    /**
     * Retorna o visitante que mais usou o sistema (mais reservas) no dia
     */
    public Visitante getVisitanteMaisAtivoDia(LocalDate data) {
        Map<Visitante, Integer> contagemPorVisitante = new HashMap<>();
        LinkedList<Reserva> reservas = sistema.getReservas();
        Node<Reserva> current = reservas.getHead();

        // Conta reservas por visitante
        while (current != null) {
            Reserva reserva = current.getElement();
            LocalDate dataReserva = reserva.getDataHoraCriacao().toLocalDate();

            if (dataReserva.equals(data)) {
                Visitante visitante = reserva.getVisitante();
                contagemPorVisitante.put(visitante, contagemPorVisitante.getOrDefault(visitante, 0) + 1);
            }

            current = current.getNext();
        }

        // Encontra o visitante com mais reservas
        Visitante maisAtivo = null;
        int maxReservas = 0;

        for (Map.Entry<Visitante, Integer> entry : contagemPorVisitante.entrySet()) {
            if (entry.getValue() > maxReservas) {
                maxReservas = entry.getValue();
                maisAtivo = entry.getKey();
            }
        }

        return maisAtivo;
    }

    /**
     * Retorna o visitante mais ativo de hoje
     */
    public Visitante getVisitanteMaisAtivoHoje() {
        return getVisitanteMaisAtivoDia(LocalDate.now());
    }

    /**
     * Retorna um ranking de todas as atrações por número de reservas (histórico completo)
     */
    public Map<Atracao, Integer> getRankingAtracoes() {
        Map<Atracao, Integer> ranking = new HashMap<>();
        LinkedList<Reserva> reservas = sistema.getReservas();
        Node<Reserva> current = reservas.getHead();

        while (current != null) {
            Reserva reserva = current.getElement();
            Atracao atracao = reserva.getAtracao();
            ranking.put(atracao, ranking.getOrDefault(atracao, 0) + 1);

            current = current.getNext();
        }

        return ranking;
    }

    /**
     * Retorna o total de visitantes cadastrados no sistema
     */
    public int getTotalVisitantes() {
        return sistema.getVisitantes().getSize();
    }

    /**
     * Retorna o total de atrações cadastradas no sistema
     */
    public int getTotalAtracoes() {
        return sistema.getAtracoes().getSize();
    }

    /**
     * Retorna o total de reservas ativas no momento
     */
    public int getTotalReservasAtivas() {
        int total = 0;
        LinkedList<Reserva> reservas = sistema.getReservas();
        Node<Reserva> current = reservas.getHead();

        while (current != null) {
            if (current.getElement().isAtiva()) {
                total++;
            }
            current = current.getNext();
        }

        return total;
    }

    /**
     * Retorna o total de pessoas aguardando em todas as filas
     */
    public int getTotalPessoasEmFilas() {
        int total = 0;
        LinkedList<FilaVirtual> filas = sistema.getFilasVirtuais();
        Node<FilaVirtual> current = filas.getHead();

        while (current != null) {
            total += current.getElement().getFila().getSize();
            current = current.getNext();
        }

        return total;
    }

    /**
     * Retorna a fila mais longa no momento
     */
    public FilaVirtual getFilaMaisLonga() {
        FilaVirtual maisLonga = null;
        int tamanhoMax = 0;
        LinkedList<FilaVirtual> filas = sistema.getFilasVirtuais();
        Node<FilaVirtual> current = filas.getHead();

        while (current != null) {
            FilaVirtual fila = current.getElement();
            int tamanho = fila.getFila().getSize();

            if (tamanho > tamanhoMax) {
                tamanhoMax = tamanho;
                maisLonga = fila;
            }

            current = current.getNext();
        }

        return maisLonga;
    }

    /**
     * Retorna a distribuição de tipos de ingresso dos visitantes
     */
    public Map<TipoIngresso, Integer> getDistribuicaoTiposIngresso() {
        Map<TipoIngresso, Integer> distribuicao = new HashMap<>();
        LinkedList<Visitante> visitantes = sistema.getVisitantes();
        Node<Visitante> current = visitantes.getHead();

        // Inicializa o mapa com zeros
        for (TipoIngresso tipo : TipoIngresso.values()) {
            distribuicao.put(tipo, 0);
        }

        // Conta visitantes por tipo de ingresso
        while (current != null) {
            TipoIngresso tipo = current.getElement().getTipoIngresso();
            distribuicao.put(tipo, distribuicao.get(tipo) + 1);
            current = current.getNext();
        }

        return distribuicao;
    }

    /**
     * Retorna a taxa de conclusão de reservas (concluídas / total)
     */
    public double getTaxaConclusaoReservas() {
        int total = 0;
        int concluidas = 0;
        LinkedList<Reserva> reservas = sistema.getReservas();
        Node<Reserva> current = reservas.getHead();

        while (current != null) {
            Reserva reserva = current.getElement();
            total++;

            if (reserva.isConcluida()) {
                concluidas++;
            }

            current = current.getNext();
        }

        return total > 0 ? (double) concluidas / total * 100.0 : 0.0;
    }

    /**
     * Retorna um relatório completo em formato String
     */
    public String gerarRelatorioCompleto() {
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("========== RELATÓRIO DO SISTEMA ==========\n\n");

        relatorio.append("ESTATÍSTICAS GERAIS:\n");
        relatorio.append("- Total de Visitantes: ").append(getTotalVisitantes()).append("\n");
        relatorio.append("- Total de Atrações: ").append(getTotalAtracoes()).append("\n");
        relatorio.append("- Reservas Ativas: ").append(getTotalReservasAtivas()).append("\n");
        relatorio.append("- Pessoas em Filas: ").append(getTotalPessoasEmFilas()).append("\n");
        relatorio.append("- Taxa de Conclusão: ").append(String.format("%.2f%%", getTaxaConclusaoReservas())).append("\n\n");

        relatorio.append("ESTATÍSTICAS DO DIA:\n");
        relatorio.append("- Reservas Hoje: ").append(getTotalReservasHoje()).append("\n");

        Atracao maisDisputa = getAtracaoMaisDisputadaHoje();
        if (maisDisputa != null) {
            relatorio.append("- Atração Mais Disputada: ").append(maisDisputa.getNome()).append("\n");
        }

        Visitante maisAtivo = getVisitanteMaisAtivoHoje();
        if (maisAtivo != null) {
            relatorio.append("- Visitante Mais Ativo: ").append(maisAtivo.getNome()).append("\n");
        }

        FilaVirtual filaMaisLonga = getFilaMaisLonga();
        if (filaMaisLonga != null) {
            relatorio.append("- Fila Mais Longa: ").append(filaMaisLonga.getAtracao().getNome())
                    .append(" (").append(filaMaisLonga.getFila().getSize()).append(" pessoas)\n");
        }

        relatorio.append("\n==========================================");

        return relatorio.toString();
    }
}