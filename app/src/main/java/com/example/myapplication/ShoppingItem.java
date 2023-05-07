package com.example.myapplication;

public class ShoppingItem {
    private String name;
    private String info;
    private String price;
    private int imageResource;
    private int buyCount;
    private String id;

    public ShoppingItem() {}

    public ShoppingItem(String name, String info, String price, int imageResource, int buyCount) {
        this.name = name;
        this.info = info;
        this.price = price;
        this.imageResource = imageResource;
        this.buyCount = buyCount;
    }

    public String getName() {
        return name;
    }
    public String getInfo() {
        return info;
    }
    public String getPrice() {
        return price;
    }
    public int getImageResource() {
        return imageResource;
    }
    public int getBuyCount(){return buyCount;}

    public String _getId(){return id;}
    public void setId(String id){this.id = id;}
}
