package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(value = PlayerEntity.class, priority = 990)
public abstract class PlayerEntityMixinReach {
    @ModifyExpressionValue(method = "attack", require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=9.0F"))
    private double attack$distance(double value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale > 1.0F ? scale * scale * value : value;
    }
}