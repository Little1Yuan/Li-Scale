package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.mob.ZoglinEntity;

@Mixin(ZoglinEntity.class)
public class ZoglinEntityMixin {
    @Dynamic
    @ModifyReturnValue(method = "getMountedHeightOffset()D", at = @At("RETURN"))
    private double getMountedHeightOffsetMixin(double original) {
        final ZoglinEntity self = (ZoglinEntity) (Object) this;
        final float scale = LiScale.getScaleFactor(self);
        return scale != 1.0F ? (original + ((1.0F - scale) * (self.isBaby() ? 0.2D : 0.15D))) : original;
    }
}