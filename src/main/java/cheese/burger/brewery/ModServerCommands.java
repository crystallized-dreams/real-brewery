package cheese.burger.brewery;

import cheese.burger.brewery.content.ResearchSyncPacket;
import cheese.burger.brewery.system.PlayerPotionRecipeData;
import cheese.burger.brewery.system.PlayerPotionResearchData;
import com.mojang.brigadier.CommandDispatcher;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.ServerCommandSource;
import static net.minecraft.server.command.CommandManager.*;
import com.mojang.brigadier.arguments.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.UUID;

public class ModServerCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("realbrewery")
                .requires(src -> src.hasPermissionLevel(2))
                .then(literal("unlockAll").executes(ctx -> unlockAll(ctx.getSource())))
                .then(literal("resetRecipes").executes(ctx -> resetRecipes(ctx.getSource())))
        );
    }

    private static int unlockAll(ServerCommandSource source) {
        World world = source.getWorld();
        PlayerPotionResearchData data = PlayerPotionResearchData.get(world);
        Collection<ServerPlayerEntity> players = source.getMinecraftServer().getPlayerManager().getPlayerList();

        int count = 0;
        for (Identifier id : Registry.POTION.getIds()) {
            for (ServerPlayerEntity player : players) {
                UUID uuid = player.getUuid();
                if (data.markAsKnown(uuid, id)) {
                    count++;
                }
            }
        }

        data.save(world);
        for (ServerPlayerEntity player : players) {
            for (Identifier id : Registry.POTION.getIds()) {
                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeUuid(player.getUuid());
                buf.writeString(id.toString());
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player,
                        ResearchSyncPacket.ID, buf);
            }
        }

        source.sendFeedback(new TranslatableText("commands.realbrewery.unlock_all.success", count), true);
        return 1;
    }

    private static int resetRecipes(ServerCommandSource source) {
        World world = source.getWorld();
        try {
            PlayerPotionRecipeData.INSTANCE.clear();
            PlayerPotionRecipeData.INSTANCE.saveData(world);

            source.sendFeedback(new TranslatableText("commands.realbrewery.reset_recipes.success"), true);
        } catch (Exception e) {
            source.sendError(new TranslatableText("commands.realbrewery.reset_recipes.fail"));
            e.printStackTrace();
            return 0;
        }

        return 1;
    }
}
