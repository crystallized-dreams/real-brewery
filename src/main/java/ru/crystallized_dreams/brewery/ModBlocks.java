package ru.crystallized_dreams.brewery;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final Block CHEESE_BLOCK=register("cheese_block", new Block(AbstractBlock.Settings.of(Material.SPONGE, MapColor.YELLOW)
            .strength(0.6F).sounds(BlockSoundGroup.WET_GRASS)));

    public static void regAll() {}

    private static Block register(String id, Block block) {
        registerBlockItem(id,new BlockItem(block, new Item.Settings().group(ItemGroup.FOOD)));
        return Registry.register(Registry.BLOCK, new Identifier(RealBrewery.MOD_ID, id), block);
    }
    private static void registerBlockItem(String id, Item item) {
        Registry.register(Registry.ITEM, new Identifier(RealBrewery.MOD_ID, id), item);
    }
}
