package com.themysterys.spu.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ClientboundResourcePackPushPacket.class)
public abstract class ResourcePackSendS2CPacketMixin {

    @Shadow
    public abstract UUID id();

    @Inject(method = "handle(Lnet/minecraft/network/protocol/common/ClientCommonPacketListener;)V", at = @At("HEAD"), cancellable = true)
    private void apply(ClientCommonPacketListener listener, CallbackInfo ci) {
        ServerData serverData = Minecraft.getInstance().getCurrentServer();
        if (serverData != null && serverData.getResourcePackStatus() == ServerData.ServerPackStatus.DISABLED) {
            if (Minecraft.getInstance().getConnection() != null) {
                Minecraft.getInstance().getConnection().send(new ServerboundResourcePackPacket(this.id(), ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED));
            }
            ci.cancel();
        }
    }

}
