package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.EntityTrackingSection;

@Mixin(EntityTrackingSection.class)
public class EntityTrackingSectionMixin
{
    @WrapOperation(method = "forEach(Lnet/minecraft/util/math/Box;Lnet/minecraft/util/function/LazyIterationConsumer;)Lnet/minecraft/util/function/LazyIterationConsumer$NextIteration;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityLike;getBoundingBox()Lnet/minecraft/util/math/Box;"))
    private Box forEach$getBoundingBox(EntityLike obj, Operation<Box> original) {
        final Box bounds = original.call(obj);
        if (obj instanceof Entity entity) {
            final float interactionWidth = LiScale.getScaleFactor(entity);
            final float interactionHeight = LiScale.getScaleFactor(entity);
            if (interactionWidth != 1.0F || interactionHeight != 1.0F) {
                final double scaledXLength = bounds.getXLength() * 0.5D * (interactionWidth - 1.0F);
                final double scaledYLength = bounds.getYLength() * 0.5D * (interactionHeight - 1.0F);
                final double scaledZLength = bounds.getZLength() * 0.5D * (interactionWidth - 1.0F);
                return bounds.expand(scaledXLength, scaledYLength, scaledZLength);
            }
        }
        return bounds;
    }

    @WrapOperation(method = "forEach(Lnet/minecraft/util/TypeFilter;Lnet/minecraft/util/math/Box;Lnet/minecraft/util/function/LazyIterationConsumer;)Lnet/minecraft/util/function/LazyIterationConsumer$NextIteration;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityLike;getBoundingBox()Lnet/minecraft/util/math/Box;"))
    private Box forEach$getBoundingBox$filtered(EntityLike obj, Operation<Box> original) {
        final Box bounds = original.call(obj);
        if (obj instanceof Entity entity) {
            final float interactionWidth = LiScale.getScaleFactor(entity);
            final float interactionHeight = LiScale.getScaleFactor(entity);
            if (interactionWidth != 1.0F || interactionHeight != 1.0F) {
                final double scaledXLength = bounds.getXLength() * 0.5D * (interactionWidth - 1.0F);
                final double scaledYLength = bounds.getYLength() * 0.5D * (interactionHeight - 1.0F);
                final double scaledZLength = bounds.getZLength() * 0.5D * (interactionWidth - 1.0F);
                return bounds.expand(scaledXLength, scaledYLength, scaledZLength);
            }
        }
        return bounds;
    }
}