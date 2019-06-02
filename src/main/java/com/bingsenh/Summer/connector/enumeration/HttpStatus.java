package com.bingsenh.Summer.connector.enumeration;

public enum  HttpStatus {
    OK(200),NOT_FOUND(404),SERVER_ERROR(500),BAD_REQUEST(404),REDIRECT(302);
    private int code;
    HttpStatus(int code){
        this.code = code;
    }
    public int getCode(){
        return code;
    }

}