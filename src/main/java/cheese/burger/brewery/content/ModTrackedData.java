package cheese.burger.brewery.content;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;

public class ModTrackedData {
    public static final TrackedData<Float> BOUNCE_STRENGTH=DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public static final TrackedData<Integer> GOAT_DASH_COOLDOWN=DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Integer> LAVA_BREATH=DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Integer> LAVA_BREATH_DAMAGE_COOL=DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public static final Integer GOAT_DASH_COOLDOWN_MAX = 20*5;

    public static void regAll() {}
}
