package com.example.demo.excp;

public class InviteIntervalException extends RuntimeException {
    private String msg;
    public InviteIntervalException(){
        super();
    }
    public InviteIntervalException(String msg){
        super(msg);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

}
