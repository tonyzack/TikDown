package com.nadhholy.tikdownloader.video.models.error;

public class Error {

    private int code;
    private String name;
    private String message;

    public Error(ERRORS error){
        this.code = error.getCode();
        this.name = error.getName();
        this.message = error.getMessage();
    }

    public Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Error(){

    }

    public Error(int code, String name, String message) {
        this.code = code;
        this.name = name;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
