package com.ericribeiro.model;

import java.io.Serializable;

public class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;

    private String identificacao;

    @java.beans.ConstructorProperties({"nome", "identificacao"})
    public Pessoa(String nome, String identificacao) {
        this.nome = nome;
        this.identificacao = identificacao;
    }

    public String getNmFila() {
        String toString = "";
        String[] nomes = nome.split(" ");

        for(String n : nomes) {
            toString += n.toLowerCase() + "_";
        }

        toString += this.identificacao;

        return toString;
    }

    public String toString() {
        return "Pessoa(nome=" + this.nome + ", identificacao=" + this.identificacao + ")";
    }

    public String getNome() {
        return this.nome;
    }

    public String getIdentificacao() {
        return this.identificacao;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }
}
