package cn.little1yuan.li_scale.mixin.client;

import cn.little1yuan.li_scale.LiScale;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Camera.class)
public abstract class CameraMixin
{
    @Shadow
    private Entity focusedEntity;

    @ModifyVariable(method = "clipToSpace", at = @At(value = "HEAD"), argsOnly = true)
    private double clipToSpaceMixin(double desiredCameraDistance) {
        return desiredCameraDistance * LiScale.getScaleFactor(focusedEntity);
    }

    @ModifyExpressionValue(method = "clipToSpace", at = @At(value = "CONSTANT", args = "floatValue=0.1F"))
    private float clipToSpace$offset(float value) {
        final float scale = LiScale.getScaleFactor(focusedEntity);
        return scale < 1.0F ? scale * value : value;
    }
}