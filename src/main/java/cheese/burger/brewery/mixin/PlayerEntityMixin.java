package cheese.burger.brewery.mixin;

import cheese.burger.brewery.ModBlocks;
import cheese.burger.brewery.RealBrewery;
import cheese.burger.brewery.content.ModEffects;
import cheese.burger.brewery.content.ModSounds;
import cheese.burger.brewery.content.ModTrackedData;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Unique
    private boolean grounded;
    @Unique
    private boolean crouched;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method="isInvulnerableTo", at=@At("HEAD"), cancellable = true)
    void isInvulnerableTo$real_brewery(DamageSource damageSource, CallbackInfoReturnable<Boolean> info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if(player.hasStatusEffect(ModEffects.BOUNCY) && damageSource==DamageSource.FALL) {
            info.setReturnValue(true);
        }
    }
    @Inject(method="initDataTracker", at=@At("HEAD"))
    void initDataTrackertick$real_brewery(CallbackInfo info) {
        if(!((Object)this instanceof PlayerEntity)) return;
        DataTracker dataTracker=((PlayerEntity)(Object)this).getDataTracker();
        dataTracker.startTracking(ModTrackedData.BOUNCE_STRENGTH, 0.f);
        dataTracker.startTracking(ModTrackedData.GOAT_DASH_COOLDOWN, 0);
        dataTracker.startTracking(ModTrackedData.LAVA_BREATH, 0);
        dataTracker.startTracking(ModTrackedData.LAVA_BREATH_DAMAGE_COOL, 0);
    }
    @Inject(method="tick", at=@At("HEAD"))
    void tick$real_brewery(CallbackInfo info) {
        PlayerEntity player=(PlayerEntity)(Object)this;
        grounded=player.world.getBlockState(player.getBlockPos().down()).isSolidBlock(player.world,player.getBlockPos().down());
        if(player.hasStatusEffect(ModEffects.CHEESEFICATION)) {
            BlockPos below=player.getBlockPos().down();
            BlockState state=player.world.getBlockState(below);
            if(!player.world.isClient() && state.isSolidBlock(player.world,below)) {
                player.world.setBlockState(below, ModBlocks.CHEESE_BLOCK.getDefaultState());
            }
        }
        if(player.hasStatusEffect(ModEffects.MAGNETISM)) tickMagnetism(player);
        if(player.hasStatusEffect(ModEffects.STICKY)) tickSticky(player);
        if(player.hasStatusEffect(ModEffects.SOCIAL_DISTANCING)) tickSocialDistancing(player);
        if(player.hasStatusEffect(ModEffects.LAVA_WALKER)) {
            int breath=player.getDataTracker().get(ModTrackedData.LAVA_BREATH);
            BlockState state=player.world.getBlockState(player.getBlockPos());
            boolean water=state.isOf(Blocks.WATER);
            if(!state.isOf(Blocks.LAVA)) {
                player.getDataTracker().set(ModTrackedData.LAVA_BREATH, Math.min(15 * 20, (breath + ((water)?4:1))));
            } else player.getDataTracker().set(ModTrackedData.LAVA_BREATH, Math.max(0,breath-1));
            if(breath>=15*20) {
                int cool=player.getDataTracker().get(ModTrackedData.LAVA_BREATH_DAMAGE_COOL);
                if(cool<=0) {
                    player.damage(DamageSource.MAGIC, 1);
                    cool=20*5+1;
                }
                player.getDataTracker().set(ModTrackedData.LAVA_BREATH_DAMAGE_COOL,Math.max(0,cool-1));
            }
        }

        tickScale(player);
    }
    @Inject(method = "setFireTicks", at = @At("RETURN"), cancellable = true)
    void setFireTicks(int ticks, CallbackInfo info) {
        PlayerEntity player=(PlayerEntity)(Object)this;
        if(!player.hasStatusEffect(ModEffects.LAVA_WALKER)) return;
        if(player.world.getBlockState(player.getBlockPos()).isOf(Blocks.LAVA)) {
            info.cancel();
        }
    }
    @Inject(method="tickMovement", at=@At("HEAD"))
    void tickMovement$real_brewery(CallbackInfo info) {
        PlayerEntity player=(PlayerEntity)(Object)this;
        if(!player.hasStatusEffect(ModEffects.GOAT_TRANSFORM)) {
            player.getDataTracker().set(ModTrackedData.GOAT_DASH_COOLDOWN,0);
            return;
        }
        int cooldown=player.getDataTracker().get(ModTrackedData.GOAT_DASH_COOLDOWN);
        if(crouched!=player.isSneaking()) {
            crouched=player.isSneaking();
            if(crouched&&world.random.nextInt(3)==0) {
                world.playSound(null, player.getBlockPos(), cooldown==0?
                        ModSounds.GOAT_PRE_RAM:
                                world.random.nextBoolean()?ModSounds.GOAT_SCREAM:ModSounds.GOAT_IDLE,
                        SoundCategory.PLAYERS,1,1);
            }
        }
        if(cooldown>0) {
            player.getDataTracker().set(ModTrackedData.GOAT_DASH_COOLDOWN,cooldown-1);
            return;
        }
        if(!jumping||!grounded||!crouched) return;
        Vec3d lookVec = getRotationVector();
        double dashStrength = 1.25;
        player.setVelocity(lookVec.x * dashStrength, 0.4, lookVec.z * dashStrength);
        player.getDataTracker().set(ModTrackedData.GOAT_DASH_COOLDOWN, ModTrackedData.GOAT_DASH_COOLDOWN_MAX);
        world.playSound(null, player.getBlockPos(), ModSounds.GOAT_JUMP, SoundCategory.PLAYERS, 1, 1);
        jumping=false;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if(player.hasStatusEffect(ModEffects.BOUNCY)) {
            Vec3d velocity = player.getVelocity();
            if(!player.isSneaking()
                    &&heightDifference<-0.3
                    &&grounded) {
                float bounceStrength = getBounceStrength(player);
                if (bounceStrength <= 0) bounceStrength = (float)Math.min(Math.abs(heightDifference)*1.25, 5.0);
                if (bounceStrength > 0.1f) {
                    player.setVelocity(velocity.x, bounceStrength, velocity.z);
                    player.setOnGround(false);
                    player.getDataTracker().set(ModTrackedData.BOUNCE_STRENGTH, bounceStrength * 0.8f);
                } else resetBounceStrength(player);
            }
            if(grounded&&heightDifference>-0.3) resetBounceStrength(player);
        } else resetBounceStrength(player);
        super.fall(heightDifference, onGround, landedState, landedPosition);
    }

    @Unique
    private void resetBounceStrength(PlayerEntity player) {
        player.getDataTracker().set(ModTrackedData.BOUNCE_STRENGTH, 0.f);
    }

    @Unique
    private float getBounceStrength(PlayerEntity player) {
        DataTracker dataTracker=player.getDataTracker();
        return dataTracker.get(ModTrackedData.BOUNCE_STRENGTH);
    }

    @Unique
    void tickSocialDistancing(PlayerEntity player) {
        double radius = 4.5;
        double strength = 0.3;

        Box searchBox = player.getBoundingBox().expand(radius);
        List<LivingEntity> nearbyEntities = player.world.getEntitiesByClass(LivingEntity.class, searchBox, e -> true);

        for (LivingEntity other : nearbyEntities) {
            if(other==player) continue;
            Vec3d playerPos = player.getPos();
            Vec3d otherPos = other.getPos();

            Vec3d direction = otherPos.subtract(playerPos);
            double distance = direction.length();

            if (distance < radius && distance > 0.001) {
                Vec3d normalizedDir = direction.normalize();

                Vec3d currentVel = other.getVelocity();
                Vec3d newVel = currentVel.add(normalizedDir.multiply(strength));
                other.setVelocity(newVel);
            }
        }
    }

    @Unique
    void tickMagnetism(PlayerEntity player) {
        //if(player.world.isClient()) return;
        double range=10.0;
        List<ItemEntity> items = player.world.getEntitiesByClass(ItemEntity.class, player.getBoundingBox().expand(range), item -> true);
        for(ItemEntity item : items) {
            if(item.cannotPickup()) continue;

            Vec3d playerPos = player.getPos();
            Vec3d itemPos = item.getPos();
            Vec3d direction = playerPos.subtract(itemPos).normalize();

            double speed = 0.5;
            item.setVelocity(direction.multiply(speed));
        }

        double searchRange = 5.0;
        BlockPos center = player.getBlockPos();

        Vec3d currentVel = player.getVelocity();
        for (int x = -(int)searchRange; x <= (int)searchRange; x++) {
            for (int y = -(int)searchRange; y <= (int)searchRange; y++) {
                for (int z = -(int)searchRange; z <= (int)searchRange; z++) {
                    BlockPos pos = center.add(x, y, z);
                    if (player.world.getBlockState(pos).isOf(Blocks.IRON_BLOCK)) {
                        Vec3d blockCenter = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                        Vec3d playerPos = player.getPos();
                        Vec3d direction = blockCenter.subtract(playerPos).normalize();

                        double pullStrength = 0.03;
                        currentVel=currentVel.add(direction.multiply(pullStrength));
                    }
                }
            }
        }
        player.setVelocity(currentVel);
    }

    @Unique
    void tickSticky(PlayerEntity player) {
        if(player.isOnGround()) {
            Vec3d vel = player.getVelocity();
            if(Math.abs(vel.x) > 0.01 || Math.abs(vel.z) > 0.01) player.setVelocity(vel.x * 0.4, vel.y, vel.z * 0.4);
        }

        BlockPos pos = player.getBlockPos();
        boolean touchingWall = player.world.getBlockState(pos.north()).isFullCube(player.world, pos.north());
        if(!touchingWall) touchingWall = player.world.getBlockState(pos.south()).isFullCube(player.world, pos.south());
        if(!touchingWall) touchingWall = player.world.getBlockState(pos.west()).isFullCube(player.world, pos.west());
        if(!touchingWall) touchingWall = player.world.getBlockState(pos.east()).isFullCube(player.world, pos.east());

        if(touchingWall) {
            double slide=-0.1;
            if(player.isSneaking()) {
                player.setOnGround(true);
                slide=-0.05;
            }
            if(player.getVelocity().y < 0) player.setVelocity(player.getVelocity().x, slide, player.getVelocity().z);
        }
    }

    @Unique
    void tickScale(PlayerEntity player) {
        ScaleData scaleData = ScaleTypes.BASE.getScaleData(player);

        float targetScale = 1.0f;
        if (player.hasStatusEffect(ModEffects.DWARF)) targetScale = 0.5f;
        else if (player.hasStatusEffect(ModEffects.GIANT)) targetScale = 2.0f;

        float currentScale = scaleData.getScale();

        if (Math.abs(currentScale - targetScale) > 0.001f) {
            scaleData.setScale(targetScale);
            scaleData.markForSync(true);
        }
    }
}
