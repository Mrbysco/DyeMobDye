package com.mrbysco.dyemobdye;

import com.mojang.logging.LogUtils;
import com.mrbysco.dyemobdye.handler.InteractionHandler;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(DyeMobDye.MOD_ID)
public class DyeMobDye {
	public static final String MOD_ID = "dyemobdye";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final ResourceLocation COLOR_CAP = modLoc("capability.color");

	public DyeMobDye(IEventBus eventBus) {
		AttachmentHandler.ATTACHMENT_TYPES.register(eventBus);

		NeoForge.EVENT_BUS.register(new InteractionHandler());
	}

	public static ResourceLocation modLoc(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}
}
