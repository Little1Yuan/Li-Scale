package cn.little1yuan.li_scale.mixin.client;

import cn.little1yuan.li_scale.LiScale;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin
{
    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    @ModifyArg(method = "renderStatusBars", index = 0, at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F"))
    private float renderStatusBarsMixin(float value)
    {
        final float healthScale = LiScale.getScaleFactor(getCameraPlayer());

        return healthScale != 1.0F ? value * healthScale : value;
    }
}