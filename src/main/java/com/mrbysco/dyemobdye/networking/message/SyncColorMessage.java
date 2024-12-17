package com.mrbysco.dyemobdye.networking.message;

import com.mrbysco.dyemobdye.CapabilityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class SyncColorMessage {
	private int entityId;
	private DyeColor color;

	public SyncColorMessage(int entityId, DyeColor color) {
		this.entityId = entityId;
		this.color = color;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(entityId);
		buf.writeInt(color.getId());
	}

	public static SyncColorMessage decode(final FriendlyByteBuf packetBuffer) {
		int entityId = packetBuffer.readInt();
		int colorId = packetBuffer.readInt();
		return new SyncColorMessage(entityId, DyeColor.byId(colorId));
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient()) {
				context.get().enqueueWork(new Runnable() {
					@Override
					public void run() {
						Entity entity = Minecraft.getInstance().level.getEntity(entityId);
						if (entity instanceof LivingEntity) {
							entity.getCapability(CapabilityHandler.COLOR_CAPABILITY).ifPresent(cap -> cap.setColor(color));
						}
					}
				});
				context.get().setPacketHandled(true);
			}
		});
		ctx.setPacketHandled(true);
	}
}
