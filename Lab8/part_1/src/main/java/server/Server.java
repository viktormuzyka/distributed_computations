package server;

import dao.*;
import models.Brand;
import models.Manufacture;
import util.IoUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import java.io.DataInputStream;
import java.io.IOException;

public class Server {
    private ServerSocket serverSocket;
    private final int port;
    private DataInputStream in;
    private DataOutputStream out;
    private final BrandDao brandDao;
    private final ManufactureDao manufactureDao;

    public Server(int port) {
        this.port = port;
        this.brandDao = new BrandDao();
        this.manufactureDao = new ManufactureDao();
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client accepted");

            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            while (processQuery());
        }
    }

    private boolean processQuery() {
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
                    writeListOfBrands(result);
                }

                case "findAllManufactures" -> {
                    List<Manufacture> manufactures = manufactureDao.findAll();
                    System.out.println("we find!");
                    writeListOfManufactures(manufactures);
                }

                default -> {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void writeListOfBrands(List<Brand> brands) throws IOException {
        out.writeInt(brands.size());

        for (Brand brand : brands) {
            IoUtils.writeBrand(out, brand);
        }
    }

    private void writeListOfManufactures(List<Manufacture> manufactures) throws IOException {
        out.writeInt(manufactures.size());

        for (Manufacture manufacture : manufactures) {
            IoUtils.writeManufacture(out, manufacture);
        }
    }

    public static void main(String[] args) {
        Server server = new Server(11111);
        try {
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}