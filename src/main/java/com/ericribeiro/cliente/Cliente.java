package com.ericribeiro.cliente;

import com.ericribeiro.cliente.queue.ProducerCliente;
import com.ericribeiro.cliente.queue.WorkerCliente;
import com.ericribeiro.cliente.utils.DialogoCliente;
import com.ericribeiro.helper.Dialogo;
import com.ericribeiro.model.Categoria;
import com.ericribeiro.model.Demanda;
import com.ericribeiro.model.Pessoa;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Cliente {

    private static final String EXCHANGE_DEMANDAS = "demandas";
    private static final String EXCHANGE_RESPOSTAS = "respostas";
    private static final String HOST = "localhost";

    public static void main(String[] argv) {

        ProducerCliente producer = new ProducerCliente(EXCHANGE_DEMANDAS, EXCHANGE_RESPOSTAS, HOST);
        WorkerCliente worker = new WorkerCliente(EXCHANGE_DEMANDAS, EXCHANGE_RESPOSTAS, HOST);

        try {
            Pessoa cliente = DialogoCliente.getDadosCliente();

            while (true) {
                String opcao = DialogoCliente.exibirOpcoesAtendimento();

                switch (opcao) {
                    case "NOVA DEMANDA":
                        Categoria categoria = DialogoCliente.exibirOpcoesDemanda();
                        Demanda demanda = new Demanda(null, categoria, null, cliente);

                        producer.enviarDemanda(demanda);

                        Dialogo.exibirMsgInfo("Demanda aberta com sucesso.");
                        break;

                    case "RESPOSTA DE DEMANDAS ANTERIORES":
                        String nmFila = cliente.getNmFila();
                        worker.entrarFilaRespostas(nmFila);
                        worker.processarRespostas();
                        break;

                    default:
                        String mensagem = "O programa será encerrado.";
                        Dialogo.exibirMsgErro(mensagem);
                        System.exit(-1);
                        break;
                }
            }

        } catch (NullPointerException e) {
            String mensagem = "O programa será encerrado.";
            Dialogo.exibirMsgErro(mensagem);
            e.printStackTrace();
            System.exit(-1);

        } catch (IllegalArgumentException e) {
            String mensagem = "Os dados inseridos são inválidos, tente novamente.";
            Dialogo.exibirMsgErro(mensagem);
            e.printStackTrace();
            System.exit(255);
        } catch (TimeoutException e) {
            String mensagem = "Tempo limite de conexão com a fila de mensagem excedido.";
            Dialogo.exibirMsgErro(mensagem);
            e.printStackTrace();
            System.exit(200);

        } catch (IOException e) {
            String mensagem = "Falha ao abrir demanda.";
            Dialogo.exibirMsgErro(mensagem);
            e.printStackTrace();
            System.exit(100);
        }
    }
}
