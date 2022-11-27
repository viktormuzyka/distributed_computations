package models;

public class Brand  {
    private int id;
    private int manufactureID;
    private String name;
    private int price;

    public Brand () {}

    public Brand(String name, int price, Showsalon showsalon) {
        this.name = name;
        this.price = price;
        showsalon.createId(this);
    }

    public Brand (int id, int manufactureID, String name, int price) {
        this.id = id;
        this.manufactureID = manufactureID;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getManufactureID() {
        return manufactureID;
    }

    public void setManufactureID(int teamId) {
        this.manufactureID = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void name(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", manufactureId=" + manufactureID +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}