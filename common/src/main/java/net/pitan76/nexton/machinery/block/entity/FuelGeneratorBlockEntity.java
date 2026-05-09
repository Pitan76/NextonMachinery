package net.pitan76.nexton.machinery.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.pitan76.nexton.core.NextonCore;
import net.pitan76.nexton.core.api.block.entity.MachineBlockEntityWithExtendedContainer;
import net.pitan76.nexton.core.api.energy.EnergyStorageProvider;
import net.pitan76.nexton.core.api.energy.IEnergyStorage;
import net.pitan76.nexton.core.api.energy.SimpleEnergyStorage;
import net.pitan76.nexton.core.api.util.EnergyUtil;
import net.pitan76.nexton.machinery.screen.FuelGeneratorScreenHandler;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.event.tile.TileTickEvent;
import net.pitan76.mcpitanlib.api.gui.args.CreateMenuEvent;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
import net.pitan76.mcpitanlib.core.registry.FuelRegistry;
import net.pitan76.mcpitanlib.guilib.api.block.entity.ExtendedBlockEntityWithContainer;

public class FuelGeneratorBlockEntity extends MachineBlockEntityWithExtendedContainer implements EnergyStorageProvider {

    public int burnTime = 0;
    public int maxBurnTime = 0;
    public final int energyPerTick;

    private final IEnergyStorage energyStorage;

    public FuelGeneratorBlockEntity(BlockEntityType<?> type, TileCreateEvent e) {
        this(type, e, 3, 10_000, 500);
    }

    public FuelGeneratorBlockEntity(BlockEntityType<?> type, TileCreateEvent e, int energyPerTick, int capacity, int maxOutput) {
        super(type, e);
        this.energyPerTick = energyPerTick;
        energyStorage = new SimpleEnergyStorage.Builder()
                .capacity(capacity)
                .maxInput(0)
                .maxOutput(maxOutput)
                .canExtract(true)
                .canInsert(false)
                .build();
    }

    @Override
    public void tick(TileTickEvent<ExtendedBlockEntityWithContainer> e) {
        super.tick(e);
        if (isClient()) return;

        if (!isFullEnergy()) {
            ItemStack stack = getStack(0);
            if (isBurning()) {
                burnTime -= 3;
                addEnergyStored(getEnergyPerTick());
            } else {
                boolean success = startBurn(stack);
                setActive(success);
                if (!success)
                    maxBurnTime = 0;
            }
        }

        if (!NextonCore.isUsingRebornEnergy)
            EnergyUtil.transferNearby(this, getEnergyStored());

        callMarkDirty();
    }

    @Override
    public void writeNbt(WriteNbtArgs args) {
        super.writeNbt(args);
        NbtUtil.putInt(args.nbt, "burnTime", burnTime);
        NbtUtil.putInt(args.nbt, "maxBurnTime", maxBurnTime);
    }

    @Override
    public void readNbt(ReadNbtArgs args) {
        super.readNbt(args);
        burnTime = NbtUtil.getInt(args.nbt, "burnTime");
        maxBurnTime = NbtUtil.getInt(args.nbt, "maxBurnTime");
    }

    public long getEnergyPerTick() {
        return energyPerTick;
    }

    public boolean isBurning() {
        return burnTime > 0;
    }

    public void startBurn(int time) {
        maxBurnTime = time;
        burnTime = time;
    }

    public boolean startBurn(ItemStack stack) {
        if (ItemStackUtil.isEmpty(stack)) return false;

        int time = FuelRegistry.get(callGetWorld(), stack);
        if (time == 0) return false;

        ItemStackUtil.decrementCount(stack, 1);
        startBurn(time);
        return true;
    }

    @Override
    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public void sync(Player player, PacketByteBuf buf) {
        PacketByteUtil.writeLong(buf, this.getEnergyStored());
        PacketByteUtil.writeInt(buf, this.burnTime);
        PacketByteUtil.writeInt(buf, this.maxBurnTime);
    }

    @Override
    public int getDefaultInvSize() {
        return 1;
    }

    @Override
    public ScreenHandler createMenu(CreateMenuEvent e) {
        return new FuelGeneratorScreenHandler(e, this, this);
    }
}
