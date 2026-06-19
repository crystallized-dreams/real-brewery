package cheese.burger.brewery.content;

import cheese.burger.brewery.system.PlayerPotionRecipeData;
import cheese.burger.brewery.RealBrewery;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.awt.*;

public class ModEffects {
    public static final StatusEffect BOUNCY=register("bouncy",new BareStatusEffect(StatusEffectType.NEUTRAL, new Color(51, 234, 24, 255).getRGB()));
    public static final Potion BOUNCY_POTION=register("bouncy",new Potion(new StatusEffectInstance(BOUNCY, 3600)));
    public static final Potion LONG_BOUNCY_POTION=register("long_bouncy",new Potion(new StatusEffectInstance(BOUNCY, 9600)));
    public static final StatusEffect CHEESEFICATION=register("cheesefication",new BareStatusEffect(StatusEffectType.HARMFUL, new Color(247, 216, 37, 255).getRGB()));
    public static final Potion CHEESEFICATION_POTION=register("cheesefication",new Potion(new StatusEffectInstance(CHEESEFICATION, 3600)));
    public static final StatusEffect DWARF=register("dwarf",new BareStatusEffect(StatusEffectType.NEUTRAL, new Color(45, 191, 22, 255).getRGB()));
    public static final Potion DWARF_POTION=register("dwarf",new Potion(new StatusEffectInstance(DWARF, 3600)));
    public static final Potion LONG_DWARF_POTION=register("long_dwarf",new Potion(new StatusEffectInstance(DWARF, 9600)));
    public static final StatusEffect GIANT=register("giant",new BareStatusEffect(StatusEffectType.NEUTRAL, new Color(247, 37, 243, 255).getRGB()));
    public static final Potion GIANT_POTION=register("giant",new Potion(new StatusEffectInstance(GIANT, 3600)));
    public static final Potion LONG_GIANT_POTION=register("long_giant",new Potion(new StatusEffectInstance(GIANT, 9600)));
    public static final StatusEffect MAGNETISM=register("magnetism",new BareStatusEffect(StatusEffectType.NEUTRAL, new Color(131, 37, 247, 255).getRGB()));
    public static final Potion MAGNETISM_POTION=register("magnetism",new Potion(new StatusEffectInstance(MAGNETISM, 3600)));
    public static final Potion LONG_MAGNETISM_POTION=register("long_magnetism",new Potion(new StatusEffectInstance(MAGNETISM, 9600)));
    public static final StatusEffect STICKY=register("sticky",new BareStatusEffect(StatusEffectType.NEUTRAL, new Color(247, 163, 37, 255).getRGB()));
    public static final Potion STICKY_POTION=register("sticky",new Potion(new StatusEffectInstance(STICKY, 3600)));
    public static final Potion LONG_STICKY_POTION=register("long_sticky",new Potion(new StatusEffectInstance(STICKY, 9600)));
    public static final StatusEffect CONFUSION=register("confusion",new BareStatusEffect(StatusEffectType.HARMFUL, new Color(135, 201, 205, 255).getRGB()));
    public static final Potion CONFUSION_POTION=register("confusion",new Potion(new StatusEffectInstance(CONFUSION, 3600)));
    public static final Potion LONG_CONFUSION_POTION=register("long_confusion",new Potion(new StatusEffectInstance(CONFUSION, 9600)));
    public static final StatusEffect SPIKY=register("spiky",new BareStatusEffect(StatusEffectType.NEUTRAL, new Color(70, 124, 77, 255).getRGB()));
    public static final Potion SPIKY_POTION=register("spiky",new Potion(new StatusEffectInstance(SPIKY, 3600)));
    public static final Potion LONG_SPIKY_POTION=register("long_spiky",new Potion(new StatusEffectInstance(SPIKY, 9600)));
    public static final StatusEffect ANONYM=register("anonym",new BareStatusEffect(StatusEffectType.BENEFICIAL, new Color(117, 27, 156, 255).getRGB()));
    public static final Potion ANONYM_POTION=register("anonym",new Potion(new StatusEffectInstance(ANONYM, 3600)));
    public static final Potion LONG_ANONYM_POTION=register("long_anonym",new Potion(new StatusEffectInstance(ANONYM, 9600)));
    public static final StatusEffect GOAT_TRANSFORM=register("goat_transform",new BareStatusEffect(StatusEffectType.BENEFICIAL, new Color(211, 37, 63, 255).getRGB()));
    public static final Potion GOAT_TRANSFORM_POTION=register("goat_transform",new Potion(new StatusEffectInstance(GOAT_TRANSFORM, 3600)));
    public static final Potion LONG_GOAT_TRANSFORM_POTION=register("long_goat_transform",new Potion(new StatusEffectInstance(GOAT_TRANSFORM, 9600)));
    public static final StatusEffect LAVA_WALKER=register("lava_walker",new BareStatusEffect(StatusEffectType.BENEFICIAL, new Color(234, 68, 18, 255).getRGB()));
    public static final Potion LAVA_WALKER_POTION=register("lava_walker",new Potion(new StatusEffectInstance(LAVA_WALKER, 3600)));
    public static final Potion LONG_LAVA_WALKER_POTION=register("long_lava_walker",new Potion(new StatusEffectInstance(LAVA_WALKER, 9600)));
    public static final StatusEffect SOCIAL_DISTANCING=register("social_distancing",new BareStatusEffect(StatusEffectType.BENEFICIAL, new Color(99, 166, 177, 255).getRGB()));
    public static final Potion SOCIAL_DISTANCING_POTION=register("social_distancing",new Potion(new StatusEffectInstance(SOCIAL_DISTANCING, 3600)));
    public static final Potion LONG_SOCIAL_DISTANCING_POTION=register("long_social_distancing",new Potion(new StatusEffectInstance(SOCIAL_DISTANCING, 9600)));

    public static void regAll() {
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(BOUNCY_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(LONG_BOUNCY_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(CHEESEFICATION_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(DWARF_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(LONG_DWARF_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(GIANT_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(LONG_GIANT_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(MAGNETISM_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(LONG_MAGNETISM_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(STICKY_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(LONG_STICKY_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(CONFUSION_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(LONG_CONFUSION_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(SPIKY_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(LONG_SPIKY_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(ANONYM_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(LONG_ANONYM_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(GOAT_TRANSFORM_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(LONG_GOAT_TRANSFORM_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(LAVA_WALKER_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(LONG_LAVA_WALKER_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(SOCIAL_DISTANCING_POTION);
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(LONG_SOCIAL_DISTANCING_POTION);
    }

    private static StatusEffect register(String id, StatusEffect entry) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(RealBrewery.MOD_ID, id), entry);
    }

    private static Potion register(String id, Potion potion) {
        return Registry.register(Registry.POTION, new Identifier(RealBrewery.MOD_ID, id), potion);
    }
}
