package xyz.drean.ayabacafarmclient.pojo;

public class Product {
    private String uid;
    private String name;
    private String urlImg;
    private double price;
    private String description;
    private String category;

    public Product() {
    }

    public Product(String uid, String name, String urlImg, double price, String description, String category) {
        this.uid = uid;
        this.name = name;
        this.urlImg = urlImg;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
