package cheese.burger.brewery.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends ThrownItemEntity {
    public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "damageEntitiesHurtByWater", at = @At("HEAD"))
    void damageEntitiesHurtByWater(CallbackInfo info) {
        Box box = this.getBoundingBox().expand(4.0, 2.0, 4.0);
        List<LivingEntity> list = this.world.getEntitiesByClass(LivingEntity.class, box, Entity::isOnFire);
        for(LivingEntity livingEntity : list) livingEntity.setFireTicks(0);
    }
}
