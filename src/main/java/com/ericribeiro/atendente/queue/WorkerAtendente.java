package com.ericribeiro.atendente.conexao;

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

public class ConexaoAtendente {

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

    public ConexaoAtendente(String nmExchangeDemandas, String nmExchangeRespostas, String nmHost) {
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

    public void enviarResposta(Demanda mensagem, String nmFilaRespostas)
            throws IOException {

        channel.basicPublish(this.nmExchangeRespostas, nmFilaRespostas, null,
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

                    if (foiResolvida) {
                        demanda.setFoiResolvida(true);
                    } else {
                        demanda.setFoiResolvida(false);
                    }

                    enviarResposta(demanda, demanda.getCliente().getNmFila());

                } catch (ClassNotFoundException e) {
                    String mensagem = "Não foi possível processar a demanda.";
                    Dialogo.exibirMsgErro(mensagem);
                    e.printStackTrace();
                    System.exit(400);

                } finally {
                    Dialogo.exibirMsgInfo("<html>Demanda fechada com sucesso! <br> Clique em <b>'OK'</b> para " +
                            "continuar" +
                            " recebendo demandas em segundo " +
                            "plano.</html>");

                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        boolean noAck = false;

        for (Object fila : nmFilas) {
            String nmFila = fila.toString().toLowerCase();
            channel.basicConsume(nmFila, noAck, consumer);
        }
    }

    public void entrarFilaDemandas(List categorias)
            throws IOException, TimeoutException {

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
}
