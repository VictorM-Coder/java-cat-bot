package org.example.exceptions;

public class HttpClientException extends Exception{
    public HttpClientException() {
        super("Build HttpClient fail");
    }
}
