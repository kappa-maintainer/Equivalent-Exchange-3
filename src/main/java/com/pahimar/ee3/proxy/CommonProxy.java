package com.pahimar.ee3.proxy;

import net.minecraftforge.common.MinecraftForge;

import com.pahimar.ee3.handler.ConfigurationHandler;
import com.pahimar.ee3.handler.CraftingHandler;
import com.pahimar.ee3.handler.ItemEventHandler;
import com.pahimar.ee3.handler.PlayerEventHandler;
import com.pahimar.ee3.handler.WorldEventHandler;

import cpw.mods.fml.common.FMLCommonHandler;

public abstract class CommonProxy implements IProxy {

    public void registerEventHandlers() {
        ItemEventHandler itemEventHandler = new ItemEventHandler();
        CraftingHandler craftingHandler = new CraftingHandler();
        PlayerEventHandler playerEventHandler = new PlayerEventHandler();

        FMLCommonHandler.instance()
            .bus()
            .register(new ConfigurationHandler());
        FMLCommonHandler.instance()
            .bus()
            .register(itemEventHandler);
        MinecraftForge.EVENT_BUS.register(itemEventHandler);
        MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
        MinecraftForge.EVENT_BUS.register(playerEventHandler);
        FMLCommonHandler.instance()
            .bus()
            .register(playerEventHandler);
        FMLCommonHandler.instance()
            .bus()
            .register(craftingHandler);
        MinecraftForge.EVENT_BUS.register(craftingHandler);
    }
}
