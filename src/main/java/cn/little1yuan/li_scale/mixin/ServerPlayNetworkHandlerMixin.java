package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @ModifyExpressionValue(method = "onPlayerInteractBlock", require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 0))
    private double onPlayerInteractBlock$xOffset(double value, PlayerInteractBlockC2SPacket packet)
    {
        return getBlockXOffset(packet.getBlockHitResult().getBlockPos(), player);
    }

    @ModifyExpressionValue(method = "onPlayerInteractBlock", require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 1))
    private double onPlayerInteractBlock$yOffset(double value, PlayerInteractBlockC2SPacket packet)
    {
        return getBlockYOffset(packet.getBlockHitResult().getBlockPos(), player);
    }

    @ModifyExpressionValue(method = "onPlayerInteractBlock", require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 2))
    private double onPlayerInteractBlock$zOffset(double value, PlayerInteractBlockC2SPacket packet)
    {
        return getBlockZOffset(packet.getBlockHitResult().getBlockPos(), player);
    }

    @ModifyArg(method = "onVehicleMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;contract(D)Lnet/minecraft/util/math/Box;"))
    private double onVehicleMove$contract(double value)
    {
        final float scale = LiScale.getScaleFactor(player);
        return scale < 1.0F ? value * scale : value;
    }

    @ModifyArg(method = "onVehicleMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"))
    private Vec3d onVehicleMove$move(MovementType type, Vec3d movement)
    {
        final float scale = LiScale.getScaleFactor(player.getRootVehicle());
        return scale != 1.0F ? movement.multiply(1.0F / scale) : movement;
    }

    @ModifyArg(method = "onPlayerMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"))
    private Vec3d onPlayerMove$move(MovementType type, Vec3d movement)
    {
        final float scale = LiScale.getScaleFactor(player);
        return scale != 1.0F ? movement.multiply(1.0F / scale) : movement;
    }

    @Unique
    private static double getBlockXOffset(BlockPos pos, PlayerEntity player)
    {
        final int blockCoord = pos.getX();
        final Direction gravity = Direction.DOWN;
        final double offset = player.getStandingEyeHeight() * -gravity.getOffsetX();
        final double footCoord = player.getPos().getX();
        final double headCoord = footCoord + offset;
        final int headCoordFloored = MathHelper.floor(headCoord);

        if (headCoordFloored == blockCoord)
        {
            return footCoord - (double) headCoordFloored;
        }
        else if (headCoordFloored > blockCoord)
        {
            return 1.0F - offset;
        }

        return -offset;
    }

    @Unique
    private static double getBlockYOffset(BlockPos pos, PlayerEntity player)
    {
        final int blockCoord = pos.getY();
        final Direction gravity = Direction.DOWN;
        final double offset = player.getStandingEyeHeight() * -gravity.getOffsetY();
        final double footCoord = player.getPos().getY();
        final double headCoord = footCoord + offset;
        final int headCoordFloored = MathHelper.floor(headCoord);

        if (headCoordFloored == blockCoord)
        {
            return footCoord - (double) headCoordFloored;
        }
        else if (headCoordFloored > blockCoord)
        {
            return 1.0F - offset;
        }

        return -offset;
    }

    @Unique
    private static double getBlockZOffset(BlockPos pos, PlayerEntity player)
    {
        final int blockCoord = pos.getZ();
        final Direction gravity = Direction.DOWN;
        final double offset = player.getStandingEyeHeight() * -gravity.getOffsetZ();
        final double footCoord = player.getPos().getZ();
        final double headCoord = footCoord + offset;
        final int headCoordFloored = MathHelper.floor(headCoord);

        if (headCoordFloored == blockCoord)
        {
            return footCoord - (double) headCoordFloored;
        }
        else if (headCoordFloored > blockCoord)
        {
            return 1.0F - offset;
        }

        return -offset;
    }
}