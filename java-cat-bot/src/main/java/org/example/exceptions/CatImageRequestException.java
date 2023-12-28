package org.example.exceptions;

public class CatImageRequestException extends Exception{
    public CatImageRequestException() {
        super("GET Cat Image Request Fail");
    }
}
