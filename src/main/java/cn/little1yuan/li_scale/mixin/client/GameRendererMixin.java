package cn.little1yuan.li_scale.mixin.client;

import cn.little1yuan.li_scale.LiScale;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final @Mutable
    MinecraftClient client;

    @ModifyExpressionValue(method = "getBasicProjectionMatrix(D)Lorg/joml/Matrix4f;", at = @At(value = "CONSTANT", args = "floatValue=0.05F"))
    private float getBasicProjectionMatrix$depth(float value) {
        float scale = Math.min(LiScale.getScaleFactor( client.getCameraEntity()), LiScale.getScaleFactor( client.getCameraEntity()));
        float depth = value;
        Entity entity = client.getCameraEntity();
        float tickDelta = client.getTickDelta();
        if (client.getCameraEntity() == null) return value;
        float mpmd;
        if (scale < 1.0F)
        {
            mpmd = Math.max(depth * scale, (float) 1 / 32767);
        } else {
            mpmd = depth;
        }
        return mpmd;
    }

    @WrapOperation(method = "bobView", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"))
    private void bobView$translate(MatrixStack obj, float x, float y, float z, Operation<Void> original)
    {
        if (true)
        {
            final float scale = LiScale.getScaleFactor(client.getCameraEntity());

            if (scale != 1.0F)
            {
                x *= scale;
                y *= scale;
                z *= scale;
            }
        }

        original.call(obj, x, y, z);
    }
}