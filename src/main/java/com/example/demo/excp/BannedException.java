package com.example.demo.excp;

public class BannedException extends RuntimeException {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public BannedException(){
        super();
    }
    public BannedException(String message){
        super(message);
    }
}
