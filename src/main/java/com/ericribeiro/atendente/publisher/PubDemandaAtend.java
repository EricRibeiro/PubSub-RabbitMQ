package com.ericribeiro.atendente.publisher;

import com.ericribeiro.atendente.dialogo.DialogoAtend;
import com.ericribeiro.cliente.conexao.ConexaoCli;
import com.ericribeiro.cliente.dialogo.DialogoCli;
import com.ericribeiro.helper.Dialogo;
import com.ericribeiro.model.Categoria;
import com.ericribeiro.model.Demanda;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class PubDemandaAtend {

    private static final String EXCHANGE_NAME = "demandas";
    private static final String HOST = "localhost";

    public static void fecharDemanda(Demanda demanda) {
        Demanda demandaFechada = DialogoAtend.atlzrEstadoDemanda(demanda);

//        try {
//            ConexaoCli.enviarMensagem(demanda, categoria.toLowerCase(), EXCHANGE_NAME, HOST);
//            Dialogo.exibirMsgInfo("Demanda aberta com sucesso.");
//
//        } catch (IOException e) {
//            String mensagem = "Falha ao abrir demanda.";
//            Dialogo.exibirMsgErro(mensagem);
//            e.printStackTrace();
//            System.exit(100);
//
//        } catch (TimeoutException e) {
//            String mensagem = "Tempo limite de conexão com a fila de mensagem excedido.";
//            Dialogo.exibirMsgErro(mensagem);
//            e.printStackTrace();
//            System.exit(200);
//
//        } catch (NullPointerException e) {
//            String mensagem = "O programa será encerrado.";
//            Dialogo.exibirMsgInfo(mensagem);
//            System.exit(0);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.exit(-1);
//        }
    }

}
