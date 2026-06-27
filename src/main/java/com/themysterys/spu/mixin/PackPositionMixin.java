package com.themysterys.spu.mixin;

import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Function;

@Mixin(Pack.Position.class)
public class PackPositionMixin {

    @Inject(method = "insert", at = @At("HEAD"), cancellable = true)
    public <T> void insert(List<T> items, T item, Function<T, PackSelectionConfig> profileGetter, boolean listInverted, CallbackInfoReturnable<Integer> cir) {
        Pack pack = (Pack)item;
        if (pack.getPackSource() == PackSource.SERVER) {
            items.add(items.size() - 1, item);
            cir.setReturnValue(items.size() - 1);
        }
    }
}
