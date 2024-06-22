package cn.little1yuan.li_scale.mixin.client;

import cn.little1yuan.li_scale.LiScale;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = GameRenderer.class, priority = 990)
public class GameRendererMixinReach {
    @Shadow @Final @Mutable
    MinecraftClient client;

    @ModifyExpressionValue(method = "updateTargetedEntity", require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=3.0D"))
    private double updateCrosshairTarget$distance(double value, float tickDelta) {
        final Entity entity = client.getCameraEntity();
        if (entity != null) {
            final float scale = LiScale.getScaleFactor(entity);
            if (scale != 1.0F) {
                return scale * value;
            }
        }
        return value;
    }

    @ModifyVariable(method = "updateTargetedEntity", require = 0, expect = 0, ordinal = 0, at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/entity/Entity;getCameraPosVec(F)Lnet/minecraft/util/math/Vec3d;"))
    private double updateCrosshairTarget$setDistance(double value, float tickDelta) {
        final Entity entity = client.getCameraEntity();
        if (entity != null) {
            final float scale = LiScale.getScaleFactor(entity);
            if (scale != 1.0F) {
                return scale * value;
            }
        }
        return value;
    }

    @ModifyVariable(method = "updateTargetedEntity", require = 0, expect = 0, ordinal = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getRotationVec(F)Lnet/minecraft/util/math/Vec3d;"))
    private double updateCrosshairTarget$squaredDistance(double value, float tickDelta) {
        final Entity entity = client.getCameraEntity();
        if (entity != null) {
            if (this.client.crosshairTarget == null || this.client.crosshairTarget.getType() == HitResult.Type.MISS) {
                final float scale = LiScale.getScaleFactor(entity);
                final double baseEntityReach = client.interactionManager.hasExtendedReach() ? 6.0D : client.interactionManager.getCurrentGameMode().isCreative() ? 5.0F : 4.5F;
                final double entityReach = scale * baseEntityReach;
                return entityReach * entityReach;
            }
        }
        return value;
    }

    @ModifyExpressionValue(method = "updateTargetedEntity", require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=6.0D"))
    private double updateCrosshairTarget$extendedDistance(double value, float tickDelta) {
        final Entity entity = client.getCameraEntity();
        if (entity != null) {
            final float scale = LiScale.getScaleFactor(entity);
            if (scale != 1.0F) {
                return scale * value;
            }
        }
        return value;
    }

    @ModifyExpressionValue(method = "updateTargetedEntity", require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=9.0D"))
    private double updateCrosshairTarget$squaredMaxDistance(double value, float tickDelta) {
        final Entity entity = client.getCameraEntity();
        if (entity != null) {
            final float scale = LiScale.getScaleFactor(entity);
            if (scale != 1.0F) {
                return scale * scale * value;
            }
        }
        return value;
    }
}