package cheese.burger.brewery.mixin;

import cheese.burger.brewery.content.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MeleeAttackGoal.class)
public class MeleeAttackGoalMixin {
    @Shadow @Final
    protected PathAwareEntity mob;

    @Inject(method = "canStart", at = @At("RETURN"), cancellable = true)
    void canTrack(CallbackInfoReturnable<Boolean> info) {
        if(!info.getReturnValue()) return;
        LivingEntity target = mob.getTarget();
        if(target == null) return;
        if(target==mob.getAttacker()) return;
        if(target.hasStatusEffect(ModEffects.ANONYM)) {
            info.setReturnValue(false);
            mob.setTarget(null);
        }
    }

    @Inject(method = "shouldContinue", at = @At("RETURN"), cancellable = true)
    void shouldContinue(CallbackInfoReturnable<Boolean> info) {
        if(!info.getReturnValue()) return;
        LivingEntity target = this.mob.getTarget();
        if(target==mob.getAttacker()) return;
        if(target!=null && target.hasStatusEffect(ModEffects.ANONYM)) {
            info.setReturnValue(false);
            mob.setTarget(null);
        }
    }
}
