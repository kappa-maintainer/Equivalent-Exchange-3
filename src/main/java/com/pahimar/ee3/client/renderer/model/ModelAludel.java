package com.pahimar.ee3.client.renderer.model;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import com.pahimar.ee3.reference.Models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelAludel {

    private IModelCustom modelAludel;

    public ModelAludel() {
        modelAludel = AdvancedModelLoader.loadModel(Models.ALUDEL);
    }

    public void render() {
        modelAludel.renderPart("Base");
    }
}
