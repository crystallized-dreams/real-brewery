package ru.crystallized_dreams.brewery.mixin;

import ru.crystallized_dreams.brewery.ClientResearchCache;
import ru.crystallized_dreams.brewery.system.PlayerPotionResearchData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(PotionItem.class)
public class PotionItemMixin {
    @Inject(method = "getTranslationKey", at=@At("RETURN"), cancellable = true)
    public void getTranslationKey(ItemStack stack, CallbackInfoReturnable<String> info) {
        ClientPlayerEntity player = net.minecraft.client.MinecraftClient.getInstance().player;
        if (player == null) return;
        Identifier potionId = Registry.POTION.getId(PotionUtil.getPotion(stack));
        if (!ClientResearchCache.isKnown(player.getUuid(), potionId)) {
            Item item=stack.getItem();
            if(item==Items.POTION) info.setReturnValue("text.real-brewery.unknown_potion");
            else if(item==Items.SPLASH_POTION) info.setReturnValue("text.real-brewery.unknown_splash_potion");
            else if(item==Items.LINGERING_POTION) info.setReturnValue("text.real-brewery.unknown_lingering_potion");
        }
    }

    @Inject(method = "appendTooltip", at=@At("HEAD"), cancellable = true)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo info) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null || world == null) {
            return;
        }

        Identifier potionId = Registry.POTION.getId(PotionUtil.getPotion(stack));
        if (!ClientResearchCache.isKnown(player.getUuid(), potionId)) {
            tooltip.add(new TranslatableText("text.real-brewery.undiscovered").formatted(Formatting.GRAY));
            tooltip.add(Text.of(""));
            tooltip.add(new TranslatableText(getUndiscoveredText(stack.getItem())).formatted(Formatting.YELLOW));
            info.cancel();
        }
    }

    @Unique
    private String getUndiscoveredText(Item item) {
        if(item==Items.POTION) return "text.real-brewery.undiscovered.drink";
        return "text.real-brewery.undiscovered.splash";
    }

    @Inject(method = "finishUsing", at=@At("HEAD"))
    void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info) {
        if(user instanceof PlayerEntity && !world.isClient())
            PlayerPotionResearchData.unlock((ServerPlayerEntity)user,stack,world);
    }
}
