package cheese.burger.brewery.mixin;

import cheese.burger.brewery.content.ModEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(HungerManager.class)
public class HungerManagerMixin {
    @Inject(method = "isNotFull", at = @At("RETURN"), cancellable = true)
    void isNotFull(CallbackInfoReturnable<Boolean> info) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player==null) return;
        if(player.hasStatusEffect(ModEffects.GOAT_TRANSFORM)) info.setReturnValue(false);
    }
}
