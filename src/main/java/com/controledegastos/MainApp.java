package com.controledegastos;

import com.controledegastos.ui.TelaPrincipal;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        TelaPrincipal tela = new TelaPrincipal();
        tela.start(stage);

    }

    public static void main(String[] args) {
        launch();
    }
}