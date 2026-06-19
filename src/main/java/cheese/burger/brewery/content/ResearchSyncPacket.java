package cheese.burger.brewery.content;

import cheese.burger.brewery.ClientResearchCache;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ResearchSyncPacket {
    public static final Identifier ID = new Identifier("real-brewery", "research_sync");

    public static void registerReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (client, handler, buf, responseSender) -> {
            UUID playerId = buf.readUuid();
            String potionIdStr = buf.readString();
            Identifier potionId = new Identifier(potionIdStr);

            client.execute(() -> {
                ClientResearchCache.markAsKnown(playerId, potionId);
            });
        });
    }
}
