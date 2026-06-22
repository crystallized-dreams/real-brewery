package ru.crystallized_dreams.brewery.mixin;

import ru.crystallized_dreams.brewery.content.ModEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(TranslatableText.class)
public class TranslatableTextMixin {
    @Environment(EnvType.CLIENT)
    @Inject(method = "visitSelf(Lnet/minecraft/text/StringVisitable$StyledVisitor;Lnet/minecraft/text/Style;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    public <T> void visitSelfConfused(StringVisitable.StyledVisitor<T> visitor, net.minecraft.text.Style style, CallbackInfoReturnable<Optional<T>> info) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        Screen screen=MinecraftClient.getInstance().currentScreen;
        if (player != null && player.hasStatusEffect(ModEffects.CONFUSION) && screenNotPause(screen)) {
            Optional<T> result = StringVisitable.plain("§k123456789").visit(visitor, style);
            info.setReturnValue(result);
        }
    }

    @Unique
    private boolean screenNotPause(Screen screen) {
        if(screen==null) return true;
        return !screen.isPauseScreen();
    }
}
