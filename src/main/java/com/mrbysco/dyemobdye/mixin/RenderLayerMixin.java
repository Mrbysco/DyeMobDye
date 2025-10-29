package com.mrbysco.dyemobdye.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.dyemobdye.client.ClientHandler;
import net.minecraft.client.color.ColorLerper;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;

@Mixin(RenderLayer.class)
public class RenderLayerMixin<S extends EntityRenderState> {
	@Unique
	private static WeakReference<Integer> dyeMobDye$colorReference = new WeakReference<>(null);

	@Inject(method = "renderColoredCutoutModel(Lnet/minecraft/client/model/Model;Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;II)V",
			at = @At(value = "HEAD"))
	private static <S extends LivingEntityRenderState> void dyemobdye$getColors(Model<? super S> model,
	                                                                           ResourceLocation textureLocation,
	                                                                           PoseStack poseStack,
	                                                                           SubmitNodeCollector nodeCollector,
	                                                                           int packedLight, S renderState,
	                                                                           int tintColor, int outlineColor,
	                                                                           CallbackInfo ci) {
		if (renderState.getRenderData(ClientHandler.COLOR) != null) {
			DyeColor dyeColor = renderState.getRenderData(ClientHandler.COLOR);
			int color = dyeColor == DyeColor.WHITE ? -1 : ColorLerper.Type.SHEEP.getColor(dyeColor);
			dyeMobDye$colorReference = new WeakReference<>(color);
		} else {
			dyeMobDye$colorReference = new WeakReference<>(null);
		}
	}

	@ModifyArg(method = "renderColoredCutoutModel(Lnet/minecraft/client/model/Model;Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;II)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"),
			index = 6)
	private static int dyemobdye$changeColor(int color) {
		if (dyeMobDye$colorReference.get() != null) {
			return dyeMobDye$colorReference.get();
		}
		return color;
	}
}
