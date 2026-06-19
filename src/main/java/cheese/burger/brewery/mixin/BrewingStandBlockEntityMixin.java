package cheese.burger.brewery.mixin;

import cheese.burger.brewery.system.PlayerPotionRecipeData;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingStandBlockEntityMixin {
    @Shadow
    public DefaultedList<ItemStack> inventory;

    @Inject(method = "craft", at = @At("HEAD"), cancellable = true)
    private void customCraft(CallbackInfo info) {
        ItemStack ingredient = inventory.get(3);

        for (int i = 0; i < 3; i++) {
            ItemStack potionStack=inventory.get(i);
            if(potionStack.isEmpty()) continue;

            BrewingStandBlockEntity blockEntity=(BrewingStandBlockEntity)(Object)this;
            if(blockEntity==null) return;
            if(blockEntity.getWorld()==null) return;
            if(blockEntity.getWorld().getServer()==null) return;
            BlockState below=blockEntity.getWorld().getBlockState(blockEntity.getPos().down());
            boolean heated=below.isIn(BlockTags.CAMPFIRES);
            if(!heated) heated=below.isIn(BlockTags.FIRE);
            if(!heated) heated=below.isOf(Blocks.LAVA);
            BlockState top=blockEntity.getWorld().getBlockState(blockEntity.getPos().up());
            boolean ender=top.isOf(Blocks.DRAGON_HEAD);

            inventory.set(i, PlayerPotionRecipeData.INSTANCE
                    .get(potionStack, ingredient, blockEntity.getWorld().getServer().getOverworld(),
                            heated, ender));
        }
        ingredient.decrement(1);
        if (ingredient.getCount() < ((BrewingStandBlockEntity)(Object)this).inventory.get(3).getCount() + 1) {
            info.cancel();
        }
    }
}
