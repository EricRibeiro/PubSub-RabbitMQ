package com.ericribeiro.atendente;

import com.ericribeiro.atendente.dialogo.DialogoAtendimento;
import com.ericribeiro.atendente.queue.WorkerAtendimento;
import com.ericribeiro.helper.Dialogo;
import com.ericribeiro.model.Categoria;
import com.ericribeiro.model.Pessoa;

import java.util.List;

public class Main {

    public static void main(String[] argv) {

        try {
            Pessoa pessoa = Dialogo.getDadosPessoa();

            List<Categoria> categorias = DialogoAtendimento.exibirOpcoesAtendimento();

            WorkerAtendimento.iniciarAtendimento(categorias, pessoa);

        } catch (IllegalArgumentException e) {
            String mensagem = "Os dados inseridos são inválidos, tente novamente.";
            Dialogo.exibirMsgErro(mensagem);
            e.printStackTrace();
            System.exit(255);

        }
    }
}
