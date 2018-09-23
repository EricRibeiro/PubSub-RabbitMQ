package com.ericribeiro.atendente.utils;

import com.ericribeiro.model.Categoria;
import com.ericribeiro.model.Demanda;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class DialogoAtendente {

    public static List<Categoria> exibirOpcoesAtendimento() throws IllegalArgumentException {
        JCheckBox abertura;
        JCheckBox cancelamento;
        JCheckBox reparo;

        List<Categoria> categorias = new ArrayList<>();

        abertura = new JCheckBox("Abertura");
        abertura.setMnemonic(KeyEvent.VK_C);

        cancelamento = new JCheckBox("Cancelamento");
        cancelamento.setMnemonic(KeyEvent.VK_G);

        reparo = new JCheckBox("Reparo");
        reparo.setMnemonic(KeyEvent.VK_H);

        Object[] message = {
                "Selecione as categorias de atendimento: ",
                "\n",
                abertura,
                cancelamento,
                reparo
        };

        JOptionPane.showMessageDialog(null, message, "Abertura de Atendimento",
                JOptionPane.INFORMATION_MESSAGE);

        if(abertura.isSelected()) {
            categorias.add(Categoria.ABERTURA);
        }

        if(cancelamento.isSelected()) {
            categorias.add(Categoria.CANCELAMENTO);
        }

        if(reparo.isSelected()) {
            categorias.add(Categoria.REPARO);
        }

        if(!abertura.isSelected() && !cancelamento.isSelected() && !reparo.isSelected()) {
            throw new IllegalArgumentException();
        }

        return categorias;
    }

    public static Boolean atlzrEstadoDemanda(Demanda demanda) {
        String mensagem = "<html> A demanda abaixo foi resolvida? " + "<br/>" +
                "Cliente: " + demanda.getCliente().getNome() + "<br/>" +
                "Categoria: " + demanda.getCategoria() +
                "</html>";

        Integer opcao = JOptionPane.showConfirmDialog(null, mensagem, "Atendimento",
                JOptionPane.YES_NO_OPTION);

        return (opcao == JOptionPane.YES_OPTION);
    }

}
