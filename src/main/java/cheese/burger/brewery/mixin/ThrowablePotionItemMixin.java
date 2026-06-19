package cheese.burger.brewery.mixin;

import cheese.burger.brewery.RealBrewery;
import cheese.burger.brewery.system.PlayerPotionResearchData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThrowablePotionItem.class)
public abstract class ThrowablePotionItemMixin extends PotionItem {
    public ThrowablePotionItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "use", at = @At("HEAD"))
    void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> info) {
        if(user instanceof PlayerEntity && !world.isClient())
            PlayerPotionResearchData.unlock((ServerPlayerEntity)user,user.getStackInHand(hand),world);
    }
}
