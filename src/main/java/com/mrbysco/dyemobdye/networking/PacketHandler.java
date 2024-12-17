package com.mrbysco.dyemobdye.networking;

import com.mrbysco.dyemobdye.DyeMobDye;
import com.mrbysco.dyemobdye.networking.message.SyncColorMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(DyeMobDye.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	private static int id = 0;

	public static void init() {
		CHANNEL.registerMessage(id++, SyncColorMessage.class, SyncColorMessage::encode, SyncColorMessage::decode, SyncColorMessage::handle);
	}
}
