package cn.little1yuan.li_scale.web;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;

public class ScaleFactorS2CPacket implements Packet<ServerPlayPacketListener> {
    private int entityId;
    private float factor;
    public ScaleFactorS2CPacket(int entityId, float factor) {
        this.entityId = entityId;
        this.factor = factor;
    }

    public ScaleFactorS2CPacket(PacketByteBuf buf) {
        entityId = buf.readVarInt();
        factor = buf.readFloat();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeFloat(factor);
    }

    @Override
    public void apply(ServerPlayPacketListener listener) {

    }

    public int getEntityId() {
        return entityId;
    }

    public float getFactor() {
        return factor;
    }
}
