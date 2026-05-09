package net.pitan76.nexton.machinery.block;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.core.serialization.CompatMapCodec;
import net.pitan76.nexton.core.api.block.MachineBlock;
import net.pitan76.nexton.machinery.block.entity.BlockEntities;
import net.pitan76.nexton.machinery.block.entity.FuelGeneratorBlockEntity;

public class FuelGeneratorBlock extends MachineBlock {

    public static final CompatMapCodec<FuelGeneratorBlock> CODEC = CompatMapCodec.createCodecOfCompatBlock(FuelGeneratorBlock::new);

    public final int energyPerTick;
    public final int capacity;
    public final int maxOutput;

    public FuelGeneratorBlock(CompatibleBlockSettings settings) {
        this(settings, 3, 10_000, 500);
    }

    public FuelGeneratorBlock(CompatibleBlockSettings settings, int energyPerTick, int capacity, int maxOutput) {
        super(settings);
        this.energyPerTick = energyPerTick;
        this.capacity = capacity;
        this.maxOutput = maxOutput;
    }

    @Override
    public CompatMapCodec<? extends FuelGeneratorBlock> getCompatCodec() {
        return CODEC;
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent e) {
        return new FuelGeneratorBlockEntity(BlockEntities.FUEL_GENERATOR, e, energyPerTick, capacity, maxOutput);
    }

    @Override
    public boolean isTick() {
        return true;
    }

}
