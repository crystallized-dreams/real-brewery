package ru.crystallized_dreams.brewery.mixin;

import ru.crystallized_dreams.brewery.ClientResearchCache;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TippedArrowItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(TippedArrowItem.class)
public class TippedArrowItemMixin {
    @Inject(method = "getTranslationKey", at=@At("RETURN"), cancellable = true)
    public void getTranslationKey(ItemStack stack, CallbackInfoReturnable<String> info) {
        ClientPlayerEntity player = net.minecraft.client.MinecraftClient.getInstance().player;
        if (player == null) return;
        Identifier potionId = Registry.POTION.getId(PotionUtil.getPotion(stack));
        if (!ClientResearchCache.isKnown(player.getUuid(), potionId))
            info.setReturnValue("text.real-brewery.unknown_arrow");
    }

    @Inject(method = "appendTooltip", at=@At("HEAD"), cancellable = true)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo info) {
        ClientPlayerEntity player = net.minecraft.client.MinecraftClient.getInstance().player;
        if (player == null || world == null) {
            return;
        }

        Identifier potionId = Registry.POTION.getId(PotionUtil.getPotion(stack));
        if (!ClientResearchCache.isKnown(player.getUuid(), potionId)) {
            tooltip.add(new TranslatableText("text.real-brewery.undiscovered").formatted(Formatting.GRAY));
            tooltip.add(Text.of(""));
            tooltip.add(new TranslatableText("text.real-brewery.undiscovered.arrow").formatted(Formatting.YELLOW));
            info.cancel();
        }
    }
}
