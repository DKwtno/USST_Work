package com.example.demo.excp;

public class ProjectFullException extends RuntimeException {
    private String msg;

    public String getMsg() {
        return msg;
    }
    public ProjectFullException(){
        super();
    }
    public ProjectFullException(String msg){
        super(msg);
        this.msg = msg;
    }
}
