package util;

import models.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import static java.lang.String.join;

public final class IoUtils {

    private IoUtils() {}

    public static void writeString(DataOutputStream out, String str) throws IOException {
       out.writeUTF(str);
    }

    public static String readString(DataInputStream in) throws IOException {
        String data = in.readUTF();
        return data;
    }

    public static Brand readBrand(DataInputStream in) throws IOException {
        Brand brand = new Brand();

        String data = in.readUTF();
        String[] brandData = data.split(";");
        brand.setId(Integer.parseInt(brandData[0]));
        brand.setManufactureId(Integer.parseInt(brandData[1]));
        brand.setPrice(Integer.parseInt(brandData[2]));
        brand.setName(brandData[3]);

        return brand;
    }

    public static Manufacture readManufacture(DataInputStream in) throws IOException {
        Manufacture manufacture = new Manufacture();

        String data = in.readUTF();
        String[] manufactureData = data.split(";");
        manufacture.setId(Integer.parseInt(manufactureData[0]));
        manufacture.setName(manufactureData[1]);

        return manufacture;
    }

    public static void writeBrand(DataOutputStream out, Brand brand) throws IOException {
        out.writeUTF(join(";", Integer.toString(brand.getId()),  Integer.toString(brand.getManufactureId()),  Integer.toString(brand.getPrice()), brand.getName()));
    }

    public static void writeManufacture(DataOutputStream out, Manufacture manufacture) throws IOException {
        out.writeUTF(join(";", Integer.toString(manufacture.getId()), manufacture.getName()));
    }

}