package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = Inventory.class, priority = 990)
public interface InventoryMixin {
    /**
     * Computes reach distance from eye position to the edge of the block
     * instead of to the center of the block
     * @author NaHCO3
     * @reason 内部的原理和事物的道理
     */
    @Dynamic
    @Overwrite
    static boolean canPlayerUse(BlockEntity blockEntity, PlayerEntity player, int range) {
        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();

        if (world == null) {
            return false;
        } else if (world.getBlockEntity(pos) != blockEntity) {
            return false;
        } else {
            double x = ((double) pos.getX()) + 0.5D;
            double y = ((double) pos.getY()) + 0.5D;
            double z = ((double) pos.getZ()) + 0.5D;
            final Vec3d eyePos = player.getEyePos();
            x = (x - 0.5D) + getBlockXOffset(pos, player) - (eyePos.getX() - player.getX());
            y = (y - 0.5D) + getBlockYOffset(pos, player) - (eyePos.getY() - player.getY());
            z = (z - 0.5D) + getBlockZOffset(pos, player) - (eyePos.getZ() - player.getZ());
            final double reach = LiScale.getScaleFactor(player) * (double) range;
            return player.squaredDistanceTo(x, y, z) <= reach * reach;
        }
    }

    @Unique
    private static double getBlockXOffset(BlockPos pos, PlayerEntity player) {
        final int blockCoord = pos.getX();
        final Direction gravity = Direction.DOWN;
        final double offset = player.getStandingEyeHeight() * -gravity.getOffsetX();
        final double footCoord = player.getPos().getX();
        final double headCoord = footCoord + offset;
        final int headCoordFloored = MathHelper.floor(headCoord);

        if (headCoordFloored == blockCoord) {
            return footCoord - (double) headCoordFloored;
        } else if (headCoordFloored > blockCoord) {
            return 1.0F - offset;
        }
        return -offset;
    }

    @Unique
    private static double getBlockYOffset(BlockPos pos, PlayerEntity player) {
        final int blockCoord = pos.getY();
        final Direction gravity = Direction.DOWN;
        final double offset = player.getStandingEyeHeight() * -gravity.getOffsetY();
        final double footCoord = player.getPos().getY();
        final double headCoord = footCoord + offset;
        final int headCoordFloored = MathHelper.floor(headCoord);

        if (headCoordFloored == blockCoord) {
            return footCoord - (double) headCoordFloored;
        } else if (headCoordFloored > blockCoord) {
            return 1.0F - offset;
        }

        return -offset;
    }

    @Unique
    private static double getBlockZOffset(BlockPos pos, PlayerEntity player) {
        final int blockCoord = pos.getZ();
        final Direction gravity = Direction.DOWN;
        final double offset = player.getStandingEyeHeight() * -gravity.getOffsetZ();
        final double footCoord = player.getPos().getZ();
        final double headCoord = footCoord + offset;
        final int headCoordFloored = MathHelper.floor(headCoord);

        if (headCoordFloored == blockCoord)
        {
            return footCoord - (double) headCoordFloored;
        } else if (headCoordFloored > blockCoord) {
            return 1.0F - offset;
        }

        return -offset;
    }
}