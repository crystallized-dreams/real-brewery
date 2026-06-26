package ru.crystallized_dreams.brewery.content;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.Tag;
import ru.crystallized_dreams.brewery.RealBrewery;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item CHEESE=register("cheese", new Item(new Item.Settings().food(new FoodComponent.Builder().hunger(3).build()).group(ItemGroup.FOOD)));

    public static final Tag<Item> BREW_INGREDIENTS= TagRegistry.item(
            new Identifier(RealBrewery.MOD_ID, "brew_ingredients")
    );

    public static void regAll() {}

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(RealBrewery.MOD_ID, id), item);
    }
}
