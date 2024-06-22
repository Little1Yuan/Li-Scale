package cn.little1yuan.li_scale.potion;

import cn.little1yuan.li_scale.LiScale;
import cn.little1yuan.li_scale.effect.StatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class Potions {
    public static final Potion BIGGER_POTION = new Potion(new StatusEffectInstance(StatusEffects.BIGGER_STATUS_EFFECT, 3600, 0));
    public static final Potion BIGGER_POTION_LONG = new Potion(new StatusEffectInstance(StatusEffects.BIGGER_STATUS_EFFECT, 8000, 0));
    public static final Potion BIGGER_POTION_AMP = new Potion(new StatusEffectInstance(StatusEffects.BIGGER_STATUS_EFFECT, 1800, 1));
    public static final Potion SMALLER_POTION = new Potion(new StatusEffectInstance(StatusEffects.SMALLER_STATUS_EFFECT, 3600, 0));
    public static final Potion SMALLER_POTION_LONG = new Potion(new StatusEffectInstance(StatusEffects.SMALLER_STATUS_EFFECT, 8000, 0));
    public static final Potion SMALLER_POTION_AMP = new Potion(new StatusEffectInstance(StatusEffects.SMALLER_STATUS_EFFECT, 1800, 0));
    public static final HashMap<String, Potion> POTIONS = new HashMap<>();
    public static void init() {
        // Add Items
        POTIONS.put("bigger", BIGGER_POTION);
        POTIONS.put("bigger_long", BIGGER_POTION_LONG);
        POTIONS.put("bigger_amp", BIGGER_POTION_AMP);
        POTIONS.put("smaller", SMALLER_POTION);
        POTIONS.put("smaller_long", SMALLER_POTION_LONG);
        POTIONS.put("smaller_amp", SMALLER_POTION_AMP);

        // Registry items
        POTIONS.forEach(Potions::registry);

        // Registry recipes
        BrewingRecipeRegistry.registerPotionRecipe(net.minecraft.potion.Potions.AWKWARD, Items.SLIME_BALL, SMALLER_POTION);
        BrewingRecipeRegistry.registerPotionRecipe(SMALLER_POTION, Items.REDSTONE, SMALLER_POTION_LONG);
        BrewingRecipeRegistry.registerPotionRecipe(SMALLER_POTION_AMP, Items.REDSTONE, SMALLER_POTION_LONG);
        BrewingRecipeRegistry.registerPotionRecipe(SMALLER_POTION, Items.GLOWSTONE_DUST, SMALLER_POTION_AMP);
        BrewingRecipeRegistry.registerPotionRecipe(SMALLER_POTION_LONG, Items.GLOWSTONE_DUST, SMALLER_POTION_AMP);
        BrewingRecipeRegistry.registerPotionRecipe(BIGGER_POTION, Items.REDSTONE, BIGGER_POTION_LONG);
        BrewingRecipeRegistry.registerPotionRecipe(BIGGER_POTION_AMP, Items.REDSTONE, BIGGER_POTION_LONG);
        BrewingRecipeRegistry.registerPotionRecipe(BIGGER_POTION, Items.GLOWSTONE_DUST, BIGGER_POTION_AMP);
        BrewingRecipeRegistry.registerPotionRecipe(BIGGER_POTION_LONG, Items.GLOWSTONE_DUST, BIGGER_POTION_AMP);
        BrewingRecipeRegistry.registerPotionRecipe(SMALLER_POTION, Items.FERMENTED_SPIDER_EYE, BIGGER_POTION);
        BrewingRecipeRegistry.registerPotionRecipe(SMALLER_POTION_AMP, Items.FERMENTED_SPIDER_EYE, BIGGER_POTION_AMP);
        BrewingRecipeRegistry.registerPotionRecipe(SMALLER_POTION_LONG, Items.FERMENTED_SPIDER_EYE, BIGGER_POTION_LONG);
        BrewingRecipeRegistry.registerPotionRecipe(BIGGER_POTION, Items.FERMENTED_SPIDER_EYE, SMALLER_POTION);
        BrewingRecipeRegistry.registerPotionRecipe(BIGGER_POTION_AMP, Items.FERMENTED_SPIDER_EYE, SMALLER_POTION_AMP);
        BrewingRecipeRegistry.registerPotionRecipe(BIGGER_POTION_LONG, Items.FERMENTED_SPIDER_EYE, SMALLER_POTION_LONG);
    }
    private static void registry(String itemId, Potion item) {
        Registry.register(Registries.POTION, new Identifier(LiScale.MOD_ID, itemId), item);
    }
}
