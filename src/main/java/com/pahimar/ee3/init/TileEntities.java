package com.pahimar.ee3.init;

import com.pahimar.ee3.reference.Names;
import com.pahimar.ee3.tileentity.TileEntityAlchemicalChest;
import com.pahimar.ee3.tileentity.TileEntityAlchemicalChestLarge;
import com.pahimar.ee3.tileentity.TileEntityAlchemicalChestMedium;
import com.pahimar.ee3.tileentity.TileEntityAlchemicalChestSmall;
import com.pahimar.ee3.tileentity.TileEntityAlchemyArray;
import com.pahimar.ee3.tileentity.TileEntityAludel;
import com.pahimar.ee3.tileentity.TileEntityAugmentationTable;
import com.pahimar.ee3.tileentity.TileEntityCalcinator;
import com.pahimar.ee3.tileentity.TileEntityDummyArray;
import com.pahimar.ee3.tileentity.TileEntityGlassBell;
import com.pahimar.ee3.tileentity.TileEntityResearchStation;
import com.pahimar.ee3.tileentity.TileEntityTransmutationTablet;

import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntities {

    public static void init() {
        GameRegistry.registerTileEntityWithAlternatives(
            TileEntityAlchemicalChest.class,
            Names.Blocks.ALCHEMICAL_CHEST,
            "tile." + Names.Blocks.ALCHEMICAL_CHEST);
        GameRegistry.registerTileEntityWithAlternatives(
            TileEntityAlchemicalChestSmall.class,
            Names.Blocks.ALCHEMICAL_CHEST + "Small",
            "tile." + Names.Blocks.ALCHEMICAL_CHEST + "Small");
        GameRegistry.registerTileEntityWithAlternatives(
            TileEntityAlchemicalChestMedium.class,
            Names.Blocks.ALCHEMICAL_CHEST + "Medium",
            "tile." + Names.Blocks.ALCHEMICAL_CHEST + "Medium");
        GameRegistry.registerTileEntityWithAlternatives(
            TileEntityAlchemicalChestLarge.class,
            Names.Blocks.ALCHEMICAL_CHEST + "Large",
            "tile." + Names.Blocks.ALCHEMICAL_CHEST + "Large");
        GameRegistry.registerTileEntityWithAlternatives(
            TileEntityAludel.class,
            Names.Blocks.ALUDEL,
            "tile." + Names.Blocks.ALUDEL);
        GameRegistry.registerTileEntityWithAlternatives(
            TileEntityCalcinator.class,
            Names.Blocks.CALCINATOR,
            "tile." + Names.Blocks.CALCINATOR);
        GameRegistry.registerTileEntityWithAlternatives(
            TileEntityGlassBell.class,
            Names.Blocks.GLASS_BELL,
            "tile." + Names.Blocks.GLASS_BELL);
        GameRegistry.registerTileEntity(TileEntityResearchStation.class, Names.Blocks.RESEARCH_STATION);
        GameRegistry.registerTileEntity(TileEntityAugmentationTable.class, Names.Blocks.AUGMENTATION_TABLE);
        GameRegistry.registerTileEntity(TileEntityAlchemyArray.class, Names.Blocks.ALCHEMY_ARRAY);
        GameRegistry.registerTileEntity(TileEntityDummyArray.class, Names.Blocks.DUMMY_ARRAY);
        GameRegistry.registerTileEntity(TileEntityTransmutationTablet.class, Names.Blocks.TRANSMUTATION_TABLET);
    }
}
