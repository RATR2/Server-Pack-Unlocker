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
        int i;
        if (((Pack) item).getPackSource() == PackSource.SERVER) {
            for (i = 0; i < items.size(); i++) {
                Pack pack = (Pack) items.get(i);
                System.out.println("[SPU] Pack Item:" + pack.getId());
                System.out.println("[SPU] Pack Pos:" + pack.getDefaultPosition());
                System.out.println("[SPU] i:" + i);
                if (!pack.isFixedPosition() || pack.getDefaultPosition() != Pack.Position.TOP) {
                    break;
                }
            }
            i += 1;
            items.add(i, item);
            cir.setReturnValue(i);
        }
    }
}
