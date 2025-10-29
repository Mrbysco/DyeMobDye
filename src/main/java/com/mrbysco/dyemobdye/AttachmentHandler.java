package com.mrbysco.dyemobdye;

import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AttachmentHandler {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, DyeMobDye.MOD_ID);
	public static final Supplier<AttachmentType<DyeColor>> COLOR = ATTACHMENT_TYPES.register("color", () ->
			AttachmentType.builder(() -> DyeColor.WHITE).serialize(DyeColor.CODEC.fieldOf("color")).sync(DyeColor.STREAM_CODEC).build()
	);
}
