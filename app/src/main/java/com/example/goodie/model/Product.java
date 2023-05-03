package com.example.goodie.model;

public class Product {
    private int id;
    private String productName;
    private double price;
    private String fileName;
    private String fileCode;
    private String downloadUri;
    private long size;


    public Product(int id, String productName, double price, String fileName, String fileCode, String downloadUri, long size) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.fileName = fileName;
        this.fileCode = fileCode;
        this.downloadUri = downloadUri;
        this.size = size;
    }

    public Product(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
