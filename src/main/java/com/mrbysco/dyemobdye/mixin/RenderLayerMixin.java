package com.mrbysco.dyemobdye.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.dyemobdye.AttachmentHandler;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;

@Mixin(RenderLayer.class)
public class RenderLayerMixin<T extends Entity> {
	@Unique
	private static WeakReference<Integer> dyeMobDye$colorReference = new WeakReference<>(null);

	@Inject(method = "renderColoredCutoutModel(Lnet/minecraft/client/model/EntityModel;Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;I)V",
			at = @At(value = "HEAD"))
	private static <T extends LivingEntity> void dyemobdye$getColors(EntityModel<T> model,
	                                                                 ResourceLocation textureLocation,
	                                                                 PoseStack poseStack, MultiBufferSource buffer,
	                                                                 int packedLight, T entity, int color,
	                                                                 CallbackInfo ci) {
		if (!(entity instanceof Sheep) && entity.hasData(AttachmentHandler.COLOR)) {
			DyeColor dyeColor = entity.getData(AttachmentHandler.COLOR);
			int newColor = dyeColor == DyeColor.WHITE ? color : Sheep.getColor(dyeColor);
			dyeMobDye$colorReference = new WeakReference<>(newColor);
		} else {
			dyeMobDye$colorReference = new WeakReference<>(null);
		}
	}

	@ModifyArg(method = "renderColoredCutoutModel(Lnet/minecraft/client/model/EntityModel;Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;I)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"),
			index = 4)
	private static int dyemobdye$changeColor(int color) {
		if (dyeMobDye$colorReference.get() != null) {
			return dyeMobDye$colorReference.get();
		}
		return color;
	}
}
