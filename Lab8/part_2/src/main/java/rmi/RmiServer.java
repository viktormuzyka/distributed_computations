package rmi;

import models.Brand;
import models.Manufacture;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RmiServer extends Remote {

    boolean insertManufacture(Manufacture manufacture) throws RemoteException;

    boolean deleteManufacture(int id) throws RemoteException;

    boolean insertBrand(Brand brand) throws RemoteException;

    boolean deleteBrand(int id) throws RemoteException;

    boolean updateBrand(Brand brand) throws RemoteException;

    boolean moveToAnotherManufacture(int manufactureId, int newManufactureId) throws RemoteException;

    List<Brand> findBrandsByManufactureName(String manufactureName) throws RemoteException;

    List<Manufacture> findAllManufactures() throws RemoteException;
}