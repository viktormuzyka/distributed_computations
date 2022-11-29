import client.Client;

import models.Brand;
import models.Manufacture;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 11111);
        try {
            client.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Connected");
        boolean res;
        res = client.deleteBrand(3);
        System.out.println("Delete brand with id 3 " + res);
        res = client.deleteBrand(6);
        System.out.println("Delete brand with id 6 " + res);
        res = client.deleteManufacture(3);
        System.out.println("Delete manufacture with id 3 " + res);
        res = client.insertBrand(new Brand(11, 2, "OPRST", 12323));
        System.out.println("Insert new brand " + res);
        res = client.updateBrand(new Brand(1, 1, "OPRST V2", 1000));
        System.out.println("Update brand with id 1, set name OPRST V2 and price 1000 " + res);
        res =  client.insertManufacture(new Manufacture(4, "VOLUN AUTO"));
        System.out.println("Insert manufacture VOLUN AUTO " + res);
        res = client.moveToAnotherManufacture(1, 4);
        System.out.println("Move brand with id 1 to manufacture VOLUN AUTO " + res);
        System.out.println(client.findBrandsByManufactureName("TOYOTA GROUP"));
        System.out.println("Find brands for TOYOTA GROUP");
        System.out.println(client.findAllManufactures());
        System.out.println("Find all manufactures");
        client.disconnect();

        System.out.println("Disconnected");
    }
}