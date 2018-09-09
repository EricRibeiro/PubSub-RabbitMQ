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
    private String celular;
}
