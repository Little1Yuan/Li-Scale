package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin({
        ArmorStandEntity.class,
        AbstractSkeletonEntity.class,
        EndermiteEntity.class,
        PatrolEntity.class,
        SilverfishEntity.class,
        ZombieEntity.class,
        AnimalEntity.class,
        PlayerEntity.class
})
public abstract class EntityVehicleHeightOffsetMixin {
    @Dynamic
    @ModifyReturnValue(method = "getMountedHeightOffset()D", at = @At("RETURN"))
    private double getHeightOffsetMixin(double offset) {
        final Entity self = (Entity) (Object) this;
        final Entity vehicle = self.getVehicle();
        if (vehicle != null) {
            final float scale = LiScale.getScaleFactor(self);
            final float vehicleScale = LiScale.getScaleFactor(vehicle);

            if (scale != 1.0F || vehicleScale != 1.0F) {
                final double vehicleScaledHeight = vehicle.getHeight();
                final double vehicleHeight = vehicleScaledHeight / vehicleScale;
                final double scaledMountedOffset = vehicle.getMountedHeightOffset();
                final double mountedOffset = scaledMountedOffset / vehicleScale;
                final double scaledOffset = offset * scale;

                final double bottom = vehicleHeight - mountedOffset - offset;
                final double down = vehicleScaledHeight - scaledMountedOffset - (bottom * scale);

                return (Math.max(down, scaledOffset));
            }
        }

        return offset;
    }
}