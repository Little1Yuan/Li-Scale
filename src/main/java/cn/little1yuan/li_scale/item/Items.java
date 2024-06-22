package cn.little1yuan.li_scale.item;

import cn.little1yuan.li_scale.LiScale;
import cn.little1yuan.li_scale.effect.BiggerStatusEffect;
import cn.little1yuan.li_scale.effect.StatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class Items {
    public static final Potion BIGGER_POTION = new Potion(new StatusEffectInstance(StatusEffects.BIGGER_STATUS_EFFECT, 3600, 0));
    public static final HashMap<String, Item> ITEMS = new HashMap<>();
    public static void init() {
        // Add Items

        // Registry items
        ITEMS.forEach(Items::registry);
    }
    private static void registry(String itemId, Item item) {
        Registry.register(Registries.ITEM, new Identifier(LiScale.MOD_ID, itemId), item);
    }
}
