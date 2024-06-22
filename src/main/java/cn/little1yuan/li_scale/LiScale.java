package cn.little1yuan.li_scale;

import cn.little1yuan.li_scale.effect.StatusEffects;
import cn.little1yuan.li_scale.item.Items;
import cn.little1yuan.li_scale.potion.Potions;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Objects;

public class LiScale implements ModInitializer {
    public static final String MOD_ID = "li_scale";
    public static MinecraftServer server = null;
    @Override
    public void onInitialize() {
        StatusEffects.init();
        Items.init();
        Potions.init();

        // Registry events
        ServerTickEvents.END_SERVER_TICK.register(server -> LiScale.server = server);
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            updateServerScaleFactor(newPlayer);
        });
    }

    public static float calculateFactorByAMP(int amp) {
        return (float) (1.0f * Math.pow(1.3f, amp));
    }

    public static void updateServerScaleFactor(LivingEntity entity) {
        if (!isServer()) return;
        scaleFactor.put(entity, calculateScale(entity));
        System.out.println("Update in server: " + server);
        if (server == null) return;
        PlayerLookup.all(server).forEach(player -> {
            PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
            packetByteBuf.writeVarInt(entity.getId());
            packetByteBuf.writeFloat(scaleFactor.get(entity));
            ServerPlayNetworking.send(player, new Identifier(MOD_ID, "factor_s2c"), packetByteBuf);
        });
    }

    public static void updateServerScaleFactor(LivingEntity entity, int amp) {
        if (!isServer()) return;
        scaleFactor.put(entity, calculateFactorByAMP(amp));
        System.out.println("Update in server: " + server);
        if (server == null) return;
        PlayerLookup.all(server).forEach(player -> {
            PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
            packetByteBuf.writeVarInt(entity.getId());
            packetByteBuf.writeFloat(scaleFactor.get(entity));
            ServerPlayNetworking.send(player, new Identifier(MOD_ID, "factor_s2c"), packetByteBuf);
        });
    }

    public static void updateFactoriesToPlayer(ServerPlayerEntity player) {
        if (!isServer()) return;
        System.out.println("Update all in server: " + server);
        scaleFactor.forEach((entity, aFloat) -> {
            PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
            packetByteBuf.writeVarInt(entity.getId());
            packetByteBuf.writeFloat(aFloat);
            ServerPlayNetworking.send(player, new Identifier(MOD_ID, "factor_s2c"), packetByteBuf);
        });
        PacketByteBuf selfBuf = new PacketByteBuf(Unpooled.buffer());
        selfBuf.writeVarInt(player.getId());
        scaleFactor.put(player, calculateScale(player));
        selfBuf.writeFloat(scaleFactor.get(player));
        ServerPlayNetworking.send(player, new Identifier(MOD_ID, "factor_s2c"), selfBuf);
    }

    public static float calculateScale(LivingEntity entity) {
        if (entity instanceof PlayerEntity && ((PlayerEntity) entity).getGameProfile() == null) return 1.0F;
        if (!(entity instanceof LivingEntity)) return 1.0f;

        try {
            if (entity.hasStatusEffect(StatusEffects.BIGGER_STATUS_EFFECT)) {
                return MathHelper.clamp(calculateFactorByAMP(getScaleAmp(entity) + 1), 0.1f, 10);
            }
            if (entity.hasStatusEffect(StatusEffects.SMALLER_STATUS_EFFECT)) {
                return MathHelper.clamp(calculateFactorByAMP(-getScaleAmp(entity) - 1), 0.1f, 10);
            }
        } catch (NullPointerException e) {
            return 1.0f;
        }

        return 1.0f;
    }

    public static HashMap<LivingEntity, Float> scaleFactor = new HashMap<>();
    public static HashMap<Integer, Float> clientScaleFactor = new HashMap<>();
    public static float getScaleFactor(LivingEntity entity) {
        if (isServer()) {
            if (entity instanceof PlayerEntity && ((PlayerEntity) entity).getGameProfile() == null) return 1.0F;
            if (!(entity instanceof LivingEntity)) return 1.0f;

            return MathHelper.clamp(scaleFactor.getOrDefault(entity, 1f), 0.1f, 10);
        } else {
            if (entity instanceof PlayerEntity && ((PlayerEntity) entity).getGameProfile() == null) return 1.0F;
            if (!(entity instanceof LivingEntity)) return 1.0f;

            return MathHelper.clamp(clientScaleFactor.getOrDefault(entity.getId(), 1f) , 0.1f, 10);
        }
    }

    public static float getScaleFactor(Entity entity) {
        if (entity instanceof LivingEntity) return getScaleFactor((LivingEntity) entity);
        return 1.0f;
    }

    public static int getScaleAmp(LivingEntity entity) {
        if (entity.hasStatusEffect(StatusEffects.BIGGER_STATUS_EFFECT)) {
            return Objects.requireNonNull(entity.getStatusEffect(StatusEffects.BIGGER_STATUS_EFFECT)).getAmplifier();
        }
        if (entity.hasStatusEffect(StatusEffects.SMALLER_STATUS_EFFECT)) {
            return Objects.requireNonNull(entity.getStatusEffect(StatusEffects.SMALLER_STATUS_EFFECT)).getAmplifier();
        }
        return 1;
    }

    public static boolean isServer() {
        return Thread.currentThread().getName().equals("Server thread");
    }
}
