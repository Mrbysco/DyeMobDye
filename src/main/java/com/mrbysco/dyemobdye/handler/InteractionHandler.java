package com.mrbysco.dyemobdye.handler;

import com.mrbysco.dyemobdye.AttachmentHandler;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class InteractionHandler {
	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
		final Level level = event.getLevel();
		final Player player = event.getEntity();
		final ItemStack stack = event.getItemStack();
		final Entity entity = event.getTarget();
		if (event.getItemStack().getItem() instanceof DyeItem dyeItem &&
				!level.isClientSide() && entity.isAlive()) {
			final DyeColor dyeColor = dyeItem.getDyeColor();

			if (!(entity instanceof Sheep)) {
				DyeColor mobColor = entity.getData(AttachmentHandler.COLOR);
				if (mobColor != dyeColor) {
					level.playSound(player, entity, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

					if (entity instanceof Wolf wolf && wolf.isOwnedBy(player)) {
						if (wolf.getCollarColor() != dyeColor) {
							wolf.setCollarColor(dyeColor);
						}
					}

					entity.setData(AttachmentHandler.COLOR, dyeColor);
					entity.syncData(AttachmentHandler.COLOR);
					if (!player.getAbilities().instabuild)
						stack.shrink(1);
					event.setCancellationResult(InteractionResult.sidedSuccess(false));
				}
			}
		}
	}
}
