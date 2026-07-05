package com.elyas.client.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerLoginPacketListenerImpl.class)
public class ServerLoginPacketListenerImplMixin {

    @ModifyVariable(method = "startClientVerification", at = @At("HEAD"), argsOnly = true)
    private GameProfile quicklan$forceOfflineProfile(GameProfile profile) {
        return UUIDUtil.createOfflineProfile(profile.name());
    }
}