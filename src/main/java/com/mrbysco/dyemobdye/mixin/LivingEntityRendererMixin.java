package com.mrbysco.dyemobdye.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.dyemobdye.CapabilityHandler;
import com.mrbysco.dyemobdye.capability.IMobColor;
import com.mrbysco.dyemobdye.client.ColorReference;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
	private static WeakReference<ColorReference> dyeMobDye$colorReference = new WeakReference<>(null);

	@Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
			at = @At(value = "HEAD"))
	public <T extends LivingEntity> void dyemobdye_getColors(T entity, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo ci) {
		IMobColor mobColor = entity.getCapability(CapabilityHandler.COLOR_CAPABILITY).orElse(null);
		if (!(entity instanceof Sheep) && mobColor != null) {
			DyeColor dyeColor = mobColor.getColor();
			float r;
			float g;
			float b;
			if (dyeColor == DyeColor.WHITE) {
				r = 1.0F;
				g = 1.0F;
				b = 1.0F;
			} else {
				float[] colorArray = Sheep.getColorArray(dyeColor);
				r = colorArray[0];
				g = colorArray[1];
				b = colorArray[2];
			}
			dyeMobDye$colorReference = new WeakReference<>(new ColorReference(r, g, b));
		} else {
			dyeMobDye$colorReference = new WeakReference<>(null);
		}
	}

	@ModifyArg(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"
			),
			index = 4
	)
	public float dyemobdye_changeRed(float red) {
		if (dyeMobDye$colorReference.get() != null) {
			return dyeMobDye$colorReference.get().red();
		}
		return red;
	}

	@ModifyArg(
			method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"
			),
			index = 5
	)
	public float dyemobdye_changeGreen(float green) {
		if (dyeMobDye$colorReference.get() != null) {
			return dyeMobDye$colorReference.get().green();
		}
		return green;
	}

	@ModifyArg(
			method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"
			),
			index = 6
	)
	public float dyemobdye_changeBlue(float blue) {
		if (dyeMobDye$colorReference.get() != null) {
			return dyeMobDye$colorReference.get().blue();
		}
		return blue;
	}
}
