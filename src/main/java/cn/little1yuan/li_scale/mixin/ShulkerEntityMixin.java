package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

@Mixin(ShulkerEntity.class)
public class ShulkerEntityMixin
{
    @ModifyReturnValue(method = "calculateBoundingBox", at = @At("RETURN"))
    private Box pehkui$calculateBoundingBox(Box box) {
        final ShulkerEntity entity = (ShulkerEntity) (Object) this;

        final float widthScale = LiScale.getScaleFactor(entity);
        final float heightScale = LiScale.getScaleFactor(entity);

        if (widthScale != 1.0F || heightScale != 1.0F) {
            final Direction facing = entity.getAttachedFace().getOpposite();

            final double xLength = box.getXLength() / -2.0D;
            final double yLength = box.getYLength() / -2.0D;
            final double zLength = box.getZLength() / -2.0D;

            final double dX = xLength * (1.0D - widthScale);
            final double dY = yLength * (1.0D - heightScale);
            final double dZ = zLength * (1.0D - widthScale);
            box = box.expand(dX, dY, dZ);
            box = box.offset(dX * facing.getOffsetX(), dY * facing.getOffsetY(), dZ * facing.getOffsetZ());
        }

        return box;
    }

    @Dynamic
    @ModifyReturnValue(method = "getActiveEyeHeight(Lnet/minecraft/entity/EntityPose;Lnet/minecraft/entity/EntityDimensions;)F", at = @At("RETURN"))
    private float pehkui$getActiveEyeHeight(float original, EntityPose pose, EntityDimensions dimensions)
    {
        final ShulkerEntity entity = (ShulkerEntity) (Object) this;

        final Direction face = entity.getAttachedFace();
        if (face != Direction.DOWN)
        {
            final float scale = LiScale.getScaleFactor(entity);
            if (scale != 1.0F)
            {
                if (face == Direction.UP)
                {
                    return divideClamped(1.0F, scale,0x1P-96F,0x1P32F) - original;
                }
                else
                {
                    return divideClamped(1.0F - original, scale, 0x1P-96F,0x1P32F);
                }
            }
        }

        return original;
    }

    @Unique
    private static float divideClamped(float dividend, float divisor, float minLimit, float maxLimit) {
        if (divisor == 1.0F) {
            return dividend;
        }

        final float ret = dividend / divisor;

        if (ret == Float.POSITIVE_INFINITY) {
            return maxLimit;
        } else if (ret == Float.NEGATIVE_INFINITY) {
            return -maxLimit;
        } else if (ret > minLimit || ret < -minLimit) {
            return ret;
        }

        return ret < 0 ? -minLimit : minLimit;
    }
}
