package com.mrbysco.dyemobdye.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.dyemobdye.CapabilityHandler;
import com.mrbysco.dyemobdye.capability.IMobColor;
import com.mrbysco.dyemobdye.client.ColorReference;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;

@Mixin(SlimeOuterLayer.class)
public class SlimeOuterLayerMixin<T extends LivingEntity> {
	@Unique
	private static WeakReference<ColorReference> dyeMobDye$colorReference = new WeakReference<>(null);

	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
			at = @At(value = "HEAD"))
	public <T extends LivingEntity> void dyemobdye_getColors(PoseStack p_117470_, MultiBufferSource p_117471_, int p_117472_, T entity, float p_117474_, float p_117475_, float p_117476_, float p_117477_, float p_117478_, float p_117479_, CallbackInfo ci) {
		IMobColor mobColor = entity.getCapability(CapabilityHandler.COLOR_CAPABILITY).orElse(null);
		if (entity instanceof Slime && mobColor != null) {
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

	@ModifyArg(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
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
			method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
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
			method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
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
