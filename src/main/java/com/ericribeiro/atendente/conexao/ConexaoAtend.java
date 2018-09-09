package com.ericribeiro.atendente.conexao;

import com.ericribeiro.atendente.publisher.PubDemandaAtend;
import com.ericribeiro.cliente.publisher.PubDemandaCli;
import com.ericribeiro.helper.Dialogo;
import com.ericribeiro.helper.Serializador;
import com.ericribeiro.model.Demanda;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public abstract class ConexaoAtend {

    private static Channel channel;
    private static Connection connection;
    private static String queueName;

    private static void conectar(String host) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);

        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public static void enviarMensagem(Demanda mensagem, String categoria, String exchange, String host) throws IOException, TimeoutException {
        conectar(host);

        channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);

        channel.basicPublish(exchange, categoria, null, Serializador.convertToBytes(mensagem));

        channel.close();
        connection.close();
    }

    public static void processarMensagem() throws IOException {
        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                try {
                    Demanda demanda = (Demanda) Serializador.convertFromBytes(body);

                    System.out.println(" [x] Mensagem do tipo: '" + envelope.getRoutingKey() + "'");
                    System.out.println(" [x] " + demanda.toString());
                    System.out.println();

                    PubDemandaAtend.fecharDemanda(demanda);

                    Dialogo.exibirMsgInfo("Aguardando demandas.");

                } catch (ClassNotFoundException e) {
                    String mensagem = "Não foi possível processar a demanda.";
                    Dialogo.exibirMsgErro(mensagem);
                    e.printStackTrace();
                    System.exit(400);
                }

            }
        };

        channel.basicConsume(queueName, true, consumer);
    }

    public static void receberMensagem(List categorias, String exchange, String host) throws IOException, TimeoutException {
        conectar(host);

        channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);
        queueName = channel.queueDeclare().getQueue();

        for (Object categoria : categorias) {
            channel.queueBind(queueName, exchange, categoria.toString().toLowerCase());
        }

        System.out.println(" [*] Esperando mensagens.");
    }
}
