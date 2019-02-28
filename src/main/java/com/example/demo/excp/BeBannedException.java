package com.example.demo.excp;

public class BeBannedException extends RuntimeException {
    private String msg;
    public BeBannedException(){
        super();
    }
    public BeBannedException(String message){
        super(message);
        this.msg = message;
    }

    public String getMsg() {
        return msg;
    }
}
