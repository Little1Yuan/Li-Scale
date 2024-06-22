package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.EntityTrackerEntry;

@Mixin(EntityTrackerEntry.class)
public abstract class EntityTrackerEntryMixin {
    @Shadow @Final
    private Entity entity;

    @ModifyExpressionValue(method = "tick", at = @At(value = "CONSTANT", args = "doubleValue=7.62939453125E-6D"))
    private double tick$minimumSquaredDistance(double value) {
        final float scale = LiScale.getScaleFactor(entity);
        return scale < 1.0F ? value * scale * scale : value;
    }
}