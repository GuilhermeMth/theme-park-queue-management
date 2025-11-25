package com.themepark.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller principal da aplicação. 
 * Gerencia a view central (app-central-view.fxml) que contém as abas "Parque" e "Visitante".
 * Lida com ações globais como Sair e abrir janelas de Cadastro.
 */
public class AppCentralController {

    // Adicionado para permitir seleção programática da aba
    @FXML
    private TabPane tabPanePrincipal;

    // Referência à MenuBar para obter a Stage
    @FXML
    private MenuBar menuBar; 
    
    // Referência ao FXML do Painel do Parque (fx:id="painelParque" no app-central-view.fxml)
    @FXML
    private MainController painelParqueController;
    
    // Referência ao FXML do Portal do Visitante (fx:id="portalVisitante" no app-central-view.fxml)
    @FXML
    private PortalVisitanteController portalVisitanteController;

    // Método de inicialização (chamado após a injeção dos componentes FXML)
    @FXML
    public void initialize() {
        // --- NOVO: Força a seleção do Portal do Visitante (Índice 1) ao iniciar ---
        if (tabPanePrincipal != null && tabPanePrincipal.getTabs().size() > 1) {
            tabPanePrincipal.getSelectionModel().select(1); // Seleciona a segunda aba (índice 1)
            System.out.println("Tela inicial definida como: Portal do Visitante.");
        }
        // ------------------------------------------------------------------------
        
        if (painelParqueController != null) {
            System.out.println("MainController (Painel do Parque) injetado com sucesso.");
        }
        if (portalVisitanteController != null) {
            System.out.println("PortalVisitanteController (Portal) injetado com sucesso.");
        }
    }
    
    // --- Métodos de Ação do Menu Superior ---

    /**
     * Abre a janela de cadastro de uma nova Atração.
     * Mapeado em app-central-view.fxml (Menu 'Cadastros' -> 'Nova Atração')
     */
    @FXML
    private void abrirCadastroAtracao(ActionEvent event) {
        abrirJanelaCadastro("/com/themepark/view/cadastro-atracao-view.fxml", "Cadastrar Nova Atração");
        
        // Após o cadastro (se for bem-sucedido), atualiza a lista de atrações no MainController
        if (painelParqueController != null) {
            painelParqueController.atualizarDados(); 
        }
    }

    /**
     * Abre a janela de cadastro de um novo Visitante.
     * Mapeado em app-central-view.fxml (Menu 'Cadastros' -> 'Novo Visitante')
     */
    @FXML
    private void abrirCadastroVisitante(ActionEvent event) {
        abrirJanelaCadastro("/com/themepark/view/cadastro-visitante-view.fxml", "Cadastrar Novo Visitante");
        
        // Se houver necessidade de atualizar a lista de visitantes no portal, chame:
        // if (portalVisitanteController != null) {
        //     portalVisitanteController.atualizarListaVisitantes();
        // }
    }

    /**
     * Confirma e fecha a aplicação.
     * Mapeado em app-central-view.fxml (Menu 'Arquivo' -> 'Sair')
     */
    @FXML
    private void sairAplicacao(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Saída");
        alert.setHeaderText(null);
        alert.setContentText("Tem certeza que deseja fechar a aplicação?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Obter a Stage (janela) atual a partir de qualquer componente, como a MenuBar
            if (menuBar != null && menuBar.getScene() != null) {
                 Stage stage = (Stage) menuBar.getScene().getWindow();
                 stage.close();
            } else {
                 // Fallback: Se não conseguir a Stage pela MenuBar
                 System.exit(0);
            }
        }
    }
    
    // --- Métodos Utilitários ---
    
    /**
     * Função auxiliar para carregar e exibir uma janela modal de cadastro.
     * @param fxmlPath O caminho do arquivo FXML.
     * @param title O título da janela.
     */
    private void abrirJanelaCadastro(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Bloqueia a janela principal
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            exibirAlertaErro("Erro ao carregar a tela de Cadastro: " + title + "\n" + e.getMessage());
        }
    }
    
    private void exibirAlertaErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de Aplicação");
        alert.setHeaderText("Falha ao Abrir Janela");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}