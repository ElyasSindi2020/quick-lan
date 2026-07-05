package com.elyas.client.mixin;

import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.players.NameAndId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {

    @Inject(method = "isSingleplayerOwner", at = @At("HEAD"), cancellable = true)
    private void quicklan$neverTreatAnyoneAsSingleplayerOwner(NameAndId nameAndId, CallbackInfoReturnable<Boolean> cir) {
        // Force every player (including the current host) to be identified purely
        // by their own offline-UUID-from-name, instead of being redirected to
        // whichever UUID last hosted this shared world.
        cir.setReturnValue(false);
    }
}