package com.mrbysco.dyemobdye.capability;

import net.minecraft.world.item.DyeColor;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IMobColor {
	DyeColor getColor();

	void setColor(DyeColor color);
}
