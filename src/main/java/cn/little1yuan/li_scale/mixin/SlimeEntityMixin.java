package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SlimeEntity;

@Mixin(SlimeEntity.class)
public class SlimeEntityMixin {
    @ModifyExpressionValue(method = "remove(Lnet/minecraft/entity/Entity$RemovalReason;)V", at = @At(value = "CONSTANT", args = "doubleValue=0.5D"))
    private double remove$verticalOffset(double value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? value * scale : value;
    }

    @ModifyExpressionValue(method = "remove(Lnet/minecraft/entity/Entity$RemovalReason;)V", at = @At(value = "CONSTANT", args = "floatValue=4.0F"))
    private float remove$horizontalOffset(float value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? value / scale : value;
    }
}