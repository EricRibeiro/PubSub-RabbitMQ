package com.ericribeiro.model;

import java.io.Serializable;

public class Demanda implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean foiResolvida;

    private Categoria categoria;

    private Pessoa atendente;

    private Pessoa cliente;

    @java.beans.ConstructorProperties({"foiResolvida", "categoria", "atendente", "cliente"})
    public Demanda(Boolean foiResolvida, Categoria categoria, Pessoa atendente, Pessoa cliente) {
        this.foiResolvida = foiResolvida;
        this.categoria = categoria;
        this.atendente = atendente;
        this.cliente = cliente;
    }

    public String toString() {
        return "Demanda(foiResolvida=" + this.foiResolvida + ", categoria=" + this.categoria + ", atendente=" + this.atendente + ", cliente=" + this.cliente + ")";
    }

    public Boolean getFoiResolvida() {
        return this.foiResolvida;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public Pessoa getAtendente() {
        return this.atendente;
    }

    public Pessoa getCliente() {
        return this.cliente;
    }

    public void setFoiResolvida(Boolean foiResolvida) {
        this.foiResolvida = foiResolvida;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setAtendente(Pessoa atendente) {
        this.atendente = atendente;
    }

    public void setCliente(Pessoa cliente) {
        this.cliente = cliente;
    }
}
