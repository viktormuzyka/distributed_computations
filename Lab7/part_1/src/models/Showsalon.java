package models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Showsalon {
    private Map<String, Manufacture> manufactures = new HashMap<>();
    private Map<String, String> manufactureNames = new HashMap<>();
    private Map<String, Brand> brands = new HashMap<>();
    private Map<String, String> brandNames = new HashMap<>();

    public Showsalon() { }

    public void createId(Manufacture manufacture) {
        int id = manufactures.size();
        String idToString = "100" + id;
        while(manufactures.get(idToString) != null) {
            id++;
            idToString = "100" + id;
        }
        manufacture.setId(Integer.parseInt(idToString));
    }

    public void createId(Brand brand) {
        int id = brands.size();
        String idToString = "200" + id;
        while(manufactures.get(idToString) != null) {
            id++;
            idToString = "200" + id;
        }
        brand.setId(Integer.parseInt(idToString));
    }

    public void addManufacture(Manufacture manufacture) {
        manufactures.put(Integer.toString(manufacture.getId()), manufacture);
        manufactureNames.put(manufacture.getName(), Integer.toString(manufacture.getId()));
    }

    public void addBrand(Brand brand, String id) {
        Manufacture manufacture = manufactures.get(id);
        brand.setManufactureID(Integer.parseInt(id));
        brands.put(Integer.toString(brand.getId()), brand);
        brandNames.put(brand.getName(), Integer.toString(brand.getId()));
        manufacture.getBrands().add(brand);
    }

    public void removeManufacture(Manufacture manufacture) {
        manufactures.remove(Integer.toString(manufacture.getId()));
        manufactureNames.remove(manufacture.getName());
        for(Brand brand: manufacture.getBrands()) {
            brands.remove(Integer.toString(brand.getId()));
        }
    }

    public void removeBrand(Brand brand) {
        brands.remove(brand.getId());
        brandNames.remove(brand.getName());
        manufactures.get(Integer.toString(brand.getManufactureID())).getBrands().remove(brand);
    }

    public void changeBrandsManufacture(Brand brand, Manufacture manufacture) {
        Manufacture old = manufactures.get(brand.getId());
        if(old != null) {
            old.getBrands().remove(brand);
        }
        brand.setId(manufacture.getId());
        manufacture.getBrands().add(brand);
    }

    public void renameBrand(Brand brand, String newName) {
        brandNames.remove(brand.getName());
        brand.setName(newName);
        brandNames.put(brand.getName(), Integer.toString(brand.getId()));
    }

    public void renameManufacture(Manufacture manufacture, String newName) {
        manufactureNames.remove(manufacture.getName());
        manufacture.setName(newName);
        manufactureNames.put(manufacture.getName(), Integer.toString(manufacture.getId()));
    }

    public Brand getBrand(String name) {
        String id = brandNames.get(name);
        if(id != null) {
            return brands.get(id);
        }
        return null;
    }

    public Manufacture getManufacture(String name) {
        String id = manufactureNames.get(name);
        if(id != null) {
            return manufactures.get(id);
        }
        return null;
    }

    public List<Brand> getManufactureBrands(String name){
        return getManufacture(name).getBrands();
    }

    public Map<String, Manufacture> getManufactures() {
        return manufactures;
    }

    public void outputAllContents(){
        for (String id : manufactures.keySet()){
            Manufacture manufacture = manufactures.get(id);
            System.out.println(manufacture);
            List<Brand> brands = manufacture.getBrands();
            for (Brand brand : brands)  System.out.println(brand.toString());
        }
    }
}