package com.example.assignment.models;

public class Status {

    //Khai báo các trường dữ liệu lấy về từ sercver
    private boolean status;
    private String message;

    private ImageNasa object;

    //Các phương thức làm việc với các trường dữ liệu
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ImageNasa getNasa() {
        return object;
    }

    public void setNasa(ImageNasa object) {
        this.object = object;
    }
}
