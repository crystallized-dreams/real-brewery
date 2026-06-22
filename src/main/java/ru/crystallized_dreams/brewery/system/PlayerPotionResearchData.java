package ru.crystallized_dreams.brewery.system;

import ru.crystallized_dreams.brewery.content.ResearchSyncPacket;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.io.File;
import java.util.*;

public class PlayerPotionResearchData {
    private final Map<String, Set<String>> players=new HashMap<>();
    private boolean isDirty=false;

    public static void unlock(ServerPlayerEntity player, ItemStack stack, World world) {
        //if (stack.getItem()!= Items.POTION) return;
        Identifier potionId = Registry.POTION.getId(PotionUtil.getPotion(stack));
        PlayerPotionResearchData data = PlayerPotionResearchData.get(world);
        if (data.markAsKnown(player.getUuid(), potionId)) {
            data.save(world);
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeUuid(player.getUuid());
            buf.writeString(potionId.toString());
            ServerPlayNetworking.send(player, ResearchSyncPacket.ID, buf);
            player.sendMessage(new net.minecraft.text.TranslatableText("text.real-brewery.new_discover",
                    new net.minecraft.text.TranslatableText("effect." + potionId.getNamespace() + "." + potionId.getPath())), true);
        }
    }

    public static PlayerPotionResearchData get(World world) {
        if(!world.isClient() && world.getServer() == null) {
            return new PlayerPotionResearchData();
        }
        PlayerPotionResearchData data=new PlayerPotionResearchData();
        data.load(world);
        return data;
    }

    private void load(World world) {
        try {
            File file = getDataFile(world);
            if (!file.exists()) return;

            NbtCompound nbt = NbtIo.readCompressed(file);
            if (nbt == null) return;

            NbtCompound playersTag = nbt.getCompound("players");

            for (String uuid : playersTag.getKeys()) {
                Set<String> known = new HashSet<>();
                NbtList list = playersTag.getList(uuid, 8);

                for (int i = 0; i < list.size(); i++) {
                    known.add(list.getString(i));
                }
                this.players.put(uuid, known);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(World world) {
        if (!isDirty) return;
        try {
            File file = getDataFile(world);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) parentFile.mkdirs();

            NbtCompound tag = new NbtCompound();
            NbtCompound playersTag = new NbtCompound();

            for (Map.Entry<String, Set<String>> entry : players.entrySet()) {
                NbtList list = new NbtList();
                for (String id : entry.getValue()) {
                    list.add(NbtString.of(id));
                }
                playersTag.put(entry.getKey(), list);
            }

            tag.put("players", playersTag);
            NbtIo.writeCompressed(tag, file);
            isDirty = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private File getDataFile(World world) {
        if (world.getServer() != null) {
            String levelName = world.getServer().getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString();
            File savesDir = new File(System.getProperty("user.dir"), "saves");
            File worldDir = new File(savesDir, levelName);
            return new File(worldDir, "data/realbrewery_research.dat");
        }
        return new File("config/realbrewery_research.dat");
    }

    public boolean isKnown(UUID uuid, Identifier id) {
        Set<String> known=players.get(uuid.toString());
        return known!=null && known.contains(id.toString());
    }
    public boolean markAsKnown(UUID uuid, Identifier id) {
        Set<String> known=players.getOrDefault(uuid.toString(), new HashSet<>());
        if(!known.contains(id.toString())) {
            known.add(id.toString());
            players.put(uuid.toString(), known);
            isDirty=true;
            return true;
        }
        return false;
    }

    public Set<String> getAllKnown(UUID uuid) {
        return players.get(uuid.toString());
    }
}
