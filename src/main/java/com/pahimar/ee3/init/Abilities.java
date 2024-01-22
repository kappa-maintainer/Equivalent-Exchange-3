package com.pahimar.ee3.init;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.pahimar.ee3.api.knowledge.AbilityRegistryProxy;
import com.pahimar.ee3.exchange.OreStack;

public class Abilities {

    public static void init() {

        for (String oreName : OreDictionary.getOreNames()) {
            if (oreName.startsWith("ore")) {
                OreDictionary.getOres(oreName)
                    .forEach(AbilityRegistryProxy::innerSetAsNotLearnable);
                AbilityRegistryProxy.innerSetAsNotLearnable(new OreStack(oreName));
            }
        }

        AbilityRegistryProxy.innerSetAsNotLearnable(new ItemStack(Blocks.coal_ore));
        AbilityRegistryProxy.innerSetAsNotLearnable(ModItems.shardMinium);
        AbilityRegistryProxy.innerSetAsNotLearnable(new ItemStack(ModItems.alchemicalDust, 1, 1));
        AbilityRegistryProxy.innerSetAsNotLearnable(new ItemStack(ModItems.alchemicalDust, 1, 2));
    }
}
