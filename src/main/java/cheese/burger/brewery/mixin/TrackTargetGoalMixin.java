package cheese.burger.brewery.mixin;

import cheese.burger.brewery.content.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TrackTargetGoal.class)
public class TrackTargetGoalMixin {
    @Shadow
    protected LivingEntity target;

    @Shadow @Final
    protected MobEntity mob;

    @Inject(method = "canTrack", at = @At("RETURN"), cancellable = true)
    void canTrack(LivingEntity target, TargetPredicate targetPredicate, CallbackInfoReturnable<Boolean> info) {
        if(!info.getReturnValue()) return;
        if(target == null) return;
        if(target==mob.getAttacker()) return;
        if(target.hasStatusEffect(ModEffects.ANONYM)) {
            info.setReturnValue(false);
            this.target = null;
            mob.setTarget(null);
        }
    }

    @Inject(method = "shouldContinue", at = @At("RETURN"), cancellable = true)
    void shouldContinue(CallbackInfoReturnable<Boolean> info) {
        if(!info.getReturnValue()) return;
        if(target==mob.getAttacker()) return;
        if(target!=null && target.hasStatusEffect(ModEffects.ANONYM)) {
            info.setReturnValue(false);
            target = null;
            mob.setTarget(null);
        }
    }
}
