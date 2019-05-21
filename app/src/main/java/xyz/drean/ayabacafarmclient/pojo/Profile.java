package xyz.drean.ayabacafarmclient.pojo;

public class Profile {
    private String uid;
    private String name;
    private String address;
    private String cel;

    public Profile() {
    }

    public Profile(String uid, String name, String address, String cel) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.cel = cel;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }
}
