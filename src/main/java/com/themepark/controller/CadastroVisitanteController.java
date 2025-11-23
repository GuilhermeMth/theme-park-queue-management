package com.themepark.controller;

import com.themepark.model.SistemaParque;
import com.themepark.model.TipoIngresso;
import com.themepark.model.Visitante;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CadastroVisitanteController {

    @FXML private TextField txtNome;
    @FXML private TextField txtCpf;
    @FXML private DatePicker dataNascimento;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<TipoIngresso> cmbTipoIngresso;

    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;
    @FXML private Button btnLimpar;

    private SistemaParque sistema;

    @FXML
    public void initialize() {
        sistema = SistemaParque.getInstance();

        configurarComboBox();
        configurarMascaraCpf();
    }

    private void configurarComboBox() {
        cmbTipoIngresso.setItems(FXCollections.observableArrayList(TipoIngresso.values()));
        cmbTipoIngresso.getSelectionModel().select(TipoIngresso.COMUM);
    }

    private void configurarMascaraCpf() {
        // Adiciona máscara de CPF: 000.000.000-00
        txtCpf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() > 14) {
                txtCpf.setText(oldValue);
            }
        });
    }

    @FXML
    private void salvarVisitante() {
        try {
            // Validações
            if (!validarCampos()) {
                return;
            }

            String nome = txtNome.getText().trim();
            String cpf = txtCpf.getText().replaceAll("[^0-9]", "");
            LocalDate dataNasc = dataNascimento.getValue();
            String email = txtEmail.getText().trim();
            TipoIngresso tipoIngresso = cmbTipoIngresso.getValue();

            // Criar visitante
            Visitante novoVisitante = new Visitante(cpf, dataNasc, email, nome, tipoIngresso);

            // Cadastrar no sistema
            sistema.cadastrarVisitante(novoVisitante);

            mostrarInformacao("Sucesso", "Visitante cadastrado com sucesso!\n\nNome: " + nome + "\nCPF: " + formatarCpf(cpf));
            fecharJanela();

        } catch (IllegalArgumentException e) {
            mostrarErro("Erro de Validação", e.getMessage());
        } catch (Exception e) {
            mostrarErro("Erro ao cadastrar visitante", e.getMessage());
        }
    }

    private boolean validarCampos() {
        // Nome
        if (txtNome.getText() == null || txtNome.getText().trim().isEmpty()) {
            mostrarErro("Campo Obrigatório", "O nome é obrigatório.");
            txtNome.requestFocus();
            return false;
        }

        // CPF
        if (txtCpf.getText() == null || txtCpf.getText().trim().isEmpty()) {
            mostrarErro("Campo Obrigatório", "O CPF é obrigatório.");
            txtCpf.requestFocus();
            return false;
        }

        String cpfNumeros = txtCpf.getText().replaceAll("[^0-9]", "");
        if (cpfNumeros.length() != 11) {
            mostrarErro("CPF Inválido", "O CPF deve conter 11 dígitos.");
            txtCpf.requestFocus();
            return false;
        }

        // Data de Nascimento
        if (dataNascimento.getValue() == null) {
            mostrarErro("Campo Obrigatório", "A data de nascimento é obrigatória.");
            dataNascimento.requestFocus();
            return false;
        }

        if (dataNascimento.getValue().isAfter(LocalDate.now())) {
            mostrarErro("Data Inválida", "A data de nascimento não pode ser futura.");
            dataNascimento.requestFocus();
            return false;
        }

        // Email
        if (txtEmail.getText() == null || txtEmail.getText().trim().isEmpty()) {
            mostrarErro("Campo Obrigatório", "O email é obrigatório.");
            txtEmail.requestFocus();
            return false;
        }

        if (!txtEmail.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            mostrarErro("Email Inválido", "Digite um email válido.");
            txtEmail.requestFocus();
            return false;
        }

        // Tipo de Ingresso
        if (cmbTipoIngresso.getValue() == null) {
            mostrarErro("Campo Obrigatório", "Selecione o tipo de ingresso.");
            cmbTipoIngresso.requestFocus();
            return false;
        }

        return true;
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
        txtCpf.clear();
        dataNascimento.setValue(null);
        txtEmail.clear();
        cmbTipoIngresso.getSelectionModel().select(TipoIngresso.COMUM);
        txtNome.requestFocus();
    }

    @FXML
    private void formatarCpfAutomatico() {
        String texto = txtCpf.getText().replaceAll("[^0-9]", "");

        if (texto.length() > 11) {
            texto = texto.substring(0, 11);
        }

        if (texto.length() > 0) {
            txtCpf.setText(formatarCpf(texto));
        }
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

    private void mostrarInformacao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}