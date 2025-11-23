package com.themepark.controller;

import com.themepark.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CadastroAtracaoController {

    @FXML private TextField txtNome;
    @FXML private ComboBox<TipoAtracao> cmbTipo;
    @FXML private Spinner<Integer> spnCapacidade;
    @FXML private Spinner<Integer> spnIdadeMinima;
    @FXML private ComboBox<NivelPrioridade> cmbPrioridade;

    @FXML private Spinner<Integer> spnHoraInicio;
    @FXML private Spinner<Integer> spnMinutoInicio;
    @FXML private Spinner<Integer> spnHoraFim;
    @FXML private Spinner<Integer> spnMinutoFim;

    @FXML private ListView<HorarioSessao> listaHorarios;
    @FXML private Button btnAdicionarHorario;
    @FXML private Button btnRemoverHorario;
    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;

    private SistemaParque sistema;
    private List<HorarioSessao> horariosAdicionados;

    @FXML
    public void initialize() {
        sistema = SistemaParque.getInstance();
        horariosAdicionados = new ArrayList<>();

        configurarComboBoxes();
        configurarSpinners();
    }

    private void configurarComboBoxes() {
        // ComboBox de Tipo de Atração
        cmbTipo.setItems(FXCollections.observableArrayList(TipoAtracao.values()));
        cmbTipo.getSelectionModel().selectFirst();

        // ComboBox de Prioridade
        cmbPrioridade.setItems(FXCollections.observableArrayList(NivelPrioridade.values()));
        cmbPrioridade.getSelectionModel().select(NivelPrioridade.PASSE_COMUM);
    }

    private void configurarSpinners() {
        // Spinner de Capacidade (1-500)
        SpinnerValueFactory<Integer> capacidadeFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 30);
        spnCapacidade.setValueFactory(capacidadeFactory);
        spnCapacidade.setEditable(true);

        // Spinner de Idade Mínima (0-18)
        SpinnerValueFactory<Integer> idadeFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 18, 0);
        spnIdadeMinima.setValueFactory(idadeFactory);
        spnIdadeMinima.setEditable(true);

        // Spinners de Horário Início
        SpinnerValueFactory<Integer> horaInicioFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9);
        spnHoraInicio.setValueFactory(horaInicioFactory);
        spnHoraInicio.setEditable(true);

        SpinnerValueFactory<Integer> minutoInicioFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 15);
        spnMinutoInicio.setValueFactory(minutoInicioFactory);
        spnMinutoInicio.setEditable(true);

        // Spinners de Horário Fim
        SpinnerValueFactory<Integer> horaFimFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 10);
        spnHoraFim.setValueFactory(horaFimFactory);
        spnHoraFim.setEditable(true);

        SpinnerValueFactory<Integer> minutoFimFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 15);
        spnMinutoFim.setValueFactory(minutoFimFactory);
        spnMinutoFim.setEditable(true);
    }

    @FXML
    private void adicionarHorario() {
        try {
            LocalTime inicio = LocalTime.of(
                    spnHoraInicio.getValue(),
                    spnMinutoInicio.getValue()
            );

            LocalTime fim = LocalTime.of(
                    spnHoraFim.getValue(),
                    spnMinutoFim.getValue()
            );

            if (inicio.isAfter(fim) || inicio.equals(fim)) {
                mostrarErro("Horário Inválido", "O horário de início deve ser anterior ao horário de fim.");
                return;
            }

            HorarioSessao novoHorario = new HorarioSessao(inicio, fim);

            // Verifica se já existe
            if (horariosAdicionados.contains(novoHorario)) {
                mostrarAviso("Horário Duplicado", "Este horário já foi adicionado.");
                return;
            }

            horariosAdicionados.add(novoHorario);
            atualizarListaHorarios();

            mostrarInformacao("Sucesso", "Horário adicionado: " + inicio + " - " + fim);

        } catch (Exception e) {
            mostrarErro("Erro ao adicionar horário", e.getMessage());
        }
    }

    @FXML
    private void removerHorario() {
        HorarioSessao selecionado = listaHorarios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAviso("Nenhum horário selecionado", "Selecione um horário para remover.");
            return;
        }

        horariosAdicionados.remove(selecionado);
        atualizarListaHorarios();
        mostrarInformacao("Sucesso", "Horário removido.");
    }

    private void atualizarListaHorarios() {
        ObservableList<HorarioSessao> horarios = FXCollections.observableArrayList(horariosAdicionados);
        listaHorarios.setItems(horarios);
    }

    @FXML
    private void salvarAtracao() {
        try {
            // Validações
            if (txtNome.getText() == null || txtNome.getText().trim().isEmpty()) {
                mostrarErro("Campo Obrigatório", "O nome da atração é obrigatório.");
                return;
            }

            if (cmbTipo.getValue() == null) {
                mostrarErro("Campo Obrigatório", "Selecione o tipo da atração.");
                return;
            }

            if (cmbPrioridade.getValue() == null) {
                mostrarErro("Campo Obrigatório", "Selecione o nível de prioridade.");
                return;
            }

            if (horariosAdicionados.isEmpty()) {
                mostrarAviso("Sem Horários", "Adicione pelo menos um horário de sessão.");
                return;
            }

            // Criar a atração
            Atracao novaAtracao = new Atracao(
                    spnCapacidade.getValue(),
                    spnIdadeMinima.getValue(),
                    txtNome.getText().trim(),
                    cmbTipo.getValue(),
                    cmbPrioridade.getValue()
            );

            // Adicionar horários
            for (HorarioSessao horario : horariosAdicionados) {
                novaAtracao.adicionarHorario(horario.getHoraInicio(), horario.getHoraFim());
            }

            // Cadastrar no sistema
            sistema.cadastrarAtracao(novaAtracao);

            mostrarInformacao("Sucesso", "Atração cadastrada com sucesso!");
            fecharJanela();

        } catch (IllegalArgumentException e) {
            mostrarErro("Erro de Validação", e.getMessage());
        } catch (Exception e) {
            mostrarErro("Erro ao cadastrar atração", e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Cancelar Cadastro");
        confirmacao.setHeaderText("Deseja realmente cancelar o cadastro?");
        confirmacao.setContentText("Todos os dados preenchidos serão perdidos.");

        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            fecharJanela();
        }
    }

    @FXML
    private void limparCampos() {
        txtNome.clear();
        cmbTipo.getSelectionModel().selectFirst();
        cmbPrioridade.getSelectionModel().select(NivelPrioridade.PASSE_COMUM);

        spnCapacidade.getValueFactory().setValue(30);
        spnIdadeMinima.getValueFactory().setValue(0);

        spnHoraInicio.getValueFactory().setValue(9);
        spnMinutoInicio.getValueFactory().setValue(0);
        spnHoraFim.getValueFactory().setValue(10);
        spnMinutoFim.getValueFactory().setValue(0);

        horariosAdicionados.clear();
        atualizarListaHorarios();
    }

    private void fecharJanela() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
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