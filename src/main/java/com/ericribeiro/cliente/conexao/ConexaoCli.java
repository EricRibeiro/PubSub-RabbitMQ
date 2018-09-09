package com.ericribeiro.cliente.conexao;

import com.ericribeiro.helper.Serializador;
import com.ericribeiro.model.Demanda;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class ConexaoCli {

    private static Channel channel;
    private static Connection connection;

    public static void enviarMensagem(Demanda mensagem, String categoria, String exchange, String host) throws IOException, TimeoutException {
        conectar(host);

        channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);

        channel.basicPublish(exchange, categoria, null, Serializador.convertToBytes(mensagem));

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
