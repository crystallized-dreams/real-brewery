package ru.crystallized_dreams.brewery.mixin;

import ru.crystallized_dreams.brewery.RealBrewery;
import ru.crystallized_dreams.brewery.content.ModEffects;
import ru.crystallized_dreams.brewery.content.ModTrackedData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    @Unique private static final Identifier LAVA_WALKER_BUBBLES = new Identifier(RealBrewery.MOD_ID, "textures/gui/lava_breath.png");
    @Unique private static final Identifier GOAT_DASH_INDICATOR = new Identifier(RealBrewery.MOD_ID, "textures/gui/goat_dash.png");

    @Inject(method = "renderStatusBars", at = @At("TAIL"))
    void renderStatusBars(MatrixStack matrices, CallbackInfo info) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null || !player.hasStatusEffect(ModEffects.LAVA_WALKER)) return;

        int breath = player.getDataTracker().get(ModTrackedData.LAVA_BREATH);
        int x = scaledWidth / 2 + 83;
        int y = scaledHeight - 49;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableDepthTest();

        MinecraftClient.getInstance().getTextureManager().bindTexture(LAVA_WALKER_BUBBLES);

        for (int i = 0; i < 10; i++) {
            float v=Math.min(1, Math.max((breath-i*20.f)/20, 0));
            int f=(int)MathHelper.lerp(v,0.f,2.f);
            DrawableHelper.drawTexture(matrices, x - i * 9, y, f * 8, 0, 8, 8, 32, 8);
        }

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    @Inject(method = "render", at = @At("TAIL"))
    void render(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null || !player.hasStatusEffect(ModEffects.GOAT_TRANSFORM)) return;

        int cooldown = player.getDataTracker().get(ModTrackedData.GOAT_DASH_COOLDOWN);
        if (cooldown <= 0) return;

        int x = scaledWidth / 2 - 3;
        int y = scaledHeight / 2 + 8;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableDepthTest();

        MinecraftClient.getInstance().getTextureManager().bindTexture(GOAT_DASH_INDICATOR);
        int f=(int)MathHelper.lerp((float)(cooldown+20)/ModTrackedData.GOAT_DASH_COOLDOWN_MAX,0.f,2.f);
        DrawableHelper.drawTexture(matrices, x, y, 7*(2-f), 0, 7, 8, 32, 8);

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }
}
