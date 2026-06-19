package cheese.burger.brewery.mixin;

import cheese.burger.brewery.content.ModEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Unique
    boolean hasLavaWalker;
    @Unique
    boolean onLava;
    @Inject(method="damage",at=@At("HEAD"), cancellable = true)
    void damageHead(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        if(source==DamageSource.LAVA&&hasLavaWalker&&onLava) info.setReturnValue(false);
    }
    @Inject(method="damage",at=@At("TAIL"))
    void damageTail(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        Entity attacker=source.getAttacker();
        if(livingEntity.hasStatusEffect(ModEffects.SPIKY)&&attacker!=null&&!source.isProjectile()) attacker.damage(DamageSource.thorns(livingEntity), amount/2);
    }
    @Inject(method = "tick", at = @At("RETURN"))
    void tick(CallbackInfo info) {
        LivingEntity entity = (LivingEntity) (Object) this;
        hasLavaWalker=entity.hasStatusEffect(ModEffects.LAVA_WALKER);
        onLava=false;
    }
    @Inject(method = "canWalkOnFluid", at = @At("RETURN"), cancellable = true)
    private void canWalkOnFluid(Fluid fluid, CallbackInfoReturnable<Boolean> info) {
        if(hasLavaWalker) {
            if (fluid == Fluids.LAVA || fluid == Fluids.FLOWING_LAVA) {
                info.setReturnValue(true);
                onLava=true;
            }
        }
    }
}
