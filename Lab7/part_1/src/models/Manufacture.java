package models;

import java.util.ArrayList;
import java.util.List;

public class Manufacture {
    private int id;
    private String name;

    private List<Brand> brands = new ArrayList<>();

    public Manufacture(String name, Showsalon showsalon) {
        this.name = name;
        showsalon.createId(this);
    }
    public Manufacture() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    @Override
    public String toString() {
        return "Manufacture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
