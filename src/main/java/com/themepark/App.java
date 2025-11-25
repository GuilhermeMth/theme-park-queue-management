package com.themepark;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.themepark.model.*;

import java.io.IOException;
import java.time.LocalTime;

/**
 * JavaFX App - Sistema de Gerenciamento de Parque Temático
 * * ATENÇÃO: Carregando agora o 'app-central-view.fxml', que é o novo container com abas.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Carrega dados de exemplo (opcional)
        carregarDadosExemplo();

        try {
            // Carrega a TELA PRINCIPAL: o novo container com as abas do Parque e Visitante.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/themepark/view/app-central-view.fxml"));
            Parent root = loader.load();

            // Configuração da janela
            Scene scene = new Scene(root, 1300, 750); // Aumentado o tamanho para o novo layout de abas
            stage.setTitle("🎡 Sistema de Gerenciamento do Parque Temático");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Erro ao carregar a view central (app-central-view.fxml). Verifique se o arquivo FXML existe e se o caminho está correto.");
            e.printStackTrace();
            throw e; // Relança a exceção para que o JavaFX a capture
        }
    }

    /**
     * Carrega alguns dados de exemplo para testar o sistema
     */
    private void carregarDadosExemplo() {
        SistemaParque sistema = SistemaParque.getInstance();

        try {
            // Criar algumas atrações de exemplo
            Atracao montanhaRussa = new Atracao(
                    20, // capacidade
                    12, // idade mínima
                    "Montanha Russa Radical",
                    TipoAtracao.MONTANHA_RUSSA,
                    NivelPrioridade.PASSE_COMUM
            );
            montanhaRussa.adicionarHorario(LocalTime.of(9, 0), LocalTime.of(9, 15));
            montanhaRussa.adicionarHorario(LocalTime.of(10, 0), LocalTime.of(10, 15));
            montanhaRussa.adicionarHorario(LocalTime.of(11, 0), LocalTime.of(11, 15));
            sistema.cadastrarAtracao(montanhaRussa);

            Atracao simulador = new Atracao(
                    30,
                    10,
                    "Simulador 4D",
                    TipoAtracao.SIMULADOR,
                    NivelPrioridade.PASSE_PREMIUM
            );
            simulador.adicionarHorario(LocalTime.of(9, 0), LocalTime.of(9, 30));
            simulador.adicionarHorario(LocalTime.of(10, 0), LocalTime.of(10, 30));
            sistema.cadastrarAtracao(simulador);

            Atracao teatro = new Atracao(
                    100,
                    0,
                    "Teatro Musical",
                    TipoAtracao.TEATRO,
                    NivelPrioridade.PASSE_COMUM
            );
            teatro.adicionarHorario(LocalTime.of(14, 0), LocalTime.of(15, 0));
            teatro.adicionarHorario(LocalTime.of(16, 0), LocalTime.of(17, 0));
            sistema.cadastrarAtracao(teatro);

            Atracao brinquedoInfantil = new Atracao(
                    15,
                    3,
                    "Carrossel Mágico",
                    TipoAtracao.BRINQUEDO_INFANTIL,
                    NivelPrioridade.PASSE_COMUM
            );
            brinquedoInfantil.adicionarHorario(LocalTime.of(9, 0), LocalTime.of(9, 10));
            brinquedoInfantil.adicionarHorario(LocalTime.of(9, 15), LocalTime.of(9, 25));
            sistema.cadastrarAtracao(brinquedoInfantil);

            Atracao passeioAquatico = new Atracao(
                    25,
                    8,
                    "Splash Adventure",
                    TipoAtracao.PASSEIO_AQUATICO,
                    NivelPrioridade.PASSE_COMUM
            );
            passeioAquatico.adicionarHorario(LocalTime.of(10, 0), LocalTime.of(10, 20));
            passeioAquatico.adicionarHorario(LocalTime.of(11, 0), LocalTime.of(11, 20));
            sistema.cadastrarAtracao(passeioAquatico);

            // Criar alguns visitantes de exemplo
            Visitante v1 = new Visitante(
                    "12345678901",
                    "15/03/1990",
                    "joao.silva@email.com",
                    "João Silva",
                    TipoIngresso.COMUM
            );
            sistema.cadastrarVisitante(v1);

            Visitante v2 = new Visitante(
                    "98765432109",
                    "20/07/1985",
                    "maria.santos@email.com",
                    "Maria Santos",
                    TipoIngresso.PREMIUM
            );
            sistema.cadastrarVisitante(v2);

            Visitante v3 = new Visitante(
                    "11122233344",
                    "10/12/1995",
                    "pedro.costa@email.com",
                    "Pedro Costa",
                    TipoIngresso.ELITE
            );
            sistema.cadastrarVisitante(v3);

            Visitante v4 = new Visitante(
                    "55566677788",
                    "05/05/2010",
                    "ana.oliveira@email.com",
                    "Ana Oliveira",
                    TipoIngresso.COMUM
            );
            sistema.cadastrarVisitante(v4);

            // Adicionar alguns visitantes nas filas para simular movimento
            sistema.adicionarVisitanteNaFila(v1, montanhaRussa);
            sistema.adicionarVisitanteNaFila(v2, montanhaRussa);
            sistema.adicionarVisitanteNaFila(v3, simulador);
            sistema.adicionarVisitanteNaFila(v4, brinquedoInfantil);
            sistema.adicionarVisitanteNaFila(v1, teatro);

            System.out.println("✅ Dados de exemplo carregados com sucesso!");

        } catch (Exception e) {
            System.err.println("⚠️ Erro ao carregar dados de exemplo: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}