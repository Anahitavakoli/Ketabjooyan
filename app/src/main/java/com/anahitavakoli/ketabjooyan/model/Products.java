package com.anahitavakoli.ketabjooyan.model;

public class Products {

    private String pname, price, pid, category, pdescription, date, time , image;

    public Products() {
    }

    public Products(String pname, String price, String pid, String category, String pdescription, String date, String time, String image) {
        this.pname = pname;
        this.price = price;
        this.pid = pid;
        this.category = category;
        this.pdescription = pdescription;
        this.date = date;
        this.time = time;
        this.image = image;
    }


    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPdescription() {
        return pdescription;
    }

    public void setPdescription(String pdescription) {
        this.pdescription = pdescription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
