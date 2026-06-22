package ru.crystallized_dreams.brewery.mixin;

import ru.crystallized_dreams.brewery.content.ModEffects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GrassBlock.class)
public abstract class GrassBlockMixin extends SpreadableBlock {
    protected GrassBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(player.hasStatusEffect(ModEffects.GOAT_TRANSFORM)) {
            player.getHungerManager().add(1,0);
            if(!world.isClient()) world.playSound(null, pos, getSoundGroup(state).getBreakSound(), SoundCategory.PLAYERS, 1, 1);
            world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
