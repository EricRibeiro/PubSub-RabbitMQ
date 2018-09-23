package com.ericribeiro.cliente.utils;

import com.ericribeiro.model.Categoria;
import com.ericribeiro.model.Pessoa;

import javax.swing.*;

public abstract class DialogoCliente {

    public static String exibirOpcoesAtendimento() throws NullPointerException {
        Object[] opcoes = {"NOVA DEMANDA", "RESPOSTA DE DEMANDAS ANTERIORES"};

        Object opcao = JOptionPane.showInputDialog(null,
                "Escolha uma operação:", "Opções de Atendimento",
                JOptionPane.INFORMATION_MESSAGE, null,
                opcoes, opcoes[0]);

        return opcao.toString();
    }

    public static Categoria exibirOpcoesDemanda() {
        Object[] opcoes = {Categoria.ABERTURA, Categoria.CANCELAMENTO, Categoria.REPARO};

        Object opcao = JOptionPane.showInputDialog(null,
                "Escolha uma operação:", "Abertura de Demanda",
                JOptionPane.INFORMATION_MESSAGE, null,
                opcoes, opcoes[0]);

        return (Categoria) opcao;
    }

    public static Pessoa getDadosCliente() throws IllegalArgumentException {
        JTextField jTextNome = new JTextField();
        JTextField jTextCpf = new JTextField();

        Pessoa pessoa;

        Object[] message = {
                "Nome:", jTextNome,
                "CPF:", jTextCpf,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Dados do Cliente", JOptionPane.OK_CANCEL_OPTION);

        String nome = jTextNome.getText();
        String cpf = jTextCpf.getText();

        if (option == JOptionPane.OK_OPTION && !nome.isEmpty() && !cpf.isEmpty()) {
            pessoa = new Pessoa(nome, cpf);

        } else {
            throw new IllegalArgumentException();
        }

        return pessoa;
    }


}
