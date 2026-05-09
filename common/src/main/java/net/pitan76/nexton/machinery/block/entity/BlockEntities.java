package net.pitan76.nexton.machinery.block.entity;

import net.pitan76.mcpitanlib.api.tile.CompatBlockEntity;
import net.pitan76.mcpitanlib.midohra.block.entity.BlockEntityTypeWrapper;
import net.pitan76.mcpitanlib.midohra.block.entity.TypedBlockEntityTypeWrapper;
import net.pitan76.nexton.machinery.block.Blocks;
import net.pitan76.mcpitanlib.api.tile.BlockEntityTypeBuilder;

import java.util.ArrayList;
import java.util.List;

import static net.pitan76.nexton.machinery.NextonMachinery._id;
import static net.pitan76.nexton.machinery.NextonMachinery.registry;

public class BlockEntities {

    private static List<BlockEntityTypeWrapper> bookingEnergyStorageBlockEntity = new ArrayList<>();

    public static TypedBlockEntityTypeWrapper<FuelGeneratorBlockEntity> FUEL_GENERATOR;
    public static TypedBlockEntityTypeWrapper<FuelGeneratorBlockEntity> FUEL_GENERATOR_MK2;
    public static TypedBlockEntityTypeWrapper<FuelGeneratorBlockEntity> FUEL_GENERATOR_MK3;
    public static TypedBlockEntityTypeWrapper<ElectricFurnaceBlockEntity> ELECTRIC_FURNACE;

    public static void init() {
        FUEL_GENERATOR = registerWithEnergyStorage("fuel_generator", BlockEntityTypeBuilder.create((e) -> new FuelGeneratorBlockEntity(BlockEntities.FUEL_GENERATOR, e), Blocks.FUEL_GENERATOR.get()));
        FUEL_GENERATOR_MK2 = registerWithEnergyStorage("fuel_generator_mk2", BlockEntityTypeBuilder.create((e) -> new FuelGeneratorBlockEntity(BlockEntities.FUEL_GENERATOR_MK2, e, 5, 20_000, 1500), Blocks.FUEL_GENERATOR_MK2.get()));
        FUEL_GENERATOR_MK3 = registerWithEnergyStorage("fuel_generator_mk3", BlockEntityTypeBuilder.create((e) -> new FuelGeneratorBlockEntity(BlockEntities.FUEL_GENERATOR_MK3, e, 10, 30_000, 3000), Blocks.FUEL_GENERATOR_MK3.get()));
        ELECTRIC_FURNACE = registerWithEnergyStorage("electric_furnace", BlockEntityTypeBuilder.create(ElectricFurnaceBlockEntity::new, Blocks.ELECTRIC_FURNACE.get()));
    }

    public static <T extends CompatBlockEntity> TypedBlockEntityTypeWrapper<T> register(String id, BlockEntityTypeBuilder<T> builder) {
        return registry.registerBlockEntityType0(_id(id), builder);
    }

    public static <T extends CompatBlockEntity> TypedBlockEntityTypeWrapper<T> registerWithEnergyStorage(String id, BlockEntityTypeBuilder<T> builder) {
        TypedBlockEntityTypeWrapper<T> result = register(id, builder);

        bookingEnergyStorageBlockEntity.add(result);
        return result;
    }

    public static List<BlockEntityTypeWrapper> getBookingEnergyStorageBlockEntity() {
        if (bookingEnergyStorageBlockEntity == null)
            throw new IllegalStateException("Already used. This method can only be called once.");

        List<BlockEntityTypeWrapper> copy = bookingEnergyStorageBlockEntity;
        bookingEnergyStorageBlockEntity = null;
        return copy;
    }
}
