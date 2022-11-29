package client;

import models.Manufacture;
import models.Brand;
import rmi.RmiServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args)
            throws MalformedURLException, NotBoundException, RemoteException, InterruptedException {
        RmiServer server = (RmiServer) Naming.lookup("//localhost:22222/server");

        System.out.println("Connected");
        boolean res;
        res = server.deleteBrand(3);
        System.out.println("Delete brand with id 3 " + res);
        res = server.deleteBrand(6);
        System.out.println("Delete brand with id 6 " + res);
        res = server.deleteManufacture(3);
        System.out.println("Delete manufacture with id 3 " + res);
        res = server.insertBrand(new Brand(11, 2, "OPRST", 12323));
        System.out.println("Insert new brand " + res);
        res = server.updateBrand(new Brand(1, 1, "OPRST V2", 1000));
        System.out.println("Update brand with id 1, set name OPRST V2 and price 1000 " + res);
        res =  server.insertManufacture(new Manufacture(4, "VOLUN AUTO"));
        System.out.println("Insert manufacture VOLUN AUTO " + res);
        res = server.moveToAnotherManufacture(1, 4);
        System.out.println("Move brand with id 1 to manufacture VOLUN AUTO " + res);
        System.out.println(server.findBrandsByManufactureName("TOYOTA GROUP"));
        System.out.println("Find brands for TOYOTA GROUP");

        System.out.println(server.findAllManufactures());
        System.out.println("Find all manufactures");
    }
}