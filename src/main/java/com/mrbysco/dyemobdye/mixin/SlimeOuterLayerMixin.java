package com.mrbysco.dyemobdye.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.dyemobdye.client.ClientHandler;
import net.minecraft.client.color.ColorLerper;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;

@Mixin(SlimeOuterLayer.class)
public class SlimeOuterLayerMixin {
	@Unique
	private static WeakReference<Integer> dyeMobDye$colorReference = new WeakReference<>(null);

	@Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/SlimeRenderState;FF)V",
			at = @At(value = "HEAD")
	)
	public void dyemobdye$changeColor(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight,
	                                  SlimeRenderState renderState, float yRot, float xRot,
	                                  CallbackInfo ci) {
		if (renderState.getRenderData(ClientHandler.COLOR) != null) {
			DyeColor dyeColor = renderState.getRenderData(ClientHandler.COLOR);
			int color = dyeColor == DyeColor.WHITE ? -1 : ColorLerper.Type.SHEEP.getColor(dyeColor);
			dyeMobDye$colorReference = new WeakReference<>(color);
		} else {
			dyeMobDye$colorReference = new WeakReference<>(null);
		}
	}

	@ModifyArg(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/SlimeRenderState;FF)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"
					, ordinal = 0
			),
			index = 6
	)
	public int dyemobdye$changeColor(int color) {
		if (dyeMobDye$colorReference.get() != null) {
			return dyeMobDye$colorReference.get();
		}
		return color;
	}

	@ModifyArg(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/SlimeRenderState;FF)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"
					, ordinal = 1
			),
			index = 6
	)
	public int dyemobdye$changeColor2(int color) {
		if (dyeMobDye$colorReference.get() != null) {
			return dyeMobDye$colorReference.get();
		}
		return color;
	}
}
