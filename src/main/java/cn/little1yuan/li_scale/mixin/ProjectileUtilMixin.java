package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin
{
    @WrapOperation(method = "getEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;F)Lnet/minecraft/util/hit/EntityHitResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getBoundingBox()Lnet/minecraft/util/math/Box;"))
    private static Box getEntityCollision$getBoundingBox(Entity obj, Operation<Box> original, World world, Entity except, Vec3d vec3d, Vec3d vec3d2, Box box, Predicate<Entity> predicate, float value) {
        final Box bounds = original.call(obj);

        final float width = LiScale.getScaleFactor(obj);
        final float height = LiScale.getScaleFactor(obj);

        final float interactionWidth = LiScale.getScaleFactor(obj);
        final float interactionHeight = LiScale.getScaleFactor(obj);

        if (width != 1.0F || height != 1.0F || interactionWidth != 1.0F || interactionHeight != 1.0F) {
            final double scaledXLength = value * ((width * interactionWidth) - 1.0F);
            final double scaledYLength = value * ((height * interactionHeight) - 1.0F);
            final double scaledZLength = value * ((width * interactionWidth) - 1.0F);

            return bounds.expand(scaledXLength, scaledYLength, scaledZLength);
        }

        return bounds;
    }

    @WrapOperation(method = "raycast", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getBoundingBox()Lnet/minecraft/util/math/Box;"))
    private static Box raycast$getBoundingBox(Entity obj, Operation<Box> original) {
        final Box bounds = original.call(obj);
        final float margin = obj.getTargetingMargin();

        final float interactionWidth = LiScale.getScaleFactor(obj);
        final float interactionHeight = LiScale.getScaleFactor(obj);

        if (interactionWidth != 1.0F || interactionHeight != 1.0F) {
            final double scaledXLength = bounds.getXLength() * 0.5D * (interactionWidth - 1.0F);
            final double scaledYLength = bounds.getYLength() * 0.5D * (interactionHeight - 1.0F);
            final double scaledZLength = bounds.getZLength() * 0.5D * (interactionWidth - 1.0F);
            final double scaledMarginWidth = margin * (interactionWidth - 1.0F);
            final double scaledMarginHeight = margin * (interactionHeight - 1.0F);

            return bounds.expand(scaledXLength + scaledMarginWidth, scaledYLength + scaledMarginHeight, scaledZLength + scaledMarginWidth);
        }

        return bounds;
    }

    @Dynamic
    @WrapOperation(method = "getCollision(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/Entity;Ljava/util/function/Predicate;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/World;)Lnet/minecraft/util/hit/HitResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Lnet/minecraft/util/hit/EntityHitResult;"))
    private static EntityHitResult getCollision$expand(World world, Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, Operation<EntityHitResult> original)
    {
        final float width = LiScale.getScaleFactor(entity);
        final float height = LiScale.getScaleFactor(entity);

        final float interactionWidth = LiScale.getScaleFactor(entity);
        final float interactionHeight = LiScale.getScaleFactor(entity);

        if (width != 1.0F || height != 1.0F || interactionWidth != 1.0F || interactionHeight != 1.0F) {
            box = box.expand((width * interactionWidth) - 1.0D, (height * interactionHeight) - 1.0D, (width * interactionWidth) - 1.0D);
        }

        return original.call(world, entity, min, max, box, predicate);
    }
}