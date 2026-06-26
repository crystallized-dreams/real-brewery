package ru.crystallized_dreams.brewery.system;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.commons.lang3.RandomUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerPotionRecipeData {
    public static PlayerPotionRecipeData INSTANCE = new PlayerPotionRecipeData();

    private final Map<String, String> recipes = new HashMap<>();
    private final List<Potion> unusedPotions = new ArrayList<>();
    private final List<Potion> globalPotionPool = new ArrayList<>();

    public void addAvailablePotion(Potion potion) {
        if (!globalPotionPool.contains(potion)) {
            globalPotionPool.add(potion);
        }
    }

    public ItemStack get(ItemStack input, ItemStack ingredient, World world, boolean heated, boolean ender) {
        if(ingredient.isEmpty() || ingredient.getItem() == net.minecraft.item.Items.AIR) return input;

        if(unusedPotions.isEmpty()) unusedPotions.addAll(globalPotionPool);

        ItemStack realInput = input.copy();
        realInput.setCount(1);
        ItemStack realIngredient = ingredient.copy();
        realIngredient.setCount(1);

        String inputItemId = "minecraft:potion";
        Potion basePotion = PotionUtil.getPotion(realInput);
        String potionType = Registry.POTION.getId(basePotion).toString();

        String ingredientId = Registry.ITEM.getId(realIngredient.getItem()).toString();
        String key = inputItemId + "|" + potionType + "|" + ingredientId;

        Potion potion;
        if (recipes.containsKey(key)) {
            String potionId = recipes.get(key);
            potion = Registry.POTION.get(new Identifier(potionId));
        } else {
            potion = getRandomPotion();
            if (potion == null) return input;

            String potionIdentifier = Registry.POTION.getId(potion).toString();
            recipes.put(key, potionIdentifier);

            saveData(world);
        }
        ItemStack result = input.copy();
        if(heated) result = Items.SPLASH_POTION.getDefaultStack();
        if(ender) result = Items.LINGERING_POTION.getDefaultStack();
        PotionUtil.setPotion(result, potion);
        return result;
    }

    private Potion getRandomPotion() {
        if (unusedPotions.isEmpty()) return null;
        int id = RandomUtils.nextInt(0, unusedPotions.size());
        Potion potion = unusedPotions.get(id);
        if(RandomUtils.nextInt(0, 3)==0) unusedPotions.remove(id);
        return potion;
    }

    public void loadData(World world) {
        try {
            File file = getDataFile(world);
            if (!file.exists()) {
                recipes.clear();
                unusedPotions.clear();
                unusedPotions.addAll(globalPotionPool);
                return;
            }

            NbtCompound tag = NbtIo.read(file);
            if (tag == null) return;

            recipes.clear();
            NbtList recipeList = tag.getList("recipes", 10);
            for (int i = 0; i < recipeList.size(); i++) {
                NbtCompound compound = recipeList.getCompound(i);
                recipes.put(compound.getString("key"), compound.getString("potion"));
            }

            unusedPotions.clear();
            NbtList unusedList = tag.getList("unused", 8);
            for (int i = 0; i < unusedList.size(); i++) {
                String idStr = (unusedList.get(i)).asString();
                Potion p = Registry.POTION.get(new Identifier(idStr));
                if (p != null) unusedPotions.add(p);
            }

            if (unusedPotions.isEmpty()) {
                unusedPotions.addAll(globalPotionPool);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveData(World world) {
        try {
            File file = getDataFile(world);
            File parent = file.getParentFile();
            if (!parent.exists()) parent.mkdirs();

            NbtCompound tag = new NbtCompound();

            NbtList recipeList = new NbtList();
            for (Map.Entry<String, String> entry : recipes.entrySet()) {
                NbtCompound compound = new NbtCompound();
                compound.putString("key", entry.getKey());
                compound.putString("potion", entry.getValue());
                recipeList.add(compound);
            }
            tag.put("recipes", recipeList);

            NbtList unusedList = new NbtList();
            for (Potion p : unusedPotions) {
                unusedList.add(NbtString.of(Registry.POTION.getId(p).toString()));
            }
            tag.put("unused", unusedList);

            NbtIo.write(tag, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDataFile(World world) {
        if (world.getServer() != null) {
            String levelName = world.getServer().getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString();
            File savesDir = new File(System.getProperty("user.dir"), "saves");
            File worldDir = new File(savesDir, levelName);
            return new File(worldDir, "data/realbrewery_recipes.dat");
        }
        return new File("config/realbrewery_recipes.dat");
    }

    public void clear() {
        recipes.clear();
        unusedPotions.clear();
        unusedPotions.addAll(globalPotionPool);
    }
}