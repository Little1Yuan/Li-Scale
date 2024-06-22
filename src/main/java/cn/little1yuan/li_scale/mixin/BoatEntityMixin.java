package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin {
    @Dynamic
    @ModifyExpressionValue(method = "updatePassengerPosition", at = @At(value = "CONSTANT", args = "floatValue=0.2F"))
    private float updatePassengerPosition$frontOffset(float value, Entity passenger) {
        final float scale = LiScale.getScaleFactor(passenger);
        return scale != 1.0F ? scale * value : value;
    }

    @Dynamic
    @ModifyExpressionValue(method = "updatePassengerPosition", at = @At(value = "CONSTANT", args = "floatValue=-0.6F"))
    private float updatePassengerPosition$backOffset(float value, Entity passenger) {
        final float scale = LiScale.getScaleFactor(passenger);
        return scale != 1.0F ? scale * value : value;
    }

    @ModifyExpressionValue(method = "updateVelocity", at = @At(value = "CONSTANT", args = "doubleValue=0.06153846016296973D"))
    private double pehkui$updateVelocity$multiplier(double value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? scale * value : value;
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
    private Box pehkui$tick$expand(Box obj, double x, double y, double z, Operation<Box> original) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);

        if (scale != 1.0F) {
            x *= scale;
            z *= scale;
        }

        if (scale != 1.0F) {
            y *= scale;
        }

        return original.call(obj, x, y, z);
    }

    @ModifyArg(method = "checkBoatInWater", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/util/math/MathHelper;ceil(D)I"))
    private double checkBoatInWater$offset(double value) {
        final Entity self = (Entity) (Object) this;
        final float scale = LiScale.getScaleFactor(self);

        if (scale != 1.0F) {
            final double minY = self.getBoundingBox().minY;
            return minY + (scale * (value - minY));
        }

        return value;
    }

    @ModifyVariable(method = "getUnderWaterLocation", at = @At(value = "STORE"))
    private double getUnderWaterLocation$offset(double value)
    {
        final Entity self = (Entity) (Object) this;
        final float scale = LiScale.getScaleFactor(self);
        if (scale > 1.0F) {
            final double maxY = self.getBoundingBox().maxY;
            return maxY + (scale * (value - maxY));
        }

        return value;
    }

    @ModifyExpressionValue(method = "updateVelocity", at = @At(value = "CONSTANT", args = "doubleValue=-7.0E-4D"))
    private double updateVelocity$sinking(double value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? scale * value : value;
    }
}