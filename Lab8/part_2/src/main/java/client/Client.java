package client;

import models.*;
import util.IoUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private Socket socket;
    private final String host;
    private final int port;
    private DataInputStream in;
    private DataOutputStream out;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void disconnect() throws IOException {
        socket.close();
    }

    public boolean insertManufacture(Manufacture manufacture) throws IOException {
        IoUtils.writeString(out, "insertManufacture");
        IoUtils.writeManufacture(out, manufacture);

        return in.readBoolean();
    }

    public boolean deleteManufacture(int id) throws IOException {
        IoUtils.writeString(out, "deleteManufacture");
        out.writeInt(id);

        return in.readBoolean();
    }

    public boolean insertBrand(Brand brand) throws IOException {
        IoUtils.writeString(out, "insertBrand");
        IoUtils.writeBrand(out, brand);

        return in.readBoolean();
    }

    public boolean deleteBrand(int id) throws IOException {
        IoUtils.writeString(out, "deleteBrand");
        out.writeInt(id);

        return in.readBoolean();
    }

    public boolean updateBrand(Brand brand) throws IOException {
        IoUtils.writeString(out, "updateBrand");
        IoUtils.writeBrand(out, brand);

        return in.readBoolean();
    }

    public boolean moveToAnotherManufacture(int brandId, int newManufactureId) throws IOException {
        IoUtils.writeString(out, "moveToAnotherManufacture");
        out.writeInt(brandId);
        out.writeInt(newManufactureId);

        return in.readBoolean();
    }

    public List<Brand> findBrandsByManufactureName(String manufactureName) throws IOException {
        IoUtils.writeString(out, "findBrandsByManufactureName");
        IoUtils.writeString(out, manufactureName);

        return readBrands();
    }

    public List<Manufacture> findAllManufactures() throws IOException {
        IoUtils.writeString(out, "findAllManufactures");

        return readManufactures();
    }

    private List<Brand> readBrands() throws IOException {
        List<Brand> result = new ArrayList<>();
        int listSize = in.readInt();

        for (int i = 0; i < listSize; i++) {
            result.add(IoUtils.readBrand(in));
        }

        return result;
    }

    private List<Manufacture> readManufactures() throws IOException {
        List<Manufacture> result = new ArrayList<>();
        int listSize = in.readInt();
        System.out.println(listSize);

        for (int i = 0; i < listSize; i++) {
            result.add(IoUtils.readManufacture(in));
        }

        return result;
    }
}