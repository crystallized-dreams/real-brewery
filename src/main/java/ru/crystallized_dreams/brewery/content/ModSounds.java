package ru.crystallized_dreams.brewery.content;

import ru.crystallized_dreams.brewery.RealBrewery;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {
    public static final SoundEvent GOAT_IDLE=register("entity.goat.idle");
    public static final SoundEvent GOAT_IMPACT=register("entity.goat.impact");
    public static final SoundEvent GOAT_JUMP=register("entity.goat.jump");
    public static final SoundEvent GOAT_PRE_RAM=register("entity.goat.pre_ram");
    public static final SoundEvent GOAT_SCREAM=register("entity.goat.scream");

    public static void regAll() { }

    private static SoundEvent register(String id) {
        Identifier identifier = new Identifier(RealBrewery.MOD_ID, id);
        return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
    }
}
