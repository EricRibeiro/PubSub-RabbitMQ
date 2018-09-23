package com.ericribeiro.atendente.utils;

public class LogAtendente {

    public static void imprimirDadosEnvio(String mensagem, String nmExchangeRespostas, String nmFilaRespostas) {
        System.out.println(" [x] Resposta enviada");
        System.out.println(" [x] Exchange: " + "'" + nmExchangeRespostas + "'");
        System.out.println(" [x] Fila: '" + nmFilaRespostas + "'");
        System.out.println(" [x] Detalhes: " + mensagem);
        System.out.println();
    }

    public static void imprimirDadosRecepcao(String tipoMensagem, String demanda) {
        System.out.println(" [x] Demanda recebida.");
        System.out.println(" [x] Tipo: '" + tipoMensagem + "'");
        System.out.println(" [x] Detalhes: " + demanda);
        System.out.println();
    }

}
