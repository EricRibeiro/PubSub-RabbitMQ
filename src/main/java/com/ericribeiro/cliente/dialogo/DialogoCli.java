package com.ericribeiro.cliente.dialogo;

import com.ericribeiro.model.Categoria;

import javax.swing.*;

public abstract class DialogoCli {

    public static String exibirOpcoesAtendimento() {
        Object[] opcoes = {"NOVA DEMANDA", "RESPOSTA DE DEMANDAS ANTERIORES"};

        Object opcao = JOptionPane.showInputDialog(null,
                "Escolha uma operação:", "Opções de Atendimento",
                JOptionPane.INFORMATION_MESSAGE, null,
                opcoes, opcoes[0]);

        return opcao.toString();
    }

    public static String exibirOpcoesDemanda() {
        Object[] opcoes = {Categoria.ABERTURA, Categoria.CANCELAMENTO, Categoria.REPARO};

        Object opcao = JOptionPane.showInputDialog(null,
                "Escolha uma operação:", "Abertura de Demanda",
                JOptionPane.INFORMATION_MESSAGE, null,
                opcoes, opcoes[0]);

        return opcao.toString();
    }

}
