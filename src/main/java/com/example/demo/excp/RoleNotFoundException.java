package com.example.demo.excp;

public class RoleNotFoundException extends RuntimeException {
    private String msg;
    public RoleNotFoundException(){
        super();
    }
    public RoleNotFoundException(String msg){
        super(msg);
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }
}
