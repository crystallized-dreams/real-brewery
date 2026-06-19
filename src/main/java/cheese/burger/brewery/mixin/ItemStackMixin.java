package cheese.burger.brewery.mixin;

import cheese.burger.brewery.content.ModEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Environment(EnvType.CLIENT)
    @Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true)
    public void getTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info) {
        if (player != null && player.hasStatusEffect(ModEffects.CONFUSION)) {
            List<Text> tooltip = info.getReturnValue();
            tooltip.clear();
            tooltip.add(Text.of("§k123456789"));
            info.setReturnValue(tooltip);
        }
    }
}
