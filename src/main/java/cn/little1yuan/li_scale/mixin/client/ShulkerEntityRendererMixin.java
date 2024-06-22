package cn.little1yuan.li_scale.mixin.client;

import cn.little1yuan.li_scale.LiScale;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.ShulkerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.Direction;

@Mixin(ShulkerEntityRenderer.class)
public class ShulkerEntityRendererMixin
{
    @Dynamic
    @Inject(at = @At("RETURN"), method = "setupTransforms")
    private void setupTransformsMixin(ShulkerEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, CallbackInfo info)
    {
        final Direction face = entity.getAttachedFace();

        if (face != Direction.DOWN)
        {
            final float h = LiScale.getScaleFactor(entity);
            if (face != Direction.UP)
            {
                final float w = LiScale.getScaleFactor(entity);
                if (w != 1.0F || h != 1.0F)
                {
                    matrices.translate(0.0, -((1.0F - w) * 0.5F) / w, -((1.0F - h) * 0.5F) / h);
                }
            }
            else if (h != 1.0F)
            {
                matrices.translate(0.0, -(1.0F - h) / h, 0.0);
            }
        }
    }
}