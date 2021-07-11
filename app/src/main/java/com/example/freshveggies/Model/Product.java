package com.example.freshveggies.Model;

public class Product
{
    String pname,quantity,image,price,info,pdate,pid,date,time,id,fname;
    Product(){}

    public Product(String pname, String quantity, String image, String price, String info, String pdate,String pid,String date,String time,String id,String fname) {
        this.pname = pname;
        this.quantity = quantity;
        this.image = image;
        this.price = price;
        this.info = info;
        this.pdate = pdate;
        this.pid=pid;
        this.id=id;
        this.date=date;
        this.time=time;
        this.fname=fname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
}
