package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
//    @ModifyReturnValue(method = "getDimensions", at = @At("RETURN"))
//    public EntityDimensions getDimensionsMixin(EntityDimensions original) {
//        return original.scaled(LiScale.getScaleFactor((LivingEntity) (Object) this));
//    }

    @ModifyReturnValue(method = "getMaxHealth", at = @At("RETURN"))
    public float getMaxHealthMixin(float original) {
        return original * LiScale.getScaleFactor((LivingEntity) (Object) this);
    }

    @ModifyVariable(method = "applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F", at = @At("HEAD"), argsOnly = true)
    private float applyArmorToDamage(float value, DamageSource source, float amount)
    {
        final Entity attacker = source.getAttacker();
        final float attackScale = attacker == null ? 1.0F : LiScale.getScaleFactor(attacker);
        final float defenseScale = LiScale.getScaleFactor((Entity) (Object) this);

        if (attackScale != 1.0F || defenseScale != 1.0F)
        {
            value = attackScale * value / defenseScale;
        }

        return value;
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(DDD)V", shift = At.Shift.AFTER))
    private void tickMovement$minVelocity(CallbackInfo info, @Local Vec3d velocity) {
        final LivingEntity self = (LivingEntity) (Object) this;

        final float scale = LiScale.getScaleFactor(self);

        if (scale < 1.0F) {
            final double min = scale * 0.005D;

            double vX = velocity.x;
            double vY = velocity.y;
            double vZ = velocity.z;

            if (Math.abs(vX) < min) vX = 0.0D;
            if (Math.abs(vY) < min) vY = 0.0D;
            if (Math.abs(vZ) < min) vZ = 0.0D;

            self.setVelocity(vX, vY, vZ);
        }
    }

    @ModifyReturnValue(method = "isClimbing()Z", at = @At("RETURN"))
    private boolean isClimbingMixin(boolean original) {
        final LivingEntity self = (LivingEntity) (Object) this;

        if (original || self.isSpectator())
        {
            return original;
        }

        final float width = LiScale.getScaleFactor(self);

        if (width > 1.0F) {
            final Box bounds = self.getBoundingBox();

            final double halfUnscaledXLength = (bounds.getXLength() / width) / 2.0D;
            final int minX = MathHelper.floor(bounds.minX + halfUnscaledXLength);
            final int maxX = MathHelper.floor(bounds.maxX - halfUnscaledXLength);

            final int minY = MathHelper.floor(bounds.minY);

            final double halfUnscaledZLength = (bounds.getZLength() / width) / 2.0D;
            final int minZ = MathHelper.floor(bounds.minZ + halfUnscaledZLength);
            final int maxZ = MathHelper.floor(bounds.maxZ - halfUnscaledZLength);

            final World world = self.getEntityWorld();

            for (final BlockPos pos : BlockPos.iterate(minX, minY, minZ, maxX, minY, maxZ)) {
                if (world.getBlockState(pos).isIn(BlockTags.CLIMBABLE)) return true;
            }
        }

        return original;
    }

    @WrapOperation(method = "tickCramming", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getBoundingBox()Lnet/minecraft/util/math/Box;"))
    private Box tickCramming$getBoundingBox(LivingEntity obj, Operation<Box> original) {
        final Box bounds = original.call(obj);
        final float interactionScale = LiScale.getScaleFactor(obj);
        if (interactionScale != 1.0F) {
            final double scaledXLength = bounds.getXLength() * 0.5D * (interactionScale - 1.0F);
            final double scaledYLength = bounds.getYLength() * 0.5D * (interactionScale - 1.0F);
            final double scaledZLength = bounds.getZLength() * 0.5D * (interactionScale - 1.0F);
            return bounds.expand(scaledXLength, scaledYLength, scaledZLength);
        }
        return bounds;
    }

    @ModifyExpressionValue(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At(value = "CONSTANT", args = "doubleValue=0.4000000059604645D"))
    private double damage$knockback(double value, DamageSource source, float amount) {
        final float scale = LiScale.getScaleFactor(source.getAttacker());
        return scale != 1.0F ? scale * value : value;
    }

    @ModifyExpressionValue(method = "knockback(Lnet/minecraft/entity/LivingEntity;)V", at = @At(value = "CONSTANT", args = "doubleValue=0.5D"))
    private double knockback$knockback(double value, LivingEntity target) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? scale * value : value;
    }

    @ModifyArg(method = "updateLimbs(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateLimbs(F)V"))
    private float updateLimbsMixin(float value)
    {
        final float scale = LiScale.getScaleFactor((LivingEntity) (Object) this);
        return divideClamped(value, scale, 0x1p-96f, 0x1p32f);
    }

    @Unique
    private static float divideClamped(float dividend, float divisor, float minLimit, float maxLimit) {
        if (divisor == 1.0F) {
            return dividend;
        }

        final float ret = dividend / divisor;

        if (ret == Float.POSITIVE_INFINITY) {
            return maxLimit;
        } else if (ret == Float.NEGATIVE_INFINITY) {
            return -maxLimit;
        } else if (ret > minLimit || ret < -minLimit) {
            return ret;
        }

        return ret < 0 ? -minLimit : minLimit;
    }

    @Dynamic
    @ModifyArg(method = "getEyeHeight(Lnet/minecraft/entity/EntityPose;Lnet/minecraft/entity/EntityDimensions;)F", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getActiveEyeHeight(Lnet/minecraft/entity/EntityPose;Lnet/minecraft/entity/EntityDimensions;)F"))
    private EntityDimensions getEyeHeight$dimensions(EntityDimensions dimensions) {
        return dimensions.scaled(1.0F / LiScale.getScaleFactor((Entity) (Object) this));
    }

    @ModifyExpressionValue(method = "travel", at = @At(value = "CONSTANT", args = "floatValue=1.0F", ordinal = 0))
    private float travel$fallDistance(float value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        if (scale != 1.0F) {
            return value / scale;
        }
        return value;
    }

    @Dynamic
    @ModifyReturnValue(method = "getEyeHeight(Lnet/minecraft/entity/EntityPose;Lnet/minecraft/entity/EntityDimensions;)F", at = @At("RETURN"))
    private float getEyeHeightMixin(float original, EntityPose pose, EntityDimensions dimensions) {
        if (pose != EntityPose.SLEEPING) {
            final float scale = LiScale.getScaleFactor((Entity) (Object) this);
            if (scale != 1.0F) {
                return original * scale;
            }
        }
        return original;
    }

//    @ModifyReturnValue(method = "getJumpVelocity()F", at = @At("RETURN"))
//    private float getJumpVelocityMixin(float original) {
//        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
//        return scale != 1.0F ? original * scale : original;
//    }
}
