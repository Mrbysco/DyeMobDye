package com.mrbysco.dyemobdye.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrbysco.dyemobdye.AttachmentHandler;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeOuterLayer.class)
public class SlimeOuterLayerMixin<T extends LivingEntity> {

	@Shadow
	@Final
	private EntityModel<T> model;

	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V"
			), cancellable = true
	)
	public void dyemobdye$changeColor(PoseStack poseStack, MultiBufferSource buffer,
	                                  int packedLight, T livingEntity, float limbSwing,
	                                  float limbSwingAmount, float partialTicks,
	                                  float ageInTicks, float netHeadYaw, float headPitch,
	                                  CallbackInfo ci, @Local VertexConsumer vertexconsumer) {
		if (livingEntity instanceof Slime && livingEntity.hasData(AttachmentHandler.COLOR)) {
			DyeColor dyeColor = livingEntity.getData(AttachmentHandler.COLOR);
			int color = dyeColor == DyeColor.WHITE ? -1 : Sheep.getColor(dyeColor);


			this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0F), color);
			ci.cancel();
		}
	}
}
