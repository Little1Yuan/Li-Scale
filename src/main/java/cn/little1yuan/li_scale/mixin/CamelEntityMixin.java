package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.CamelEntity;

@Mixin(CamelEntity.class)
public class CamelEntityMixin {
    @Dynamic
    @ModifyExpressionValue(method = "getMountedHeightOffset()D", at = @At(value = "CONSTANT", args = "floatValue=0.6F"))
    private float getMountedHeightOffset$adultOffset(float value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? scale * value : value;
    }

    @Dynamic
    @ModifyExpressionValue(method = "getMountedHeightOffset()D", at = @At(value = "CONSTANT", args = "floatValue=0.35F"))
    private float getMountedHeightOffset$babyOffset(float value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? scale * value : value;
    }

    @Dynamic
    @ModifyExpressionValue(method = "updatePassengerPosition", at = @At(value = "CONSTANT", args = "floatValue=0.5F"))
    private float updatePassengerPosition$frontOffset(float value, Entity passenger) {
        final float scale = LiScale.getScaleFactor(passenger);
        return scale != 1.0F ? scale * value : value;
    }

    @Dynamic
    @ModifyExpressionValue(method = "updatePassengerPosition", at = @At(value = "CONSTANT", args = "floatValue=-0.7F"))
    private float updatePassengerPosition$backOffset(float value, Entity passenger) {
        final float scale = LiScale.getScaleFactor(passenger);
        return scale != 1.0F ? scale * value : value;
    }

    @Dynamic
    @ModifyExpressionValue(method = "updatePassengerPosition", at = @At(value = "CONSTANT", args = "floatValue=0.2F"))
    private float updatePassengerPosition$animalOffset(float value, Entity passenger) {
        final float scale = LiScale.getScaleFactor(passenger);
        return scale != 1.0F ? scale * value : value;
    }

    @Dynamic
    @ModifyReturnValue(method = "getDimensions(Lnet/minecraft/entity/EntityPose;)Lnet/minecraft/entity/EntityDimensions;", at = @At("RETURN"))
    private EntityDimensions pehkui$getDimensions(EntityDimensions original, EntityPose pose)
    {
        if (pose == EntityPose.SITTING)
        {
            original = original.scaled(LiScale.getScaleFactor((Entity) (Object) this), LiScale.getScaleFactor((Entity) (Object) this));
        }

        return original;
    }
}