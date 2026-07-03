package com.elyas.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(IntegratedServer.class)
public class ExampleClientMixin {

	@Unique
	private boolean quicklan$published = false;

	@Inject(method = "tickServer", at = @At("HEAD"))
	private void quicklan$onTickServer(BooleanSupplier haveTime, CallbackInfo ci) {
		if (this.quicklan$published) {
			return;
		}

		// Guard: Wait until the client-side connection and player are completely ready
		if (Minecraft.getInstance().getConnection() == null || Minecraft.getInstance().player == null) {
			return;
		}

		this.quicklan$published = true;

		IntegratedServer server = (IntegratedServer) (Object) this;

		// Disable online mode for incoming LAN players
		server.setUsesAuthentication(false);

		// Safe to publish now that the client handshake is fully complete
		boolean published = server.publishServer(GameType.SURVIVAL, false, 0);

		if (published) {
			java.lang.System.out.println("[Quick-LAN] World successfully opened to LAN in offline mode!");
		} else {
			java.lang.System.err.println("[Quick-LAN] Failed to auto-open world to LAN.");
		}
	}
}