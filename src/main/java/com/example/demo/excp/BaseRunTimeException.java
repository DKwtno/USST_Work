package com.example.demo.excp;

public class BaseRunTimeException extends RuntimeException {
    private String message;
    public BaseRunTimeException(){
        super();
    }
    public BaseRunTimeException(String message){
        super(message);
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
