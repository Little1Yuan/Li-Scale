package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ForgingScreenHandler.class)
public class ForgingScreenHandlerMixin {
    @ModifyExpressionValue(method = { "method_24924"}, require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 0))
    private double canUse$xOffset(double value, PlayerEntity player, World world, BlockPos pos)
    {
        return getBlockXOffset(pos, player);
    }

    @ModifyExpressionValue(method = { "method_24924"}, require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 1))
    private double canUse$yOffset(double value, PlayerEntity player, World world, BlockPos pos)
    {
        return getBlockYOffset(pos, player);
    }

    @ModifyExpressionValue(method = { "method_24924" }, require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 2))
    private double canUse$zOffset(double value, PlayerEntity player, World world, BlockPos pos)
    {
        return getBlockZOffset(pos, player);
    }

    @Dynamic
    @ModifyExpressionValue(method = { "method_24924", "func_234646_a", "m_39783_" }, require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=64.0D"))
    private double canUse$distance(double value, PlayerEntity player) {
        final float scale = LiScale.getScaleFactor(player);
        return scale != 1.0F ? scale * scale * value : value;
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