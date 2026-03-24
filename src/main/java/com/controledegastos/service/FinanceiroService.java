package com.controledegastos.service;

import com.controledegastos.model.Transacao;
import com.controledegastos.model.TipoTransacao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FinanceiroService {

    private ObservableList<Transacao> transacoes = FXCollections.observableArrayList();

    public ObservableList<Transacao> listarTransacoes() {
        return transacoes;
    }

    public void adicionarTransacao(String descricao, double valor, TipoTransacao tipo, String categoria) {
        transacoes.add(new Transacao(descricao, valor, tipo, categoria));
    }

    public void removerTransacao(Transacao t) {
        transacoes.remove(t);
    }

    public double calcularSaldo() {
        return transacoes.stream()
                .mapToDouble(t -> t.getTipo() == TipoTransacao.RECEITA ? t.getValor() : -t.getValor())
                .sum();
    }
}