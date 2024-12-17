package com.mrbysco.dyemobdye;

import com.mrbysco.dyemobdye.capability.IMobColor;
import com.mrbysco.dyemobdye.capability.MobColorCapability;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityHandler {
	public static final Capability<IMobColor> COLOR_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	@SubscribeEvent
	public void onEntityConstructing(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof LivingEntity) {
			event.addCapability(DyeMobDye.COLOR_CAP, new MobColorCapability());
		}
	}
}
