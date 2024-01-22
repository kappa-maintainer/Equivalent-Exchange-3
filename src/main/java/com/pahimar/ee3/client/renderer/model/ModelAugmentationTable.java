package com.pahimar.ee3.client.renderer.model;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import com.pahimar.ee3.reference.Models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelAugmentationTable {

    private IModelCustom modelAugmentationTable;

    public ModelAugmentationTable() {
        modelAugmentationTable = AdvancedModelLoader.loadModel(Models.AUGMENTATION_TABLE);
    }

    public void render() {
        modelAugmentationTable.renderAll();
    }
}
