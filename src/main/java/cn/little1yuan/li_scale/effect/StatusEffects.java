package cn.little1yuan.li_scale.effect;

import cn.little1yuan.li_scale.LiScale;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class StatusEffects {
    public static final HashMap<String, StatusEffect> EFFECTS = new HashMap<>();
    public static final BiggerStatusEffect BIGGER_STATUS_EFFECT = new BiggerStatusEffect();
    public static final SmallerStatusEffect SMALLER_STATUS_EFFECT = new SmallerStatusEffect();
    public static void init() {
        // Add effects
        EFFECTS.put("bigger", BIGGER_STATUS_EFFECT);
        EFFECTS.put("smaller", SMALLER_STATUS_EFFECT);

        // Registry items
        EFFECTS.forEach(StatusEffects::registry);
    }
    private static void registry(String effectId, StatusEffect effect) {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(LiScale.MOD_ID, effectId), effect);
    }
}
