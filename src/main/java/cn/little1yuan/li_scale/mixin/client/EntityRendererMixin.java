package cn.little1yuan.li_scale.mixin.client;

import cn.little1yuan.li_scale.LiScale;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin
{
    @Dynamic
    @WrapOperation(method = "renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getNameLabelHeight()F"))
    private float renderLabelIfPresent$getNameLabelHeight(Entity entity, Operation<Float> original)
    {
        return (original.call(entity) - entity.getHeight()) + (entity.getHeight() / LiScale.getScaleFactor(entity));
    }
}