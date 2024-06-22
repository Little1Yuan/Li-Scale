package cn.little1yuan.li_scale.mixin.client;

import cn.little1yuan.li_scale.LiScale;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @ModifyReturnValue(method = "getPositionOffset", at = @At("RETURN"))
    private Vec3d getPositionOffsetMixin(Vec3d original, AbstractClientPlayerEntity entity) {
        return original != Vec3d.ZERO ? original.multiply(LiScale.getScaleFactor(entity)) : original;
    }
}