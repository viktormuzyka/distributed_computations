package server;

import dao.BrandDao;
import dao.ManufactureDao;
import models.Brand;
import models.Manufacture;
import util.IoUtils;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;
public class Server {
    private final int port;

    private Channel channel;
    private final BrandDao brandDao;
    private final ManufactureDao manufactureDao;

    public Server(int port) {
        this.port = port;
        this.brandDao = new BrandDao();
        this.manufactureDao = new ManufactureDao();
    }

    public void start() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(port);

        Connection connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare("rpc_queue", false, false, false, null);
        channel.queuePurge("rpc_queue");
        channel.basicQos(1);

        System.out.println("Awaiting requests...");

        processQuery();
    }

    private void processQuery() throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();

            DataInputStream in = new DataInputStream(new ByteArrayInputStream(delivery.getBody()));
            ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(outBytes);

            try {
                String query = IoUtils.readString(in);
                System.out.println(query);
                switch (query) {
                    case "insertManufacture" -> {
                        Manufacture manufacture = IoUtils.readManufacture(in);
                        boolean result = manufactureDao.insert(manufacture);
                        out.writeBoolean(result);
                    }

                    case "deleteManufacture" -> {
                        int id = in.readInt();
                        boolean result = manufactureDao.deleteById(id);
                        out.writeBoolean(result);
                    }

                    case "insertBrand" -> {
                        Brand brand = IoUtils.readBrand(in);

                        boolean result = brandDao.insert(brand);
                        out.writeBoolean(result);
                    }

                    case "deleteBrand" -> {
                        int id = in.readInt();
                        boolean result = brandDao.deleteById(id);
                        out.writeBoolean(result);
                    }

                    case "updateBrand" -> {
                        Brand brand = IoUtils.readBrand(in);
                        boolean result = brandDao.update(brand);
                        out.writeBoolean(result);
                    }

                    case "moveToAnotherManufacture" -> {
                        int playerId = in.readInt();
                        int newTeamId = in.readInt();
                        boolean result = brandDao.moveToAnotherManufacture(playerId, newTeamId);
                        out.writeBoolean(result);
                    }

                    case "findBrandsByManufactureName" -> {
                        String manufactureName = IoUtils.readString(in);
                        List<Brand> result = brandDao.findByManufactureName(manufactureName);
                        writeListOfBrands(out, result);
                    }

                    case "findAllManufactures" -> {
                        List<Manufacture> manufactures = manufactureDao.findAll();
                        writeListOfManufactures(out, manufactures);
                    }
                }
            } catch (RuntimeException e) {
                System.out.println("[.]" + e);
            } finally {
                channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, outBytes.toByteArray());
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        channel.basicConsume("rpc_queue", false, deliverCallback, consumerTag -> {});
    }

    private void writeListOfBrands(DataOutputStream out, List<Brand> brands) throws IOException {
        out.writeInt(brands.size());

        for (Brand brand : brands) {
            IoUtils.writeBrand(out, brand);
        }
    }

    private void writeListOfManufactures(DataOutputStream out, List<Manufacture> manufactures) throws IOException {
        out.writeInt(manufactures.size());

        for (Manufacture manufacture : manufactures) {
            IoUtils.writeManufacture(out, manufacture);
        }
    }

    public static void main(String[] args) {
        try {
            Server server = new Server(5672);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}