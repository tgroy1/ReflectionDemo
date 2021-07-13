package main.external.interfaces;

public interface HttpClient {

    void initialize();

    String sendRequest(String request);
}
