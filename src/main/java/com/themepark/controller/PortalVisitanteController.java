package com.themepark.controller;

import com.themepark.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class PortalVisitanteController {

    @FXML private TextField txtBuscaCpf;
    @FXML private Button btnBuscarVisitante;
    @FXML private Label lblNomeVisitante;
    @FXML private Label lblCpfVisitante;
    @FXML private Label lblIdadeVisitante;
    @FXML private Label lblTipoIngresso;

    @FXML private TableView<Atracao> tabelaAtracoesDisponiveis;
    @FXML private TableColumn<Atracao, String> colNomeAtracao;
    @FXML private TableColumn<Atracao, String> colTipoAtracao;
    @FXML private TableColumn<Atracao, Integer> colCapacidade;
    @FXML private TableColumn<Atracao, Integer> colIdadeMin;
    @FXML private TableColumn<Atracao, String> colPrioridade;

    @FXML private ListView<String> listaMinhasFilas;
    @FXML private TextArea txtDetalhesEspera;

    @FXML private TableView<Reserva> tabelaHistorico;
    @FXML private TableColumn<Reserva, String> colAtracaoReserva;
    @FXML private TableColumn<Reserva, String> colDataReserva;
    @FXML private TableColumn<Reserva, String> colStatusReserva;

    @FXML private Button btnEntrarFila;
    @FXML private Button btnSairFila;
    @FXML private Button btnConsultarPosicao;
    @FXML private Button btnAtualizarDados;

    private SistemaParque sistema;
    private Visitante visitanteAtual;

    @FXML
    public void initialize() {
        sistema = SistemaParque.getInstance();

        configurarTabelaAtracoes();
        configurarTabelaHistorico();
        desabilitarFuncionalidades();
    }

    private void configurarTabelaAtracoes() {
        colNomeAtracao.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colTipoAtracao.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipo().toString()));
        colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidadePorSessao"));
        colIdadeMin.setCellValueFactory(new PropertyValueFactory<>("faixaEtariaMininima"));
        colPrioridade.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPrioridadeAceita().toString()));
    }

    private void configurarTabelaHistorico() {
        colAtracaoReserva.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAtracao().getNome()));
        colDataReserva.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDataHoraCriacaoFormatada()));
        colStatusReserva.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus().toString()));
    }

    @FXML
    private void buscarVisitante() {
        String cpf = txtBuscaCpf.getText().replaceAll("[^0-9]", "");

        if (cpf.isEmpty()) {
            mostrarErro("Campo Vazio", "Digite o CPF do visitante.");
            return;
        }

        if (cpf.length() != 11) {
            mostrarErro("CPF Inválido", "O CPF deve conter 11 dígitos.");
            return;
        }

        Visitante visitante = sistema.buscarVisitante(cpf);

        if (visitante == null) {
            mostrarErro("Visitante Não Encontrado", "Não existe visitante cadastrado com este CPF.");
            limparDadosVisitante();
            return;
        }

        visitanteAtual = visitante;
        carregarDadosVisitante();
        habilitarFuncionalidades();
    }

    private void carregarDadosVisitante() {
        lblNomeVisitante.setText(visitanteAtual.getNome());
        lblCpfVisitante.setText(formatarCpf(visitanteAtual.getCpf()));
        lblIdadeVisitante.setText(visitanteAtual.calcularIdade() + " anos");
        lblTipoIngresso.setText(visitanteAtual.getTipoIngresso().toString());

        carregarAtracoesDisponiveis();
        carregarMinhasFilas();
        carregarHistorico();
    }

    private void carregarAtracoesDisponiveis() {
        List<Atracao> disponiveis = sistema.listarAtracoesDisponiveisParaVisitante(visitanteAtual);
        ObservableList<Atracao> atracoesObservable = FXCollections.observableArrayList(disponiveis);
        tabelaAtracoesDisponiveis.setItems(atracoesObservable);
    }

    private void carregarMinhasFilas() {
        ObservableList<String> filasTexto = FXCollections.observableArrayList();
        List<Reserva> reservasAtivas = sistema.obterReservasAtivasVisitante(visitanteAtual);

        for (Reserva reserva : reservasAtivas) {
            Atracao atracao = reserva.getAtracao();
            int posicao = sistema.consultarPosicaoNaFila(visitanteAtual, atracao);
            int tempoEspera = sistema.estimarTempoEspera(visitanteAtual, atracao);

            String texto = String.format("%s - Posição: %d - Tempo: %d min",
                    atracao.getNome(), posicao, tempoEspera);
            filasTexto.add(texto);
        }

        listaMinhasFilas.setItems(filasTexto);
    }

    private void carregarHistorico() {
        List<Reserva> reservas = sistema.obterReservasVisitante(visitanteAtual);
        ObservableList<Reserva> reservasObservable = FXCollections.observableArrayList(reservas);
        tabelaHistorico.setItems(reservasObservable);
    }

    @FXML
    private void entrarNaFila() {
        if (visitanteAtual == null) {
            mostrarErro("Erro", "Nenhum visitante selecionado.");
            return;
        }

        Atracao atracaoSelecionada = tabelaAtracoesDisponiveis.getSelectionModel().getSelectedItem();

        if (atracaoSelecionada == null) {
            mostrarAviso("Nenhuma atração selecionada", "Selecione uma atração para entrar na fila.");
            return;
        }

        // Verifica se já está na fila
        List<Reserva> reservasAtivas = sistema.obterReservasAtivasVisitante(visitanteAtual);
        for (Reserva r : reservasAtivas) {
            if (r.getAtracao().equals(atracaoSelecionada)) {
                mostrarAviso("Já está na fila", "Você já está aguardando nesta atração.");
                return;
            }
        }

        try {
            sistema.adicionarVisitanteNaFila(visitanteAtual, atracaoSelecionada);

            int posicao = sistema.consultarPosicaoNaFila(visitanteAtual, atracaoSelecionada);
            int tempoEspera = sistema.estimarTempoEspera(visitanteAtual, atracaoSelecionada);

            mostrarInformacao("Sucesso",
                    String.format("Você entrou na fila de %s!\n\nPosição: %d\nTempo estimado: %d minutos",
                            atracaoSelecionada.getNome(), posicao, tempoEspera));

            carregarMinhasFilas();
            carregarHistorico();

        } catch (IllegalArgumentException e) {
            mostrarErro("Erro ao entrar na fila", e.getMessage());
        }
    }

    @FXML
    private void sairDaFila() {
        if (visitanteAtual == null) {
            mostrarErro("Erro", "Nenhum visitante selecionado.");
            return;
        }

        String filaSelecionada = listaMinhasFilas.getSelectionModel().getSelectedItem();

        if (filaSelecionada == null) {
            mostrarAviso("Nenhuma fila selecionada", "Selecione uma fila para sair.");
            return;
        }

        // Extrai o nome da atração da string
        String nomeAtracao = filaSelecionada.split(" - ")[0];
        Atracao atracao = sistema.buscarAtracao(nomeAtracao);

        if (atracao == null) {
            mostrarErro("Erro", "Atração não encontrada.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Sair da Fila");
        confirmacao.setHeaderText("Deseja realmente sair da fila de " + nomeAtracao + "?");
        confirmacao.setContentText("Esta ação não pode ser desfeita.");

        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            sistema.removerVisitanteDaFila(visitanteAtual, atracao);
            mostrarInformacao("Sucesso", "Você saiu da fila de " + nomeAtracao);
            carregarMinhasFilas();
            carregarHistorico();
        }
    }

    @FXML
    private void consultarPosicao() {
        if (visitanteAtual == null) {
            mostrarErro("Erro", "Nenhum visitante selecionado.");
            return;
        }

        String filaSelecionada = listaMinhasFilas.getSelectionModel().getSelectedItem();

        if (filaSelecionada == null) {
            mostrarAviso("Nenhuma fila selecionada", "Selecione uma fila para consultar.");
            return;
        }

        String nomeAtracao = filaSelecionada.split(" - ")[0];
        Atracao atracao = sistema.buscarAtracao(nomeAtracao);

        if (atracao == null) {
            mostrarErro("Erro", "Atração não encontrada.");
            return;
        }

        int posicao = sistema.consultarPosicaoNaFila(visitanteAtual, atracao);
        int tempoEspera = sistema.estimarTempoEspera(visitanteAtual, atracao);
        FilaVirtual fila = sistema.obterFilaVirtual(atracao);

        StringBuilder detalhes = new StringBuilder();
        detalhes.append("═══════════════════════════════════\n");
        detalhes.append("DETALHES DA FILA\n");
        detalhes.append("═══════════════════════════════════\n\n");
        detalhes.append("Atração: ").append(atracao.getNome()).append("\n");
        detalhes.append("Sua Posição: ").append(posicao).append("\n");
        detalhes.append("Tempo Estimado: ").append(tempoEspera).append(" minutos\n");
        detalhes.append("Tamanho da Fila: ").append(fila.getFila().getSize()).append(" pessoas\n");
        detalhes.append("Capacidade por Sessão: ").append(atracao.getCapacidadePorSessao()).append(" pessoas\n");
        detalhes.append("Duração da Sessão: ").append(atracao.getDuracaoSessaoMinutos()).append(" minutos\n");
        detalhes.append("\n═══════════════════════════════════");

        txtDetalhesEspera.setText(detalhes.toString());
    }

    @FXML
    private void atualizarDados() {
        if (visitanteAtual != null) {
            carregarDadosVisitante();
            mostrarInformacao("Atualizado", "Dados atualizados com sucesso!");
        }
    }

    private void limparDadosVisitante() {
        visitanteAtual = null;
        lblNomeVisitante.setText("-");
        lblCpfVisitante.setText("-");
        lblIdadeVisitante.setText("-");
        lblTipoIngresso.setText("-");
        tabelaAtracoesDisponiveis.getItems().clear();
        listaMinhasFilas.getItems().clear();
        tabelaHistorico.getItems().clear();
        txtDetalhesEspera.clear();
        desabilitarFuncionalidades();
    }

    private void habilitarFuncionalidades() {
        btnEntrarFila.setDisable(false);
        btnSairFila.setDisable(false);
        btnConsultarPosicao.setDisable(false);
        btnAtualizarDados.setDisable(false);
    }

    private void desabilitarFuncionalidades() {
        btnEntrarFila.setDisable(true);
        btnSairFila.setDisable(true);
        btnConsultarPosicao.setDisable(true);
        btnAtualizarDados.setDisable(true);
    }

    private String formatarCpf(String cpf) {
        if (cpf.length() == 11) {
            return cpf.substring(0, 3) + "." +
                    cpf.substring(3, 6) + "." +
                    cpf.substring(6, 9) + "-" +
                    cpf.substring(9, 11);
        }
        return cpf;
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAviso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarInformacao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}