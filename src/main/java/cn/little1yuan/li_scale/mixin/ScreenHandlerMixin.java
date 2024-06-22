package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @Dynamic
    @ModifyExpressionValue(method = { "method_17696", "func_216960_a", "m_38913_" }, require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 0))
    private static double canUse$xOffset(double value, Block block, PlayerEntity player, World world, BlockPos pos)
    {
        return getBlockXOffset(pos, player);
    }

    @Dynamic
    @ModifyExpressionValue(method = { "method_17696", "func_216960_a", "m_38913_" }, require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 1))
    private static double canUse$yOffset(double value, Block block, PlayerEntity player, World world, BlockPos pos)
    {
        return getBlockYOffset(pos, player);
    }

    @Dynamic
    @ModifyExpressionValue(method = { "method_17696", "func_216960_a", "m_38913_" }, require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 2))
    private static double canUse$zOffset(double value, Block block, PlayerEntity player, World world, BlockPos pos)
    {
        return getBlockZOffset(pos, player);
    }

    @Dynamic
    @ModifyExpressionValue(method = { "method_17696", "func_216960_a", "m_38913_" }, require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=64.0D"))
    private static double canUse$distance(double value, Block block, PlayerEntity player)
    {
        final float scale = LiScale.getScaleFactor(player);
        return scale > 1.0F ? scale * scale * value : value;
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