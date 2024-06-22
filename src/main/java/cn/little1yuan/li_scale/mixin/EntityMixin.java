package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @ModifyExpressionValue(method = "updateSupportingBlockPos", at = @At(value = "CONSTANT", args = "doubleValue=1.0E-6"))
    private double isInsideWallOffsetMixin(double value) {
        if ((Entity) (Object) this instanceof LivingEntity) {
            final float scale = LiScale.getScaleFactor((LivingEntity) (Object) this);
            return scale < 1.0F ? value * scale : value;
        }
        return value;
    }

    @ModifyArg(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V"))
    private float fall$fallDistance(float distance) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? distance / scale : distance;
    }

    @ModifyExpressionValue(method = "move", at = @At(value = "CONSTANT", ordinal = 0, args = "doubleValue=0.6D"))
    private double move$flapping(double value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? value / scale : value;
    }

    @ModifyExpressionValue(method = "move", at = @At(value = "CONSTANT", ordinal = 0, args = "floatValue=0.6F"))
    private float move$bobbing(float value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? value / scale : value;
    }

    @ModifyExpressionValue(method = "move", at = @At(value = "CONSTANT", ordinal = 1, args = "floatValue=0.6F"))
    private float move$step(float value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? value * scale : value;
    }

    @ModifyExpressionValue(method = "updateSubmergedInWaterState()V", at = @At(value = "CONSTANT", args = "doubleValue=0.1111111119389534D"))
    private double updateSubmergedInWaterState$offset(double value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? value * scale : value;
    }

    @ModifyExpressionValue(method = "updateSupportingBlockPos", at = @At(value = "CONSTANT", args = "doubleValue=1.0E-6"))
    private double updateSupportingBlockPos$offset(double value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale < 1.0F ? value * scale : value;
    }

    @ModifyReturnValue(method = "getDimensions", at = @At("RETURN"))
    private EntityDimensions getDimensionsMixin(EntityDimensions original) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        if (scale != 1.0F) {
            return original.scaled(scale, scale);
        }
        return original;
    }

    @ModifyExpressionValue(method = "move", at = @At(value = "CONSTANT", args = "doubleValue=1.0E-7D"))
    private double move$minVelocity(double value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale < 1.0F ? scale * value : value;
    }

    @ModifyArg(method = "move", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;adjustMovementForSneaking(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/MovementType;)Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d move$adjustMovementForSneaking(Vec3d movement, MovementType type) {
        return type == MovementType.SELF || type == MovementType.PLAYER ? movement.multiply(LiScale.getScaleFactor((Entity) (Object) this)) : movement;
    }

    @WrapOperation(method = "pushAwayFrom", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    private void pushSelfAwayFrom$other(Entity obj, double x, double y, double z, Operation<Void> original, @Local(argsOnly = true) Entity other) {
        final float otherScale = LiScale.getScaleFactor(other);

        if (otherScale != 1.0F) {
            x *= otherScale;
            z *= otherScale;
        }

        original.call(obj, x, y, z);
    }

    @WrapOperation(method = "pushAwayFrom", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    private void pushSelfAwayFrom$self(Entity obj, double x, double y, double z, Operation<Void> original) {
        final float ownScale = LiScale.getScaleFactor((Entity) (Object) this);

        if (ownScale != 1.0F) {
            x *= ownScale;
            z *= ownScale;
        }

        original.call(obj, x, y, z);
    }

    @Inject(at = @At("HEAD"), method = "spawnSprintingParticles", cancellable = true)
    private void spawnSprintingParticle0sMixin(CallbackInfo info) {
        if (LiScale.getScaleFactor((Entity) (Object) this) < 1.0F)
            info.cancel();
    }
}
