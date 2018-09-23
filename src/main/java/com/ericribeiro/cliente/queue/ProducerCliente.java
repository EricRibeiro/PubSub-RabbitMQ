package com.ericribeiro.cliente.queue;

import com.ericribeiro.cliente.utils.LogCliente;
import com.ericribeiro.helper.Serializador;
import com.ericribeiro.model.Categoria;
import com.ericribeiro.model.Demanda;
import com.rabbitmq.client.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerCliente {

    @Getter @Setter
    private Channel channel;

    @Getter @Setter
    private Connection connection;

    @Getter @Setter
    private String nmExchangeDemandas;

    @Getter @Setter
    private String nmExchangeRespostas;

    @Getter @Setter
    private String nmHost;

    public ProducerCliente(String nmExchangeDemandas, String nmExchangeRespostas, String nmHost) {
        this.nmExchangeDemandas = nmExchangeDemandas;
        this.nmExchangeRespostas = nmExchangeRespostas;
        this.nmHost = nmHost;
    }

    private void conectar() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.nmHost);

        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void enviarDemanda(Demanda mensagem) throws IOException, TimeoutException {
        conectar();

        channel.exchangeDeclare(this.nmExchangeDemandas, BuiltinExchangeType.DIRECT);

        boolean durable = true;
        String nmFilaDemandas = mensagem.getCategoria().toString().toLowerCase();

        channel.queueDeclare(nmFilaDemandas, durable, false, false, null);
        channel.queueBind(nmFilaDemandas, this.nmExchangeDemandas, nmFilaDemandas);

        channel.basicPublish(this.nmExchangeDemandas, nmFilaDemandas,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                Serializador.convertToBytes(mensagem));

        channel.close();
        connection.close();

        LogCliente.imprimirDadosEnvio(mensagem.toString(), this.nmExchangeDemandas, nmFilaDemandas);
    }
}
