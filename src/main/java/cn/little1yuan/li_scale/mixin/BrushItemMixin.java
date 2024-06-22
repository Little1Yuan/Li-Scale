package cn.little1yuan.li_scale.mixin;

import java.util.function.Predicate;

import cn.little1yuan.li_scale.LiScale;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import net.minecraft.item.BrushItem;
import net.minecraft.util.hit.HitResult;

@Mixin(BrushItem.class)
public class BrushItemMixin {
    @Dynamic
    @WrapOperation(method = "getHitResult(Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/util/hit/HitResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getCollision(Lnet/minecraft/entity/Entity;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/HitResult;"))
    private HitResult getHitResult$getCollision(Entity entity, Predicate<Entity> predicate, double range, Operation<HitResult> original) {
        final float scale = LiScale.getScaleFactor(entity);
        return original.call(entity, predicate, scale != 1.0F ? range * scale : range);
    }
}