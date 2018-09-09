package com.ericribeiro.cliente;

import com.ericribeiro.cliente.dialogo.DialogoCli;
import com.ericribeiro.cliente.publisher.PubDemandaCli;
import com.ericribeiro.helper.Dialogo;
import com.ericribeiro.model.Pessoa;

public class Main {

    public static void main(String[] argv) {
        try {
            Pessoa pessoa = Dialogo.getDadosPessoa();
            String opcao = DialogoCli.exibirOpcoesAtendimento();

            switch (opcao) {
                case "NOVA DEMANDA":
                    PubDemandaCli.abrirDemanda(pessoa);
                    break;

                case "RESPOSTA DE DEMANDAS ANTERIORES":
                    System.out.println("Working on it.");
                    break;

                default:
                    System.exit(-1);
                    break;
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
        }
    }
}
