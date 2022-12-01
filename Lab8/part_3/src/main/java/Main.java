import client.Client;
import models.Brand;
import models.Manufacture;

public class Main {

    public static void main(String[] args) {
        try (Client client = new Client("localhost", 5672)) {
            client.connect();
            System.out.println(client.insertManufacture(new Manufacture(101, "RENAULT AUTO")));
            System.out.println(client.insertBrand(new Brand(404, 101, "RENAULT", 123)));
            System.out.println(client.deleteBrand(4));
            System.out.println(client.findBrandsByManufactureName("TOYOTA GROUP"));
            System.out.println("DONE!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}