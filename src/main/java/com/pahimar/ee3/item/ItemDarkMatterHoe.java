package com.pahimar.ee3.item;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

import com.pahimar.ee3.creativetab.CreativeTab;
import com.pahimar.ee3.reference.Key;
import com.pahimar.ee3.reference.Material;
import com.pahimar.ee3.reference.Names;
import com.pahimar.ee3.reference.Sounds;
import com.pahimar.ee3.reference.Textures;
import com.pahimar.ee3.reference.ToolMode;
import com.pahimar.ee3.util.CommonSoundHelper;
import com.pahimar.ee3.util.IChargeable;
import com.pahimar.ee3.util.IKeyBound;
import com.pahimar.ee3.util.IModalTool;
import com.pahimar.ee3.util.NBTHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDarkMatterHoe extends ItemHoe implements IKeyBound, IChargeable, IModalTool {

    public ItemDarkMatterHoe() {
        super(Material.Tools.DARK_MATTER);
        this.setCreativeTab(CreativeTab.EE3_TAB);
        this.setNoRepair();
        this.maxStackSize = 1;
        this.setUnlocalizedName(Names.Tools.DARK_MATTER_HOE);
    }

    @Override
    public String getUnlocalizedName() {
        return String
            .format("item.%s%s", Textures.RESOURCE_PREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String
            .format("item.%s%s", Textures.RESOURCE_PREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(
            this.getUnlocalizedName()
                .substring(
                    this.getUnlocalizedName()
                        .indexOf(".") + 1));
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Override
    public boolean showDurabilityBar(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IChargeable) {
            return ((IChargeable) itemStack.getItem()).getChargeLevel(itemStack) > 0;
        }

        return false;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IChargeable) {
            return (double) (((IChargeable) itemStack.getItem()).getMaxChargeLevel()
                - ((IChargeable) itemStack.getItem()).getChargeLevel(itemStack))
                / (double) ((IChargeable) itemStack.getItem()).getMaxChargeLevel();
        }

        return 1d;
    }

    @Override
    public short getMaxChargeLevel() {
        return 3;
    }

    @Override
    public short getChargeLevel(ItemStack itemStack) {
        if (NBTHelper.getShort(itemStack, Names.NBT.CHARGE_LEVEL) != null) {
            return NBTHelper.getShort(itemStack, Names.NBT.CHARGE_LEVEL);
        }

        return 0;
    }

    @Override
    public void setChargeLevel(ItemStack itemStack, short chargeLevel) {
        if (chargeLevel <= this.getMaxChargeLevel()) {
            NBTHelper.setShort(itemStack, Names.NBT.CHARGE_LEVEL, chargeLevel);
        }
    }

    @Override
    public void increaseChargeLevel(ItemStack itemStack) {
        if (getChargeLevel(itemStack) < this.getMaxChargeLevel()) {
            NBTHelper.setShort(itemStack, Names.NBT.CHARGE_LEVEL, (short) (getChargeLevel(itemStack) + 1));
        }
    }

    @Override
    public void decreaseChargeLevel(ItemStack itemStack) {
        if (getChargeLevel(itemStack) > 0) {
            NBTHelper.setShort(itemStack, Names.NBT.CHARGE_LEVEL, (short) (getChargeLevel(itemStack) - 1));
        }
    }

    @Override
    public void doKeyBindingAction(EntityPlayer entityPlayer, ItemStack itemStack, Key key) {
        if (key == Key.CHARGE) {
            if (!entityPlayer.isSneaking()) {
                if (getChargeLevel(itemStack) == this.getMaxChargeLevel()) {
                    CommonSoundHelper.playSoundAtPlayer(entityPlayer, Sounds.FAIL, 1.5f, 1.5f);
                } else {
                    increaseChargeLevel(itemStack);
                    CommonSoundHelper.playSoundAtPlayer(
                        entityPlayer,
                        Sounds.CHARGE_UP,
                        0.5F,
                        0.5F + 0.5F * (getChargeLevel(itemStack) * 1.0F / this.getMaxChargeLevel()));
                }
            } else {
                if (getChargeLevel(itemStack) == 0) {
                    CommonSoundHelper.playSoundAtPlayer(entityPlayer, Sounds.FAIL, 1.5f, 1.5f);
                } else {
                    decreaseChargeLevel(itemStack);
                    CommonSoundHelper.playSoundAtPlayer(
                        entityPlayer,
                        Sounds.CHARGE_DOWN,
                        0.5F,
                        1.0F - (0.5F - 0.5F * (getChargeLevel(itemStack) * 1.0F / this.getMaxChargeLevel())));
                }
            }
        } else if (key == Key.EXTRA) {
            CommonSoundHelper.playSoundAtPlayer(entityPlayer, Sounds.TOCK, 0.5f, 1.5F);
            changeToolMode(itemStack);
        }
    }

    @Override
    public List<ToolMode> getAvailableToolModes() {
        // TODO
        return Arrays.asList(ToolMode.STANDARD);
    }

    @Override
    public ToolMode getCurrentToolMode(ItemStack itemStack) {
        if (NBTHelper.getShort(itemStack, Names.NBT.MODE) != null
            && NBTHelper.getShort(itemStack, Names.NBT.MODE) < ToolMode.TYPES.length) {
            return ToolMode.TYPES[NBTHelper.getShort(itemStack, Names.NBT.MODE)];
        }

        return null;
    }

    @Override
    public void setToolMode(ItemStack itemStack, ToolMode toolMode) {
        NBTHelper.setShort(itemStack, Names.NBT.MODE, (short) toolMode.ordinal());
    }

    @Override
    public void changeToolMode(ItemStack itemStack) {
        ToolMode currentToolMode = getCurrentToolMode(itemStack);

        if (getAvailableToolModes().contains(currentToolMode)) {
            if (getAvailableToolModes().indexOf(currentToolMode) == getAvailableToolModes().size() - 1) {
                setToolMode(itemStack, getAvailableToolModes().get(0));
            } else {
                setToolMode(
                    itemStack,
                    getAvailableToolModes().get(getAvailableToolModes().indexOf(currentToolMode) + 1));
            }
        }
    }
}
