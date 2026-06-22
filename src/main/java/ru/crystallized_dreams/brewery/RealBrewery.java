package ru.crystallized_dreams.brewery;

import ru.crystallized_dreams.brewery.content.*;
import ru.crystallized_dreams.brewery.system.PlayerPotionRecipeData;
import ru.crystallized_dreams.brewery.system.PlayerPotionResearchData;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class RealBrewery implements ModInitializer {
	public static final String MOD_ID = "real-brewery";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("downloading free robux");
		ModItems.regAll();
		ModBlocks.regAll();
		ModEffects.regAll();
		ModTrackedData.regAll();
		ModSounds.regAll();
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			ServerWorld world = server.getWorld(net.minecraft.world.World.OVERWORLD);
			if(world != null) PlayerPotionRecipeData.INSTANCE.loadData(world);
		});
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.player;

			World world = server.getOverworld();
			if (world != null) {
				PlayerPotionResearchData data = PlayerPotionResearchData.get(world);
				Set<String> knownIds = data.getAllKnown(player.getUuid());

				if (knownIds != null && !knownIds.isEmpty()) {
					for (String idStr : knownIds) {
						Identifier potionId = new Identifier(idStr);
						PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
						buf.writeUuid(player.getUuid());
						buf.writeString(potionId.toString());

						ServerPlayNetworking.send(player, ResearchSyncPacket.ID, buf);
					}
				}
			}
		});
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> ModServerCommands.register(dispatcher));
	}
}