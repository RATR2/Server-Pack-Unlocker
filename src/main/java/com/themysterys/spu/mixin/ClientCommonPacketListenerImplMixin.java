package com.themysterys.spu.mixin;

import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class ClientCommonPacketListenerImplMixin {

    @Shadow
    @Final
    @Nullable
    protected ServerData serverData;

    @Shadow
    @Final
    protected Connection connection;

    @Inject(method = "handleResourcePackPush", at = @At("HEAD"), cancellable = true)
    public void rejectPackPrompt(ClientboundResourcePackPushPacket packet, CallbackInfo ci) {
        ServerData.ServerPackStatus serverPackStatus = this.serverData != null ? this.serverData.getResourcePackStatus() : null;
        if (serverPackStatus == ServerData.ServerPackStatus.DISABLED) {
            connection.send(new ServerboundResourcePackPacket(packet.id(), ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED));
            ci.cancel();
        }
    }
}
