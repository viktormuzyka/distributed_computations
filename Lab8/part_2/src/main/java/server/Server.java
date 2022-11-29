package server;

import rmi.RmiServer;
import rmi.RmiServerImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(22222);
        RmiServer server = new RmiServerImpl();
        registry.rebind("server", server);
        System.out.println("Server started!");
    }
}