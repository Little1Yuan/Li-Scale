package cn.little1yuan.li_scale.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

import static cn.little1yuan.li_scale.LiScale.MOD_ID;
import static cn.little1yuan.li_scale.LiScale.clientScaleFactor;

public class LiScaleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Registry packet receivers
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "factor_s2c"), (client, handler, buf, responseSender) -> clientScaleFactor.put(buf.readVarInt(), buf.readFloat()));
    }
}
