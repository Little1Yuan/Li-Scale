package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.LlamaEntity;

@Mixin(LlamaEntity.class)
public abstract class LlamaEntityMixin {
    @Dynamic
    @ModifyExpressionValue(method = "updatePassengerPosition", at = @At(value = "CONSTANT", args = "floatValue=0.3F"))
    private float updatePassengerPosition$offset(float value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? scale * value : value;
    }
}