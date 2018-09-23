package com.ericribeiro.atendente.queue;

import com.ericribeiro.atendente.utils.DialogoAtendente;
import com.ericribeiro.atendente.utils.LogAtendente;
import com.ericribeiro.helper.Dialogo;
import com.ericribeiro.helper.Serializador;
import com.ericribeiro.model.Demanda;
import com.ericribeiro.model.Pessoa;
import com.rabbitmq.client.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class WorkerAtendente {

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

    @Getter @Setter
    private List nmFilas;

    public WorkerAtendente(String nmExchangeDemandas, String nmExchangeRespostas, String nmHost) {
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

    public void entrarFilaDemandas(List categorias) throws IOException, TimeoutException {
        conectar();

        nmFilas = categorias;

        channel.exchangeDeclare(this.nmExchangeDemandas, BuiltinExchangeType.DIRECT);

        boolean durable = true;

        for (Object fila : nmFilas) {
            String nmFila = fila.toString().toLowerCase();

            channel.queueDeclare(nmFila, durable,
                    false, false, null);

            channel.queueBind(nmFila, this.nmExchangeDemandas, nmFila);
        }

        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        System.out.println(" [*] Esperando mensagens.");
    }

    public void enviarResposta(Demanda mensagem, String nmFilaRespostas) throws IOException {
        channel.exchangeDeclare(this.nmExchangeRespostas, BuiltinExchangeType.DIRECT);

        boolean durable = true;
        channel.queueDeclare(nmFilaRespostas, durable, false, false, null);
        channel.queueBind(nmFilaRespostas, this.nmExchangeRespostas, nmFilaRespostas);

        channel.basicPublish(this.nmExchangeRespostas, nmFilaRespostas,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                Serializador.convertToBytes(mensagem));

        LogAtendente.imprimirDadosEnvio(mensagem.toString(), this.nmExchangeRespostas, nmFilaRespostas);
    }

    public void processarDemandas(final Pessoa pessoa) throws IOException {
        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                try {
                    Demanda demanda = (Demanda) Serializador.convertFromBytes(body);
                    demanda.setAtendente(pessoa);

                    LogAtendente.imprimirDadosRecepcao(envelope.getRoutingKey(), demanda.toString());

                    Boolean foiResolvida = DialogoAtendente.atlzrEstadoDemanda(demanda);

                    demanda.setFoiResolvida(foiResolvida);

                    enviarResposta(demanda, demanda.getCliente().getNmFila());

                } catch (ClassNotFoundException e) {
                    String mensagem = "Não foi possível processar a demanda.";
                    Dialogo.exibirMsgErro(mensagem);
                    e.printStackTrace();
                    System.exit(400);

                } finally {
                    channel.basicAck(envelope.getDeliveryTag(), false);

                    Dialogo.exibirMsgInfo("<html>Demanda fechada com sucesso! <br> Clique em <b>'OK'</b> para " +
                            "continuar" +
                            " recebendo demandas em segundo " +
                            "plano.</html>");
                }
            }
        };

        boolean noAck = false;

        for (Object fila : nmFilas) {
            String nmFila = fila.toString().toLowerCase();
            channel.basicConsume(nmFila, noAck, consumer);
        }
    }


}