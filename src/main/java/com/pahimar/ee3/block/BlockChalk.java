package com.pahimar.ee3.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import com.pahimar.ee3.init.ModItems;
import com.pahimar.ee3.reference.Names;

public class BlockChalk extends BlockEE {

    public BlockChalk() {
        super(Material.clay);
        this.setBlockName(Names.Items.CHALK);
        this.setHardness(0.6f);
        this.setStepSound(soundTypeStone);
    }

    @Override
    public Item getItemDropped(int par1, Random random, int par2) {
        return ModItems.chalk;
    }

    @Override
    public int quantityDropped(Random random) {
        return (random.nextInt(4) + 1);
    }
}
