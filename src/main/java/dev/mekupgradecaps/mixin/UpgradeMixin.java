package dev.mekupgradecaps.mixin;

import dev.mekupgradecaps.UpgradeCapConfig;
import mekanism.api.Upgrade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Upgrade.class, remap = false)
public abstract class UpgradeMixin {
    @Inject(method = "getMax", at = @At("HEAD"), cancellable = true)
    private void mekupgradecaps$getMax(CallbackInfoReturnable<Integer> cir) {
        Upgrade upgrade = (Upgrade) (Object) this;
        if (upgrade == Upgrade.SPEED) {
            cir.setReturnValue(UpgradeCapConfig.speedMax());
        } else if (upgrade == Upgrade.ENERGY) {
            cir.setReturnValue(UpgradeCapConfig.energyMax());
        }
    }
}
