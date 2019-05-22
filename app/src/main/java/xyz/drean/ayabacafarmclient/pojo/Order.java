package xyz.drean.ayabacafarmclient.pojo;

public class Order {
    private String uid;
    private String uidClient;
    private String address;
    private String nameProduct;
    private String urlImg;
    private String nameClient;
    private String cel;
    private String quantity;
    private double priceUnit;
    private double igv;
    private String date;
    private double total;

    public Order() {
    }

    public Order(String uid, String uidClient, String address, String nameProduct, String urlImg, String nameClient, String cel, String quantity, double priceUnit, double igv, String date, double total) {
        this.uid = uid;
        this.uidClient = uidClient;
        this.address = address;
        this.nameProduct = nameProduct;
        this.urlImg = urlImg;
        this.nameClient = nameClient;
        this.cel = cel;
        this.quantity = quantity;
        this.priceUnit = priceUnit;
        this.igv = igv;
        this.date = date;
        this.total = total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUidClient() {
        return uidClient;
    }

    public void setUidClient(String uidClient) {
        this.uidClient = uidClient;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public double getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(double priceUnit) {
        this.priceUnit = priceUnit;
    }

    public double getIgv() {
        return igv;
    }

    public void setIgv(double igv) {
        this.igv = igv;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
