package com.controledegastos.model;

public class Transacao {

    private String descricao;
    private double valor;
    private TipoTransacao tipo;
    private String categoria;

    public Transacao(String descricao, double valor, TipoTransacao tipo, String categoria) {
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public String getCategoria() {
        return categoria;
    }
}