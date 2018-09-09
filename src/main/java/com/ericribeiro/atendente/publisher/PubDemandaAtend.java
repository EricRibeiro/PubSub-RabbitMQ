package com.ericribeiro.atendente.publisher;

import com.ericribeiro.atendente.conexao.ConexaoAtend;
import com.ericribeiro.atendente.dialogo.DialogoAtend;
import com.ericribeiro.helper.Dialogo;
import com.ericribeiro.model.Demanda;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class PubDemandaAtend {

    private static final String EXCHANGE_NAME = "resolvidas";
    private static final String HOST = "localhost";

    public static void fecharDemanda(Demanda demanda) {
        Boolean foiResolvida = DialogoAtend.atlzrEstadoDemanda(demanda);

        if (foiResolvida) {
            demanda.setFoiResolvida(true);
        } else {
            demanda.setFoiResolvida(false);
        }

        try {
            ConexaoAtend.enviarMensagem(demanda, demanda.getCliente().getNmFila(), EXCHANGE_NAME, HOST);
            Dialogo.exibirMsgInfo("Demanda fechada com sucesso.");

        } catch (IOException e) {
            String mensagem = "Falha ao fechar demanda.";
            Dialogo.exibirMsgErro(mensagem);
            e.printStackTrace();
            System.exit(100);

        } catch (TimeoutException e) {
            String mensagem = "Tempo limite de conexão com a fila de mensagem excedido.";
            Dialogo.exibirMsgErro(mensagem);
            e.printStackTrace();
            System.exit(200);

        } catch (NullPointerException e) {
            String mensagem = "O programa será encerrado.";
            Dialogo.exibirMsgInfo(mensagem);
            System.exit(0);

        } catch (Exception e) {
            String mensagem = "O programa será encerrado.";
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
