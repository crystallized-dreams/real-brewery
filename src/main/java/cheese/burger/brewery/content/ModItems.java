package cheese.burger.brewery.content;

import cheese.burger.brewery.RealBrewery;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item CHEESE=register("cheese", new Item(new Item.Settings().food(new FoodComponent.Builder().hunger(3).build())));

    public static void regAll() {}

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(RealBrewery.MOD_ID, id), item);
    }
}
