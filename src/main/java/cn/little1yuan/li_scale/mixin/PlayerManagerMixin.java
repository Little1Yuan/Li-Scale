package cn.little1yuan.li_scale.mixin;

import cn.little1yuan.li_scale.LiScale;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Inject(method = "onPlayerConnect", at = @At(value = "RETURN"))
    public void onPlayerConnectMixin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        LiScale.updateFactoriesToPlayer(player);
    }
}
