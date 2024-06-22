package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

@Mixin(EntitySelector.class)
public class EntitySelectorMixin {
    @Dynamic
    @ModifyArg(method = { "method_9810", "func_197344_a", "m_121141_" }, require = 0, expect = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;intersects(Lnet/minecraft/util/math/Box;)Z"))
    private static Box method_9810$intersects(Box bounds, @Local(argsOnly = true) Entity obj) {
        final float scale = LiScale.getScaleFactor(obj);
        if (scale != 1.0F) {
            final double scaledXLength = bounds.getXLength() * 0.5D * (scale - 1.0F);
            final double scaledYLength = bounds.getYLength() * 0.5D * (scale - 1.0F);
            final double scaledZLength = bounds.getZLength() * 0.5D * (scale - 1.0F);
            return bounds.expand(scaledXLength, scaledYLength, scaledZLength);
        }
        return bounds;
    }
}