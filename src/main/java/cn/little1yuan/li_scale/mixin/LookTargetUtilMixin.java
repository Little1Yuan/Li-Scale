package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LookTargetUtil.class)
public class LookTargetUtilMixin {
    @ModifyVariable(method = "give(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;F)V", at = @At("HEAD"), argsOnly = true)
    private static float give$offset(float value, LivingEntity entity, ItemStack stack, Vec3d targetLocation, Vec3d velocityFactor, float yOffset) {
        final float scale = LiScale.getScaleFactor(entity);
        return scale != 1.0F ? scale * value : value;
    }
}