package com.ericribeiro.atendente.conexao;

import com.ericribeiro.atendente.publisher.PubDemandaAtend;
import com.ericribeiro.helper.Dialogo;
import com.ericribeiro.helper.Serializador;
import com.ericribeiro.model.Demanda;
import com.ericribeiro.model.Pessoa;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public abstract class ConexaoAtend {

    private static Channel channel;
    private static Connection connection;
    private static String queueName;

    private synchronized static void conectar(String host) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);

        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public synchronized static void enviarMensagem(Demanda mensagem, String filaDeResposta, String exchange, String host) throws IOException, TimeoutException {
        conectar(host);

        channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);

        channel.basicPublish(exchange, filaDeResposta, null, Serializador.convertToBytes(mensagem));

        System.out.println(" [x] Resposta da demanda: " + mensagem.toString());
        System.out.println(" [x] Exchange: " + "'" + exchange + "'");
        System.out.println(" [x] Fila de resposta: " + filaDeResposta);
        System.out.println();

        channel.close();
        connection.close();
    }

    public synchronized static void processarMensagem(final Pessoa pessoa) throws IOException {
        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                try {
                    Demanda demanda = (Demanda) Serializador.convertFromBytes(body);

                    System.out.println(" [x] Mensagem do tipo: '" + envelope.getRoutingKey() + "'");
                    System.out.println(" [x] " + demanda.toString());
                    System.out.println();

                    demanda.setAtendente(pessoa);

                    PubDemandaAtend.fecharDemanda(demanda);

                    Dialogo.exibirMsgInfo("<html>Clique em <b>'OK'</b> para continuar recebendo demandas em segundo " +
                            "plano.</html>");

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

    public synchronized static void receberMensagem(List categorias, String exchange, String host) throws IOException,
            TimeoutException {
        conectar(host);

        channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);
        queueName = channel.queueDeclare().getQueue();

        for (Object categoria : categorias) {
            channel.queueBind(queueName, exchange, categoria.toString().toLowerCase());
        }

        System.out.println(" [*] Esperando mensagens.");
    }
}
