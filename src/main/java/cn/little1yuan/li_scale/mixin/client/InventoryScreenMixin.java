package cn.little1yuan.li_scale.mixin.client;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {
    @Dynamic
    @Inject(method = "drawEntity(Lnet/minecraft/client/gui/DrawContext;IIILorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/entity/LivingEntity;)V", at = @At(value = "HEAD"))
    private static void drawEntity$head(@Coerce Object drawContext, int x, int y, int k, @Coerce Object quaternionf, @Nullable @Coerce Object quaternionf2, LivingEntity entity, CallbackInfo info, @Share("bounds") LocalRef<Box> bounds) {

        bounds.set(entity.getBoundingBox());

        final EntityDimensions dims = entity.getDimensions(entity.getPose());
        final Vec3d pos = entity.getPos();
        final double r = dims.width / 2.0D;
        final double h = dims.height;
        final double xPos = pos.x;
        final double yPos = pos.y;
        final double zPos = pos.z;
        final Box box = new Box(xPos - r, yPos, zPos - r, xPos + r, yPos + h, zPos + r);

        entity.setBoundingBox(box);
    }

    @Dynamic
    @Inject(method = "drawEntity(Lnet/minecraft/client/gui/DrawContext;IIILorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/entity/LivingEntity;)V", at = @At(value = "RETURN"))
    private static void drawEntity$return(@Coerce Object drawContext, int i, int j, int k, @Coerce Object quaternionf, @Nullable @Coerce Object quaternionf2, LivingEntity entity, CallbackInfo info, @Share("bounds") LocalRef<Box> bounds) {
        entity.setBoundingBox(bounds.get());
    }
}
