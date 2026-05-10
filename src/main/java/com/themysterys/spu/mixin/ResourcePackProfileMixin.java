package com.themysterys.spu.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Pack.class)
public class ResourcePackProfileMixin {

    @ModifyReturnValue(method = "isFixedPosition", at = @At("RETURN"))
    private boolean isFixedPosition(boolean original) {
        return false;
    }
}
