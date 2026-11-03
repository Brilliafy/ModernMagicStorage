package com.brilliafy.magicstorage.mixin;

import com.brilliafy.magicstorage.Constants;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Inject(method = "init", at = @At("HEAD"))
    private void onInit(CallbackInfo ci) {
        Constants.LOGGER.debug("TitleScreen initialized with ModernMagicStorage");
    }
}
