package io.minki.gapi.maps;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.lingala.zip4j.ZipFile;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import proto.Mappy;
import proto.MappyServiceGrpc;

import java.io.*;

public class MapsManager {
    private final InstanceManager instanceManager;
    private final MappyServiceGrpc.MappyServiceBlockingStub stub;
    private Mappy.Map lastLoadedMap;

    public MapsManager(InstanceManager instanceManager) {
        this.instanceManager = instanceManager;

        ManagedChannel channel = ManagedChannelBuilder.forAddress("mappy", 3000).usePlaintext().build();
        MappyServiceGrpc.MappyServiceBlockingStub stub = MappyServiceGrpc.newBlockingStub(channel);

        this.stub = stub;
    }

    public Mappy.Map getRandomMap(String game) {
        Mappy.GetRandomMapRequest request = Mappy.GetRandomMapRequest.newBuilder().setGame(game).build();
        Mappy.GetRandomMapResponse response = stub.getRandomMap(request);

        return response.getMap();
    }

    public InstanceContainer loadMap(Mappy.Map map) {
        try {
            File mapZip = new File("world.zip");
            OutputStream outStream = new FileOutputStream(mapZip);
            System.out.println(map.getData().size());
            outStream.write(map.getData().toByteArray());
            outStream.close();

            InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
            instanceContainer.setChunkLoader(new AnvilLoader("worlds/world"));
            instanceContainer.setChunkSupplier(LightingChunk::new);

            new ZipFile("world.zip").extractAll("worlds/world/region");

            File folder = new File("worlds/world/region");
            File[] listOfFiles = folder.listFiles();
            if(listOfFiles != null) {
                for (File listOfFile : listOfFiles) {
                    String regionName = listOfFile.getName();
                    String[] parts = regionName.split("\\.");
                    instanceContainer.loadChunk(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                }
            }

            lastLoadedMap = map;
            return instanceContainer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Mappy.Map getLastLoadedMap() {
        return lastLoadedMap;
    }
}
