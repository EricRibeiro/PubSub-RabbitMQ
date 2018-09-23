package com.ericribeiro.model;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@ToString
public class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    @NonNull
    @Getter @Setter
    private String nome;

    @NonNull
    @Getter @Setter
    private String identificacao;

    public String getNmFila() {
        String toString = "";
        String[] nomes = nome.split(" ");

        for(String n : nomes) {
            toString += n.toLowerCase() + "_";
        }

        toString += this.identificacao;

        return toString;
    }
}
