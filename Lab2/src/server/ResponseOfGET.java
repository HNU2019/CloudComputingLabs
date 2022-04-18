package src.server;

import java.net.Socket;

public class ResponseOfGET {
    private Socket server;
    private String url;

    public ResponseOfGET(Socket socket, String url) {
        server = socket;
        this.url = url;
    }

    public void response(){

    }
}
