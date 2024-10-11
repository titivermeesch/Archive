package io.minki.proxyfranz.discovery;

public class GameServer {
    private String name;
    private String address;
    private int port;

    public GameServer(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
    }


    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
