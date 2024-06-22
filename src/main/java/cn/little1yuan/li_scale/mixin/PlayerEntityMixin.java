package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin
{
    @ModifyExpressionValue(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "CONSTANT", args = "doubleValue=0.4000000059604645D"))
    private double attack$knockback(double value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? scale * value : value;
    }

    @ModifyReturnValue(method = "getOffGroundSpeed", at = @At(value = "RETURN", ordinal = 0))
    private float getOffGroundSpeedMixin(float original) {
        final float scale = LiScale.getScaleFactor((PlayerEntity) (Object) this);
        return scale != 1.0F ? original * scale : original;
    }

    @Dynamic
    @ModifyReturnValue(method = "getDimensions(Lnet/minecraft/entity/EntityPose;)Lnet/minecraft/entity/EntityDimensions;", at = @At("RETURN"))
    private EntityDimensions getDimensionsMixin(EntityDimensions original) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? original.scaled(scale, scale) : original;
    }

    @Inject(at = @At("RETURN"), method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;")
    private void dropItemMixin(ItemStack stack, boolean spread, boolean thrown, CallbackInfoReturnable<ItemEntity> info) {
        final ItemEntity entity = info.getReturnValue();
        if (entity != null) {
            final float scale = LiScale.getScaleFactor((Entity) (Object) this);
            if (scale != 1.0F) {
                final Vec3d pos = entity.getPos();
                entity.setPosition(pos.x, pos.y + ((1.0F - scale) * 0.3D), pos.z);
            }
        }
    }

    @WrapOperation(method = "tickMovement()V", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
    private Box tickMovement$expand(Box obj, double x, double y, double z, Operation<Box> original) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        if (scale != 1.0F) {
            x *= scale;
            z *= scale;
            //y *= scale;
        }
        return original.call(obj, x, y, z);
    }

    @ModifyExpressionValue(method = "attack(Lnet/minecraft/entity/Entity;)V", at = { @At(value = "CONSTANT", args = "floatValue=0.5F", ordinal = 1), @At(value = "CONSTANT", args = "floatValue=0.5F", ordinal = 2), @At(value = "CONSTANT", args = "floatValue=0.5F", ordinal = 3) })
    private float attack$knockbackMixin(float value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? scale * value : value;
    }

    @ModifyExpressionValue(method = "getAttackCooldownProgressPerTick", at = @At(value = "CONSTANT", args = "doubleValue=20.0D"))
    private double getAttackCooldownProgressPerTick$multiplier(double value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? value / scale : value;
    }

    @ModifyExpressionValue(method = "updateCapeAngles", at = { @At(value = "CONSTANT", args = "doubleValue=10.0D"), @At(value = "CONSTANT", args = "doubleValue=-10.0D") })
    private double updateCapeAngles$limits(double value) {
        final float scale = LiScale.getScaleFactor((Entity) (Object) this);
        return scale != 1.0F ? scale * value : value;
    }
}