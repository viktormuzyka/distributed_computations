import models.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Application {
    private final static String path = "src/resources/input.xml";

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        Showsalon showsalon = DOMparser.parse(path);
        showsalon.outputAllContents();

        System.out.println("\nAdd a new manufacture EzVictory\n");
        Manufacture manufacture_1 = new Manufacture("EzVictory", showsalon);
        showsalon.addManufacture(manufacture_1);
        showsalon.outputAllContents();

        System.out.println("\nAdd some new brands for this manufacture\n");
        Brand first_brand = new Brand("Br11111", 202, showsalon);
        showsalon.addBrand(first_brand, Integer.toString(manufacture_1.getId()));
        Brand second_brand = new Brand("hg22222", 232, showsalon);
        showsalon.addBrand(second_brand, Integer.toString(manufacture_1.getId()));
        showsalon.outputAllContents();

        System.out.println("\nChange brand name\n");
        showsalon.renameBrand(showsalon.getBrand("Br11111"), "NewName3333");
        showsalon.outputAllContents();

        System.out.println("\nChange manufacture name\n");
        showsalon.renameManufacture(showsalon.getManufacture("EzVictory"), "SoHardByCool");
        showsalon.outputAllContents();

        System.out.println("\nGet toyota groups brands\n");
        List<Brand> brands = showsalon.getManufactureBrands("TOYOTA GROUP");
        for (Brand b : brands)
            System.out.println(b);

        System.out.println("\nGet all manufactures\n");
        Map<String, Manufacture> manufactureMap = showsalon.getManufactures();
        for (String id : manufactureMap.keySet()){
            System.out.println(manufactureMap.get(id));
        }

        System.out.println("\nDelete a SoHardByCool brand hg22222\n");
        showsalon.removeBrand(second_brand);
        brands = showsalon.getManufactureBrands("SoHardByCool");
        for (Brand b : brands)
            System.out.println(b);

        System.out.println("\nDelete SoHardByCool\n");
        showsalon.removeManufacture(manufacture_1);
        showsalon.outputAllContents();

        //DOMparser.write(showsalon, path);
    }
}