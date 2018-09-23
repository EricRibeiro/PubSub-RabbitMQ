package com.ericribeiro.atendente;

import com.ericribeiro.atendente.queue.WorkerAtendente;
import com.ericribeiro.atendente.utils.DialogoAtendente;
import com.ericribeiro.helper.Dialogo;
import com.ericribeiro.model.Categoria;
import com.ericribeiro.model.Pessoa;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Atendente {

    private static final String EXCHANGE_DEMANDAS = "demandas";
    private static final String EXCHANGE_RESPOSTAS = "respostas";
    private static final String HOST = "localhost";

    public static void main(String[] argv) {

        WorkerAtendente worker = new WorkerAtendente(EXCHANGE_DEMANDAS, EXCHANGE_RESPOSTAS, HOST);

        try {
            Pessoa atendente = DialogoAtendente.getDadosAtendente();

            List<Categoria> categorias = DialogoAtendente.exibirOpcoesAtendimento();

            worker.entrarFilaDemandas(categorias);

            Dialogo.exibirMsgInfo("<html>Clique em <b>'OK'</b> para receber demandas em segundo plano.</html>");

            worker.processarDemandas(atendente);

        } catch (IllegalArgumentException e) {
            String mensagem = "Os dados inseridos são inválidos, tente novamente.";
            Dialogo.exibirMsgErro(mensagem);
            e.printStackTrace();
            System.exit(255);

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
