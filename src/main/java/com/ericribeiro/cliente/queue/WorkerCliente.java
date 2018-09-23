package com.ericribeiro.cliente.queue;

import com.ericribeiro.cliente.utils.LogCliente;
import com.ericribeiro.helper.Dialogo;
import com.ericribeiro.helper.Serializador;
import com.ericribeiro.model.Demanda;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class WorkerCliente {

    private Channel channel;

    private Connection connection;

    private String nmExchangeDemandas;

    private String nmExchangeRespostas;

    private String nmFila;

    private String nmHost;

    public WorkerCliente(String nmExchangeDemandas, String nmExchangeRespostas, String nmHost) {
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

    public void entrarFilaRespostas(String nmFila) throws IOException, TimeoutException {
        conectar();

        this.nmFila = nmFila;

        channel.exchangeDeclare(this.nmExchangeRespostas, BuiltinExchangeType.DIRECT);

        boolean durable = true;
        channel.queueDeclare(nmFila, durable,
                false, false, null);

        channel.queueBind(nmFila, this.nmExchangeRespostas, nmFila);

        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        System.out.println(" [*] Esperando mensagens.");
    }


    public void processarRespostas() throws IOException {
        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                try {
                    Demanda demanda = (Demanda) Serializador.convertFromBytes(body);

                    LogCliente.imprimirDadosRecepcao(envelope.getRoutingKey(), demanda.toString());

                    String categoria = demanda.getCategoria().toString().toLowerCase();
                    categoria = categoria.substring(0, 1).toUpperCase() + categoria.toLowerCase().substring(1);

                    String estado = (demanda.getFoiResolvida()) ? "Resolvida" : "Não Resolvida";

                    Dialogo.exibirMsgInfo(
                            "<html>" +
                                    "<strong>Status da Demanda</strong><br><br>" +
                                    "<i>Cliente: </i>" + demanda.getCliente().getNome() + "<br>" +
                                    "<i>Categoria: </i>" + categoria + "<br>" +
                                    "<i>Atendente: </i>" + demanda.getAtendente().getNome() + "<br>" +
                                    "<i>Estado: </i>" + estado + "<br>" +
                                    "</html>");

                } catch (ClassNotFoundException e) {
                    String mensagem = "Não foi possível processar a resposta.";
                    Dialogo.exibirMsgErro(mensagem);
                    e.printStackTrace();
                    System.exit(400);

                } finally {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        boolean noAck = false;
        channel.basicConsume(nmFila, noAck, consumer);
    }
}
