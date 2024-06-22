package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AxolotlEntity;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AxolotlEntity.class)
public class AxolotlEntityMixin
{
    @Dynamic
    @ModifyExpressionValue(method = "squaredAttackRange", at = @At(value = "CONSTANT", args = "doubleValue=1.5D"))
    private double squaredAttackRange$range(double value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? scale * scale * value : value;
    }
}