import dao.BrandDao;
import dao.ManufactureDao;
import models.Brand;
import models.Manufacture;

public class Main {
    public static void main(String[] args) {
        ManufactureDao manufactureDao = new ManufactureDao();
        BrandDao brandDao = new BrandDao();

        brandDao.deleteById(3);
        brandDao.deleteById(5);

        manufactureDao.deleteById(3);

        brandDao.insert(new Brand(11, 2, "BMP", 234));

        brandDao.update(new Brand(1, 2, "TEST1", 2312));

        manufactureDao.insert(new Manufacture(4, "MY MANUFACTURE"));

        manufactureDao.update(new Manufacture(4, "NOT MY MANUFACTURE"));

        brandDao.deleteById(3);
    }
}