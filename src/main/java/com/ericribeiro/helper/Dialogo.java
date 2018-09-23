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
}
