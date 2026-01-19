package net.nerdman.multiversalhome.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerHomesProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerHomes> PLAYER_HOMES = CapabilityManager.get(new CapabilityToken<PlayerHomes>() {});

    private PlayerHomes homes = null;
    private final LazyOptional<PlayerHomes> optional = LazyOptional.of(this::createPlayerHomes);

    private PlayerHomes createPlayerHomes() {
        if(this.homes == null){
            this.homes = new PlayerHomes();
        }

        return this.homes;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_HOMES){
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerHomes().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerHomes().loadNBTData(nbt);
    }
}
