package com.mrbysco.dyemobdye;

import com.mojang.logging.LogUtils;
import com.mrbysco.dyemobdye.handler.InteractionHandler;
import com.mrbysco.dyemobdye.networking.PacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(DyeMobDye.MOD_ID)
public class DyeMobDye {
	public static final String MOD_ID = "dyemobdye";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final ResourceLocation COLOR_CAP = new ResourceLocation(MOD_ID, "capability.color");

	public DyeMobDye() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		eventBus.addListener(this::setup);

		MinecraftForge.EVENT_BUS.register(new InteractionHandler());
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
	}

	private void setup(final FMLCommonSetupEvent event) {
		PacketHandler.init();
	}
}
