package com.example.myapplication;

public class CartItem {
    private String name;
    private String price;
    private int imageResource;
    private String user;
    private String id;

    public CartItem(){}

    public CartItem(String name, String price, int imageResource, String user) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.user = user;
    }

    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }
    public int getImageResource() {
        return imageResource;
    }
    public String getUser() {return user;}

    public String _getId(){return id;}
    public void setId(String id){this.id = id;}
}
