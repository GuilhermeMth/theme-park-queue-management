module com.themepark.ThemeParkQueueManager {
    // Requer os módulos ESSENCIAIS do JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics; // Necessário para Stage, Scene, etc.

    // 1. Exporta o pacote principal (onde está a classe App.java) para que o JavaFX possa iniciá-lo
    exports com.themepark;

    // 2. Exporta o pacote do controller para que outras partes do seu projeto (e o FXML) possam usá-lo
    exports com.themepark.controller;

    // 3. ABRE o pacote do controller para o javafx.fxml. ISSO É CRUCIAL!
    // O FXMLLoader usa reflexão (reflection) para instanciar a classe MainController
    // e injetar os elementos @FXML. Sem o 'opens', o erro de classe não encontrada pode ocorrer.
    opens com.themepark.controller to javafx.fxml;

    // 4. Exporta o pacote de modelo (se você usa dados de modelo em TableViews, por exemplo)
    exports com.themepark.model;
    opens com.themepark.model to javafx.base; // Abre para o javafx.base para TableView PropertyValueFactory
}