package ru.crystallized_dreams.brewery;

import ru.crystallized_dreams.brewery.content.ResearchSyncPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class RealBreweryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ResearchSyncPacket.registerReceiver();
        ClientPlayConnectionEvents.DISCONNECT.register((handler, connection) -> {
            ClientResearchCache.clear();
        });

    }
}
