package com.mrbysco.dyemobdye.client;

import com.google.common.reflect.TypeToken;
import com.mrbysco.dyemobdye.AttachmentHandler;
import com.mrbysco.dyemobdye.DyeMobDye;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;

public class ClientHandler {
	public static final ContextKey<DyeColor> COLOR = new ContextKey<>(DyeMobDye.modLoc("color"));

	public static void registerCustomRenderData(RegisterRenderStateModifiersEvent event) {
		event.registerEntityModifier(new TypeToken<LivingEntityRenderer<? extends LivingEntity, LivingEntityRenderState, ?>>() {}, (entity, renderState) -> {
			renderState.setRenderData(COLOR, entity.getData(AttachmentHandler.COLOR));
		});
	}
}
