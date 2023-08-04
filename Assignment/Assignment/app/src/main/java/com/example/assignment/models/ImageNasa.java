package com.example.assignment.models;

public class ImageNasa {

    //Khai báo các trường dữ liệu lấy về từ nasa
    private int id;
    private String title;
    private String url;
    private String copyright;
    private String date;;
    private String explanation;

    //Tạo contrustor
    public ImageNasa(String title, String url, String copyright, String date, String explanation) {
        this.title = title;
        this.url = url;
        this.copyright = copyright;
        this.date = date;
        this.explanation = explanation;
    }

    //Các phương thức làm việc với các trường dữ liệu

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
