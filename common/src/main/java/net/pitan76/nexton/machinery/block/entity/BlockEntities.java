package net.pitan76.nexton.machinery.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.pitan76.nexton.machinery.block.Blocks;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;
import net.pitan76.mcpitanlib.api.tile.BlockEntityTypeBuilder;

import java.util.ArrayList;
import java.util.List;

import static net.pitan76.nexton.machinery.NextonMachinery._id;
import static net.pitan76.nexton.machinery.NextonMachinery.registry;

public class BlockEntities {

    private static List<RegistryResult<BlockEntityType<?>>> bookingEnergyStorageBlockEntity = new ArrayList<>();

    public static RegistryResult<BlockEntityType<?>> FUEL_GENERATOR;
    public static RegistryResult<BlockEntityType<?>> FUEL_GENERATOR_MK2;
    public static RegistryResult<BlockEntityType<?>> FUEL_GENERATOR_MK3;
    public static RegistryResult<BlockEntityType<?>> ELECTRIC_FURNACE;

    public static void init() {
        FUEL_GENERATOR = registerWithEnergyStorage("fuel_generator", BlockEntityTypeBuilder.create((e) -> new FuelGeneratorBlockEntity(BlockEntities.FUEL_GENERATOR.get(), e), Blocks.FUEL_GENERATOR.getOrNull()));
        FUEL_GENERATOR_MK2 = registerWithEnergyStorage("fuel_generator_mk2", BlockEntityTypeBuilder.create((e) -> new FuelGeneratorBlockEntity(BlockEntities.FUEL_GENERATOR_MK2.get(), e, 5, 20_000, 1500), Blocks.FUEL_GENERATOR_MK2.getOrNull()));
        FUEL_GENERATOR_MK3 = registerWithEnergyStorage("fuel_generator_mk3", BlockEntityTypeBuilder.create((e) -> new FuelGeneratorBlockEntity(BlockEntities.FUEL_GENERATOR_MK3.get(), e, 10, 30_000, 3000), Blocks.FUEL_GENERATOR_MK3.getOrNull()));
        ELECTRIC_FURNACE = registerWithEnergyStorage("electric_furnace", BlockEntityTypeBuilder.create(ElectricFurnaceBlockEntity::new, Blocks.ELECTRIC_FURNACE.getOrNull()));
    }

    public static <T extends BlockEntity> RegistryResult<BlockEntityType<?>> register(String id, BlockEntityTypeBuilder<T> builder) {
        return registry.registerBlockEntityType(_id(id), builder::build);
    }

    public static <T extends BlockEntity> RegistryResult<BlockEntityType<?>> registerWithEnergyStorage(String id, BlockEntityTypeBuilder<T> builder) {
        RegistryResult<BlockEntityType<?>> result = register(id, builder);

        bookingEnergyStorageBlockEntity.add(result);
        return result;
    }

    public static List<RegistryResult<BlockEntityType<?>>> getBookingEnergyStorageBlockEntity() {
        if (bookingEnergyStorageBlockEntity == null)
            throw new IllegalStateException("Already used. This method can only be called once.");

        List<RegistryResult<BlockEntityType<?>>> copy = bookingEnergyStorageBlockEntity;
        bookingEnergyStorageBlockEntity = null;
        return copy;
    }
}
