package com.example.demo.excp;

public class AuthorityNotMatchException extends RuntimeException {
    private String msg;
    public AuthorityNotMatchException(){
        super();
    }
    public AuthorityNotMatchException(String msg){
        super(msg);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
