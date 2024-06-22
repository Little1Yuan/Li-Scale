package cn.little1yuan.li_scale.effect;

import cn.little1yuan.li_scale.LiScale;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SmallerStatusEffect extends StatusEffect {
    protected SmallerStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xc5d8db);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        entity.removeStatusEffect(StatusEffects.BIGGER_STATUS_EFFECT);
        LiScale.updateServerScaleFactor(entity, -amplifier-1);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        entity.setHealth(Math.min(entity.getHealth(), entity.getMaxHealth()));
        LiScale.updateServerScaleFactor(entity, 0);
    }
}
