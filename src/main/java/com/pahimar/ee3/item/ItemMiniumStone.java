package com.pahimar.ee3.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.pahimar.ee3.reference.Key;
import com.pahimar.ee3.reference.Names;
import com.pahimar.ee3.util.IKeyBound;
import com.pahimar.ee3.util.LogHelper;
import com.pahimar.ee3.util.NBTHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMiniumStone extends ItemEE implements IKeyBound {

    public ItemMiniumStone() {
        super();
        this.setUnlocalizedName(Names.Items.MINIUM_STONE);
        this.setMaxDamage(1000); // TODO Get this from configs
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack copiedStack = itemStack.copy();

        copiedStack.setItemDamage(copiedStack.getItemDamage() + 1);
        copiedStack.stackSize = 1;

        return copiedStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack, int renderPass) {
        return NBTHelper.hasKey(itemStack, Names.NBT.CRAFTING_GUI_OPEN)
            || NBTHelper.hasKey(itemStack, Names.NBT.TRANSMUTATION_GUI_OPEN);
    }

    @Override
    public void doKeyBindingAction(EntityPlayer entityPlayer, ItemStack itemStack, Key key) {
        LogHelper.info("{} {} {}", entityPlayer.toString(), itemStack.toString(), key.toString());
    }
}
