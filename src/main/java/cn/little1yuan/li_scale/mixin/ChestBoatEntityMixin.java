package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;

@Mixin(ChestBoatEntity.class)
public abstract class ChestBoatEntityMixin {
    @ModifyReturnValue(method = "canPlayerUse", at = @At("RETURN"))
    private boolean canPlayerUseMixin(boolean original, PlayerEntity playerEntity) {
        if (!original) {
            final float scale = LiScale.getScaleFactor(playerEntity);
            final ChestBoatEntity self = (ChestBoatEntity) (Object) this;
            if (scale > 1.0F && !self.isRemoved() && self.getPos().isInRange(playerEntity.getPos(), 8.0 * scale)) {
                return true;
            }
        }
        return original;
    }
}