package cn.little1yuan.li_scale.mixin.client;

import cn.little1yuan.li_scale.LiScale;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerMixin {
    @ModifyExpressionValue(method = "sendMovementPackets", at = @At(value = "CONSTANT", args = "doubleValue=2.0E-4D"))
    private double sendMovementPackets$minVelocity(double value) {
        final float scale = LiScale.getScaleFactor((LivingEntity) (Object) this);
        return scale < 1.0F ? scale * value : value;
    }

//    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "CONSTANT", args = "floatValue=3.0F"))
//    private float tickMovement$flightSpeed(float value) {
//        final float scale = LiScale.getScaleFactor((LivingEntity) (Object) this);
//        return scale != 1.0F ? scale * value : value;
//    }

    @ModifyExpressionValue(method = "autoJump", at = { @At(value = "CONSTANT", args = "floatValue=1.2F"), @At(value = "CONSTANT", args = "floatValue=0.75F") })
    private float autoJump$heightAndBoost(float value) {
        final float jumpScale = LiScale.getScaleFactor((LivingEntity) (Object) this);
        return jumpScale != 1.0F ? value * jumpScale : value;
    }
}
