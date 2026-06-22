package ru.crystallized_dreams.brewery.mixin;

import ru.crystallized_dreams.brewery.content.ModEffects;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {
    @Unique
    PlayerEntity player$real_brewery;

    public BlockItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if(entity instanceof PlayerEntity) player$real_brewery=(PlayerEntity)entity;
    }

    @Inject(method="getTranslationKey()Ljava/lang/String;", at=@At("RETURN"), cancellable = true)
    void getTranslationKey$real_brewery(CallbackInfoReturnable<String> info) {
        if(player$real_brewery==null) return;
        if(player$real_brewery.hasStatusEffect(ModEffects.CONFUSION)) {
            info.setReturnValue("§k123456789");
        }
    }
    @Inject(method="appendTooltip", at=@At("HEAD"), cancellable = true)
    void appendTooltip$real_brewery(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo info) {
        if(player$real_brewery==null) return;
        if(player$real_brewery.hasStatusEffect(ModEffects.CONFUSION)) {
            info.cancel();
        }
    }
}
