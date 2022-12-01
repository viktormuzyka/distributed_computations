package client;

import models.Brand;
import models.Manufacture;
import util.IoUtils;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Client implements AutoCloseable {
    private Channel channel;
    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);

        Connection connection = factory.newConnection();
        channel = connection.createChannel();
    }

    private DataInputStream call(byte[] params)
            throws IOException, ExecutionException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", "rpc_queue", props, params);

        CompletableFuture<ByteArrayInputStream> response = new CompletableFuture<>();
        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.complete(new ByteArrayInputStream(delivery.getBody()));
            }
        }, consumerTag -> {});

        ByteArrayInputStream resultByteArray = response.get();
        channel.basicCancel(ctag);
        return new DataInputStream(resultByteArray);
    }

    public boolean insertManufacture(Manufacture toInsert) throws IOException, ExecutionException, InterruptedException {
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outBytes);
        IoUtils.writeString(out, "insertManufacture");
        IoUtils.writeManufacture(out, toInsert);
        DataInputStream resultStream = call(outBytes.toByteArray());
        return resultStream.readBoolean();
    }

    public boolean insertBrand(Brand toInsert) throws IOException, ExecutionException, InterruptedException {
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outBytes);
        IoUtils.writeString(out, "insertBrand");
        IoUtils.writeBrand(out, toInsert);
        DataInputStream resultStream = call(outBytes.toByteArray());
        return resultStream.readBoolean();
    }

    public boolean deleteBrand(int id) throws IOException, ExecutionException, InterruptedException {
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outBytes);
        IoUtils.writeString(out, "deleteBrand");
        out.writeLong(id);
        DataInputStream resultStream = call(outBytes.toByteArray());
        return resultStream.readBoolean();
    }

    public List<Brand> findBrandsByManufactureName(String manufactureName)
            throws IOException, ExecutionException, InterruptedException {
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outBytes);
        IoUtils.writeString(out, "findBrandsByManufactureName");
        IoUtils.writeString(out, manufactureName);
        DataInputStream resultStream = call(outBytes.toByteArray());
        return readBrands(resultStream);
    }

    private List<Brand> readBrands(DataInputStream in) throws IOException {
        List<Brand> result = new ArrayList<>();
        int listSize = in.readInt();

        for (int i = 0; i < listSize; i++) {
            result.add(IoUtils.readBrand(in));
        }

        return result;
    }

    @Override
    public void close() throws Exception {
        channel.close();
    }
}