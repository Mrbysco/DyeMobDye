package com.mrbysco.dyemobdye.handler;

import com.mrbysco.dyemobdye.CapabilityHandler;
import com.mrbysco.dyemobdye.capability.IMobColor;
import com.mrbysco.dyemobdye.networking.PacketHandler;
import com.mrbysco.dyemobdye.networking.message.SyncColorMessage;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

public class InteractionHandler {
	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
		final Level level = event.getLevel();
		final Player player = event.getEntity();
		final ItemStack stack = event.getItemStack();
		final Entity entity = event.getTarget();
		if (event.getItemStack().getItem() instanceof DyeItem dyeItem &&
				!level.isClientSide && entity.isAlive()) {
			final DyeColor dyeColor = dyeItem.getDyeColor();

			if (!(entity instanceof Sheep)) {
				IMobColor mobColor = entity.getCapability(CapabilityHandler.COLOR_CAPABILITY).orElse(null);
				if (mobColor != null && mobColor.getColor() != dyeColor) {
					level.playSound(player, entity, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

					if (entity instanceof Wolf wolf && wolf.isOwnedBy(player)) {
						if (wolf.getCollarColor() != dyeColor) {
							wolf.setCollarColor(dyeColor);
						}
					}

					if (!level.isClientSide) {
						mobColor.setColor(dyeColor);
						PacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
								new SyncColorMessage(entity.getId(), dyeColor));
						if (!player.getAbilities().instabuild)
							stack.shrink(1);
					}
					event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityTrack(StartTracking event) {
		Entity entity = event.getTarget();
		if (!(entity instanceof Sheep)) {
			IMobColor mobColor = entity.getCapability(CapabilityHandler.COLOR_CAPABILITY).orElse(null);
			if (mobColor != null) {
				PacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
						new SyncColorMessage(entity.getId(), mobColor.getColor()));
			}
		}
	}
}
