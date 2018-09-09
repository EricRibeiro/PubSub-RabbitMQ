package com.ericribeiro.helper;

import com.ericribeiro.model.Pessoa;

import javax.swing.*;

public abstract class Dialogo {

    public static void exibirMsgErro(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void exibirMsgInfo(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    public static Pessoa getDadosPessoa() {
        JTextField jTextNome = new JTextField();
        JTextField jTextCelular = new JTextField();

        Pessoa pessoa = null;

        Object[] message = {
                "Nome:", jTextNome,
                "Celular:", jTextCelular,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Inserir Dados", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String nome = jTextNome.getText();
            String celular = jTextCelular.getText();
            pessoa = new Pessoa(nome, celular);
        }

        return pessoa;
    }

}
