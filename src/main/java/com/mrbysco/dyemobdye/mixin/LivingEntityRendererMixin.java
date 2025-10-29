package com.mrbysco.dyemobdye.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.dyemobdye.AttachmentHandler;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
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

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
	@Unique
	private static WeakReference<Integer> dyeMobDye$colorReference = new WeakReference<>(null);

	@Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
			at = @At(value = "HEAD"))
	public void dyemobdye$getColors(T entity, float entityYaw, float partialTicks,
	                                PoseStack poseStack, MultiBufferSource buffer,
	                                int packedLight, CallbackInfo ci) {
		if (!(entity instanceof Sheep)) {
			DyeColor dyeColor = entity.getData(AttachmentHandler.COLOR);
			int color = dyeColor == DyeColor.WHITE ? -1 : Sheep.getColor(dyeColor);
			dyeMobDye$colorReference = new WeakReference<>(color);
		} else {
			dyeMobDye$colorReference = new WeakReference<>(null);
		}
	}

	@ModifyArg(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"
			),
			index = 4
	)
	public int dyemobdye$changeColor(int color) {
		if (dyeMobDye$colorReference.get() != null) {
			return dyeMobDye$colorReference.get();
		}
		return color;
	}
}
