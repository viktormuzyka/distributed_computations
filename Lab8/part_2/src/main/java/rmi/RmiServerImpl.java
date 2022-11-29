package rmi;


import dao.BrandDao;
import dao.ManufactureDao;
import models.Brand;
import models.Manufacture;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RmiServerImpl extends UnicastRemoteObject implements RmiServer {
    private final BrandDao brandDao;
    private final ManufactureDao manufactureDao;
    private final transient ReadWriteLock lock;

    public RmiServerImpl() throws RemoteException {
        this.brandDao = new BrandDao();
        this.manufactureDao = new ManufactureDao();
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public boolean insertManufacture(Manufacture manufacture) throws RemoteException {
        try {
            lock.writeLock().lock();
            return manufactureDao.insert(manufacture);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean deleteManufacture(int id) throws RemoteException {
        try {
            lock.writeLock().lock();
            return manufactureDao.deleteById(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean insertBrand(Brand brand) throws RemoteException {
        try {
            lock.writeLock().lock();
            return brandDao.insert(brand);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean deleteBrand(int id) throws RemoteException {
        try {
            lock.writeLock().lock();
            return brandDao.deleteById(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean updateBrand(Brand brand) throws RemoteException {
        try {
            lock.writeLock().lock();
            return brandDao.update(brand);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean moveToAnotherManufacture(int manufactureId, int newManufactureId) throws RemoteException {
        try {
            lock.writeLock().lock();
            return brandDao.moveToAnotherManufacture(manufactureId, newManufactureId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<Brand> findBrandsByManufactureName(String manufactureName) throws RemoteException {
        try {
            lock.readLock().lock();
            return brandDao.findByManufactureName(manufactureName);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Manufacture> findAllManufactures() throws RemoteException {
        try {
            lock.readLock().lock();
            return manufactureDao.findAll();
        } finally {
            lock.readLock().unlock();
        }
    }
}