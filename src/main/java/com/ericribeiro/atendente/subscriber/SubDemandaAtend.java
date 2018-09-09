package com.ericribeiro.atendente.subscriber;

import com.ericribeiro.atendente.conexao.ConexaoAtend;
import com.ericribeiro.helper.Dialogo;
import com.ericribeiro.model.Pessoa;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public abstract class SubDemandaAtend {

    private static final String EXCHANGE_NAME = "demandas";
    private static final String HOST = "localhost";

    public static void iniciarAtendimento(List categorias, Pessoa pessoa) {

        try {
            ConexaoAtend.receberMensagem(categorias, EXCHANGE_NAME, HOST);
            Dialogo.exibirMsgInfo("<html>Clique em <b>'OK'</b> para receber demandas em segundo plano.</html>");
            ConexaoAtend.processarMensagem(pessoa);

        } catch (IOException e) {
            String mensagem = "Falha ao atender demanda.";
            Dialogo.exibirMsgErro(mensagem);
            e.printStackTrace();
            System.exit(100);

        } catch (TimeoutException e) {
            String mensagem = "Tempo limite de conexão com a fila de mensagem excedido.";
            Dialogo.exibirMsgErro(mensagem);
            e.printStackTrace();
            System.exit(200);
        }

    }
}
