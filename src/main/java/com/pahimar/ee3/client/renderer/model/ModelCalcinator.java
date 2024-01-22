package com.pahimar.ee3.client.renderer.model;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import com.pahimar.ee3.reference.Models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCalcinator {

    private IModelCustom modelCalcinator;

    public ModelCalcinator() {
        modelCalcinator = AdvancedModelLoader.loadModel(Models.CALCINATOR);
    }

    public void render() {
        modelCalcinator.renderAll();
    }

    public void renderPart(String partName) {
        modelCalcinator.renderPart(partName);
    }
}
