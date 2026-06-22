package ru.crystallized_dreams.brewery.mixin;

import ru.crystallized_dreams.brewery.ClientResearchCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.LingeringPotionItem;
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

import java.util.List;

@Mixin(LingeringPotionItem.class)
public class LingeringPotionItemMixin {
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
            tooltip.add(new TranslatableText("text.real-brewery.undiscovered.splash").formatted(Formatting.YELLOW));
            info.cancel();
        }
    }
}
