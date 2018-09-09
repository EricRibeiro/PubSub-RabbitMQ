package com.ericribeiro.model;

import java.io.Serializable;

import lombok.*;

@AllArgsConstructor
@ToString
public class Demanda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter
    private Boolean foiResolvida;

    @NonNull
    @Getter @Setter
    private Categoria categoria;

    @Getter @Setter
    private Pessoa atendente;

    @NonNull
    @Getter @Setter
    private Pessoa cliente;

    public String getNmFilaResposta() {
        return this.cliente.toString();
    }
}
