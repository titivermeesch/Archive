package io.minki.gapi;

import io.minki.gapi.agones.AgonesClient;
import io.minki.gapi.maps.MapsManager;
import net.minestom.server.MinecraftServer;

public class Gapi {
    private MapsManager mapsManager;
    private AgonesClient agonesClient;

    public Gapi() {
    }

    public AgonesClient getAgonesClient() {
        if (this.agonesClient == null) {
            this.agonesClient = new AgonesClient();
        }

        return agonesClient;
    }

    public MapsManager getMapsManager() {
        if (this.mapsManager == null) {
            this.mapsManager = new MapsManager(MinecraftServer.getInstanceManager());
        }

        return this.mapsManager;
    }
}
