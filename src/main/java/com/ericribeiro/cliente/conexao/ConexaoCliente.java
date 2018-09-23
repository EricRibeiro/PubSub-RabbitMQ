package com.ericribeiro.cliente.conexao;

import com.ericribeiro.helper.Serializador;
import com.ericribeiro.model.Demanda;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class ConexaoCli {

    private static Channel channel;
    private static Connection connection;

    public static void enviarMensagem(Demanda mensagem, String nmFila, String nmExchange, String host)
            throws IOException, TimeoutException {

        conectar(host);

        channel.exchangeDeclare(nmExchange, BuiltinExchangeType.DIRECT);

        boolean durable = true;
        channel.queueDeclare(nmFila, durable, false, false, null);

        channel.basicPublish(nmExchange, nmFila,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                Serializador.convertToBytes(mensagem));

        channel.close();
        connection.close();
    }

    private static void conectar(String host) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);

        connection = factory.newConnection();
        channel = connection.createChannel();
    }
}
