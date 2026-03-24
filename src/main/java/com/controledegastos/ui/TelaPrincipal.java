package com.controledegastos.ui;

import com.controledegastos.model.Transacao;
import com.controledegastos.model.TipoTransacao;
import com.controledegastos.service.FinanceiroService;

import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Map;
import java.util.stream.Collectors;

public class TelaPrincipal {

    private FinanceiroService financeiro = new FinanceiroService();

    public void start(Stage stage) {

        TextField descricaoField = new TextField();
        descricaoField.setPromptText("Descrição");

        TextField valorField = new TextField();
        valorField.setPromptText("Valor");

        ComboBox<TipoTransacao> tipoBox = new ComboBox<>();
        tipoBox.getItems().addAll(TipoTransacao.values());

        ComboBox<String> categoriaBox = new ComboBox<>();
        categoriaBox.getItems().addAll(
                "Alimentação", "Transporte", "Moradia",
                "Lazer", "Saúde", "Educação", "Outros"
        );

        Button adicionar = new Button("Adicionar");
        Button remover = new Button("Remover Selecionado");

        Label saldo = new Label("Saldo: R$ 0,00");

        // TABELA
        TableView<Transacao> tabela = new TableView<>();
        tabela.setItems(financeiro.listarTransacoes()); // 🔥 VINCULAÇÃO DIRETA

        TableColumn<Transacao, String> colDescricao = new TableColumn<>("Descrição");
        colDescricao.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDescricao()));

        TableColumn<Transacao, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTipo().toString()));

        TableColumn<Transacao, Number> colValor = new TableColumn<>("Valor");
        colValor.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getValor()));

        TableColumn<Transacao, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCategoria()));

        tabela.getColumns().addAll(colDescricao, colTipo, colValor, colCategoria);

        // GRÁFICO
        PieChart grafico = new PieChart();

        // ADICIONAR
        adicionar.setOnAction(e -> {

            try {
                String desc = descricaoField.getText();
                double val = Double.parseDouble(valorField.getText().replace(",", "."));
                TipoTransacao tipo = tipoBox.getValue();
                String categoria = categoriaBox.getValue();

                financeiro.adicionarTransacao(desc, val, tipo, categoria);

                atualizarSaldo(saldo);
                atualizarGrafico(grafico);

                descricaoField.clear();
                valorField.clear();

            } catch (Exception ex) {
                mostrarAlerta("Erro", "Dados inválidos");
            }

        });

        // REMOVER
        remover.setOnAction(e -> {

            Transacao selecionada = tabela.getSelectionModel().getSelectedItem();

            if (selecionada == null) {
                mostrarAlerta("Erro", "Selecione uma transação");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("Deseja remover?");

            if (confirm.showAndWait().get() == ButtonType.OK) {
                financeiro.removerTransacao(selecionada);
                atualizarSaldo(saldo);
                atualizarGrafico(grafico);
            }

        });

        VBox root = new VBox(10);

        root.getChildren().addAll(
                descricaoField,
                valorField,
                tipoBox,
                categoriaBox,
                adicionar,
                remover,
                tabela,
                saldo,
                grafico
        );

        Scene scene = new Scene(root, 500, 600);

        System.out.println("CSS encontrado? " + (getClass().getResource("/css/style.css") != null));

        try {

            scene.getStylesheets().add(
                    getClass().getResource("/css/style.css").toExternalForm()
            );

            System.out.println("CSS carregado!");

        } catch (Exception ex) {

            ex.printStackTrace();
            System.out.println("Erro ao carregar CSS");

        }

        stage.setScene(scene);
        stage.setTitle("Controle de Gastos");
        stage.show();
    }

    private void atualizarSaldo(Label saldo) {
        double s = financeiro.calcularSaldo();
        saldo.setText("Saldo: R$ " + String.format("%.2f", s));
    }

    private void atualizarGrafico(PieChart grafico) {

        grafico.getData().clear();

        Map<String, Double> dados = financeiro.listarTransacoes().stream()
                .filter(t -> t.getTipo() == TipoTransacao.DESPESA)
                .collect(Collectors.groupingBy(
                        Transacao::getCategoria,
                        Collectors.summingDouble(Transacao::getValor)
                ));

        dados.forEach((cat, val) ->
                grafico.getData().add(new PieChart.Data(cat, val))
        );
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(titulo);
    }
}