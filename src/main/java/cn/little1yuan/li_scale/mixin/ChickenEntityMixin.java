package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.ChickenEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChickenEntity.class)
public abstract class ChickenEntityMixin {
    @ModifyExpressionValue(method = "updatePassengerPosition", at = @At(value = "CONSTANT", args = "floatValue=0.1F"))
    private float updatePassengerPosition$offset(float value, Entity passenger) {
        final float scale = LiScale.getScaleFactor(passenger);
        return scale != 1.0F ? scale * value : value;
    }
}