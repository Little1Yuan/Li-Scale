package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SkeletonHorseEntity;

@Mixin(SkeletonHorseEntity.class)
public abstract class SkeletonHorseEntityMixin
{
    @Dynamic
    @ModifyReturnValue(method = "getMountedHeightOffset()D", at = @At("RETURN"))
    private double getMountedHeightOffsetMixin(double original) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);

        return scale != 1.0F ? (original + ((1.0F - scale) * 0.1875D)) : original;
    }
}