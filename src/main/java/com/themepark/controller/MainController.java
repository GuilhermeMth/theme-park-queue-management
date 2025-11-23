package com.themepark.controller;

import com.themepark.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent; // Novo import necessário
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node; // Novo import necessário para fechar a janela
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MainController {

    @FXML private TableView<Atracao> tabelaAtracoes;
    @FXML private TableColumn<Atracao, String> colNome;
    @FXML private TableColumn<Atracao, String> colTipo;
    @FXML private TableColumn<Atracao, Integer> colCapacidade;
    @FXML private TableColumn<Atracao, Integer> colIdadeMinima;
    @FXML private TableColumn<Atracao, String> colPrioridade;

    @FXML private TableView<FilaVirtual> tabelaFilas;
    @FXML private TableColumn<FilaVirtual, String> colAtracaoFila;
    @FXML private TableColumn<FilaVirtual, Integer> colTamanhoFila;
    @FXML private TableColumn<FilaVirtual, Integer> colTempoEspera;

    @FXML private Label lblTotalVisitantes;
    @FXML private Label lblTotalAtracoes;
    @FXML private Label lblReservasAtivas;
    @FXML private Label lblReservasHoje;
    @FXML private Label lblPessoasEmFilas;
    @FXML private Label lblAtracaoMaisDisputa;
    @FXML private Label lblVisitanteMaisAtivo;

    @FXML private TextArea txtRelatorio;
    @FXML private Button btnProcessarSessao;

    private SistemaParque sistema;

    @FXML
    public void initialize() {
        sistema = SistemaParque.getInstance();

        configurarTabelaAtracoes();
        configurarTabelaFilas();
        carregarDados();
        atualizarEstatisticas();
    }

    private void configurarTabelaAtracoes() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colTipo.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipo().toString()));
        colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidadePorSessao"));
        colIdadeMinima.setCellValueFactory(new PropertyValueFactory<>("faixaEtariaMininima"));
        colPrioridade.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPrioridadeAceita().toString()));
    }

    private void configurarTabelaFilas() {
        colAtracaoFila.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAtracao().getNome()));
        colTamanhoFila.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getFila().getSize()));
        colTempoEspera.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTempoEsperaMinutos()));
    }

    private void carregarDados() {
        // Carregar atrações
        List<Atracao> atracoes = sistema.listarAtracoes();
        ObservableList<Atracao> atracoesObservable = FXCollections.observableArrayList(atracoes);
        tabelaAtracoes.setItems(atracoesObservable);

        // Carregar filas
        List<FilaVirtual> filas = sistema.listarFilasVirtuais();
        ObservableList<FilaVirtual> filasObservable = FXCollections.observableArrayList(filas);
        tabelaFilas.setItems(filasObservable);
    }

    private void atualizarEstatisticas() {
        Estatisticas stats = sistema.getEstatisticas();

        lblTotalVisitantes.setText(String.valueOf(stats.getTotalVisitantes()));
        lblTotalAtracoes.setText(String.valueOf(stats.getTotalAtracoes()));
        lblReservasAtivas.setText(String.valueOf(stats.getTotalReservasAtivas()));
        lblReservasHoje.setText(String.valueOf(stats.getTotalReservasHoje()));
        lblPessoasEmFilas.setText(String.valueOf(stats.getTotalPessoasEmFilas()));

        Atracao maisDisputa = stats.getAtracaoMaisDisputadaHoje();
        lblAtracaoMaisDisputa.setText(maisDisputa != null ? maisDisputa.getNome() : "N/A");

        Visitante maisAtivo = stats.getVisitanteMaisAtivoHoje();
        lblVisitanteMaisAtivo.setText(maisAtivo != null ? maisAtivo.getNome() : "N/A");
    }

    @FXML
    private void abrirCadastroAtracao() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/themepark/view/cadastro-atracao-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cadastrar Atração");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            carregarDados();
            atualizarEstatisticas();
        } catch (IOException e) {
            mostrarErro("Erro ao abrir cadastro de atração", e.getMessage());
        }
    }

    @FXML
    private void abrirCadastroVisitante() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/themepark/view/cadastro-visitante-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cadastrar Visitante");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            atualizarEstatisticas();
        } catch (IOException e) {
            mostrarErro("Erro ao abrir cadastro de visitante", e.getMessage());
        }
    }

    @FXML
    private void abrirPortalVisitante() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/themepark/view/portal-visitante-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Portal do Visitante");
            stage.setScene(new Scene(root, 900, 700));
            stage.show();
        } catch (IOException e) {
            mostrarErro("Erro ao abrir portal do visitante", e.getMessage());
        }
    }

    @FXML
    private void processarSessaoSelecionada() {
        FilaVirtual filaSelecionada = tabelaFilas.getSelectionModel().getSelectedItem();

        if (filaSelecionada == null) {
            mostrarAviso("Nenhuma fila selecionada", "Selecione uma fila para processar a próxima sessão.");
            return;
        }

        if (filaSelecionada.getFila().getSize() == 0) {
            mostrarAviso("Fila vazia", "A fila selecionada está vazia.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Processar Sessão");
        confirmacao.setHeaderText("Processar próxima sessão de " + filaSelecionada.getAtracao().getNome() + "?");
        confirmacao.setContentText("Isso atenderá até " + filaSelecionada.getAtracao().getCapacidadePorSessao() + " pessoas.");

        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            List<Visitante> atendidos = sistema.processarProximaSessao(filaSelecionada.getAtracao());

            StringBuilder mensagem = new StringBuilder();
            mensagem.append("Sessão processada com sucesso!\n\n");
            mensagem.append("Visitantes atendidos (").append(atendidos.size()).append("):\n");
            for (Visitante v : atendidos) {
                mensagem.append("- ").append(v.getNome()).append("\n");
            }

            mostrarInformacao("Sessão Processada", mensagem.toString());

            carregarDados();
            atualizarEstatisticas();
        }
    }

    @FXML
    private void gerarRelatorio() {
        String relatorio = sistema.getEstatisticas().gerarRelatorioCompleto();
        txtRelatorio.setText(relatorio);
    }

    @FXML
    private void removerAtracao() {
        Atracao selecionada = tabelaAtracoes.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            mostrarAviso("Nenhuma atração selecionada", "Selecione uma atração para remover.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Remover Atração");
        confirmacao.setHeaderText("Remover atração " + selecionada.getNome() + "?");
        confirmacao.setContentText("Esta ação não pode ser desfeita.");

        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            sistema.removerAtracao(selecionada.getNome());
            carregarDados();
            atualizarEstatisticas();
            mostrarInformacao("Sucesso", "Atração removida com sucesso!");
        }
    }

    @FXML
    private void atualizarDados() {
        carregarDados();
        atualizarEstatisticas();
        mostrarInformacao("Atualizado", "Dados atualizados com sucesso!");
    }

    // ===============================================
    // NOVO MÉTODO PARA SAIR DA APLICAÇÃO
    // ===============================================

    @FXML
    public void sairAplicacao(ActionEvent event) {
        // 1. O método precisa ser 'public' e ter a anotação '@FXML'.
        // 2. Ele recebe um ActionEvent para descobrir qual componente (Node) o disparou.

        // Exibe uma confirmação
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Sair da Aplicação");
        confirmacao.setHeaderText("Tem certeza que deseja sair?");
        confirmacao.setContentText("Todos os dados não salvos serão perdidos (se aplicável).");

        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            // Obtém o Stage (janela) do componente que disparou o evento.
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            // Fecha o Stage principal
            stage.close();

            // Opcional: Para garantir que o programa Java encerre completamente
            // Platform.exit();
            // System.exit(0);
        }
    }

    // ===============================================
    // MÉTODOS DE UTILIDADE (ALERTAS)
    // ===============================================

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