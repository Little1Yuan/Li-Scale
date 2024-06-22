package cn.little1yuan.li_scale.effect;

import cn.little1yuan.li_scale.LiScale;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.MathHelper;

public class BiggerStatusEffect extends StatusEffect {
    protected BiggerStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x535b5c);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        entity.removeStatusEffect(StatusEffects.SMALLER_STATUS_EFFECT);
        LiScale.updateServerScaleFactor(entity, amplifier+1);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        entity.setHealth(Math.min(entity.getHealth(), entity.getMaxHealth()));
        LiScale.updateServerScaleFactor(entity, 0);
    }
}
