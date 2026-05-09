package net.pitan76.nexton.machinery.block;

import net.minecraft.block.Block;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;

import static net.pitan76.nexton.machinery.NextonMachinery._id;
import static net.pitan76.nexton.machinery.NextonMachinery.registry;

public class Blocks {

    public static RegistryResult<Block> FUEL_GENERATOR;
    public static RegistryResult<Block> FUEL_GENERATOR_MK2;
    public static RegistryResult<Block> FUEL_GENERATOR_MK3;
    public static RegistryResult<Block> ELECTRIC_FURNACE;

    public static void init() {
        FUEL_GENERATOR = registry.registerBlock(_id("generator"), () -> new FuelGeneratorBlock(CompatibleBlockSettings.of(_id("generator"), CompatibleMaterial.STONE), 3, 10_000, 500));
        FUEL_GENERATOR_MK2 = registry.registerBlock(_id("generator_mk2"), () -> new FuelGeneratorBlock(CompatibleBlockSettings.of(_id("generator_mk2"), CompatibleMaterial.STONE), 5, 20_000, 1500));
        FUEL_GENERATOR_MK3 = registry.registerBlock(_id("generator_mk3"), () -> new FuelGeneratorBlock(CompatibleBlockSettings.of(_id("generator_mk3"), CompatibleMaterial.STONE), 10, 30_000, 3000));
        ELECTRIC_FURNACE = registry.registerBlock(_id("electric_furnace"), () -> new ElectricFurnaceBlock(CompatibleBlockSettings.of(_id("electric_furnace"), CompatibleMaterial.STONE)));
    }
}
