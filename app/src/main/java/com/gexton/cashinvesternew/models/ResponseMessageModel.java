package com.gexton.cashinvesternew.models;

public class ResponseMessageModel {
    public boolean status;
    public String msg;

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                '}';
    }
}
