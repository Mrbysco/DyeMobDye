package com.mrbysco.dyemobdye.capability;

import com.mrbysco.dyemobdye.CapabilityHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MobColorCapability implements IMobColor, ICapabilitySerializable<CompoundTag> {
	private DyeColor color;

	public MobColorCapability() {
		this.color = DyeColor.WHITE;
	}

	@Override
	public DyeColor getColor() {
		return color;
	}

	@Override
	public void setColor(DyeColor color) {
		this.color = color;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		return CapabilityHandler.COLOR_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this));
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("color", (byte) (color.getId() & 240 | color.getId() & 15));
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		setColor(DyeColor.byId(nbt.getInt("color") & 15));
	}
}
