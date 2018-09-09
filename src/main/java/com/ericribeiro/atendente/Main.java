package com.ericribeiro.atendente;

import com.ericribeiro.atendente.dialogo.DialogoAtend;
import com.ericribeiro.atendente.subscriber.SubDemandaAtend;
import com.ericribeiro.model.Categoria;

import java.util.List;

public class Main {

    private static final String EXCHANGE_NAME = "demandas";
    private static final String HOST = "localhost";

    public static void main(String[] argv) {
        List<Categoria> categorias = DialogoAtend.exibirOpcoesAtendimento();

        SubDemandaAtend.iniciarAtendimento(categorias);

    }
}
