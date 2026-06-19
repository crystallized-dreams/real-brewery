package cheese.burger.brewery.mixin;

import cheese.burger.brewery.system.PlayerPotionRecipeData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {
    @Inject(method="isValidIngredient", at=@At("HEAD"), cancellable=true)
    private static void isValidIngredient(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(true);
    }

    @Inject(method="hasRecipe", at=@At("HEAD"), cancellable=true)
    private static void hasRecipe(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(true);
    }

    @Inject(method="registerPotionRecipe", at=@At("HEAD"))
    private static void registerPotionRecipe(Potion input, Item item, Potion output, CallbackInfo info) {
        PlayerPotionRecipeData.INSTANCE.addAvailablePotion(output);
    }
}
