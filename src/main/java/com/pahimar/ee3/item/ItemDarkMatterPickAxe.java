package com.pahimar.ee3.item;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.pahimar.ee3.creativetab.CreativeTab;
import com.pahimar.ee3.reference.Key;
import com.pahimar.ee3.reference.Material;
import com.pahimar.ee3.reference.Names;
import com.pahimar.ee3.reference.Sounds;
import com.pahimar.ee3.reference.ToolMode;
import com.pahimar.ee3.util.CommonSoundHelper;
import com.pahimar.ee3.util.IChargeable;
import com.pahimar.ee3.util.IKeyBound;
import com.pahimar.ee3.util.LogHelper;
import com.pahimar.ee3.util.NBTHelper;

public class ItemDarkMatterPickAxe extends ItemToolModalEE implements IKeyBound, IChargeable {

    private static final Set blocksEffectiveAgainst = Sets.newHashSet(
        new Block[] { Blocks.cobblestone, Blocks.double_stone_slab, Blocks.stone_slab, Blocks.stone, Blocks.sandstone,
            Blocks.mossy_cobblestone, Blocks.iron_ore, Blocks.iron_block, Blocks.coal_ore, Blocks.gold_block,
            Blocks.gold_ore, Blocks.diamond_ore, Blocks.diamond_block, Blocks.ice, Blocks.netherrack, Blocks.lapis_ore,
            Blocks.lapis_block, Blocks.redstone_ore, Blocks.lit_redstone_ore, Blocks.rail, Blocks.detector_rail,
            Blocks.golden_rail, Blocks.activator_rail });

    public ItemDarkMatterPickAxe() {
        super(2f, Material.Tools.DARK_MATTER, blocksEffectiveAgainst);
        this.setCreativeTab(CreativeTab.EE3_TAB);
        this.setNoRepair();
        this.setUnlocalizedName(Names.Tools.DARK_MATTER_PICKAXE);
    }

    @Override
    public boolean func_150897_b(Block block) {
        return block
            == Blocks.obsidian
                ? this.toolMaterial.getHarvestLevel() == 3
                : (block != Blocks.diamond_block && block != Blocks.diamond_ore
                    ? (block != Blocks.emerald_ore
                        && block != Blocks.emerald_block
                            ? (block != Blocks.gold_block && block != Blocks.gold_ore
                                ? (block != Blocks.iron_block && block != Blocks.iron_ore
                                    ? (block != Blocks.lapis_block && block != Blocks.lapis_ore
                                        ? (block != Blocks.redstone_ore && block != Blocks.lit_redstone_ore
                                            ? (block.getMaterial() == net.minecraft.block.material.Material.rock
                                                || (block.getMaterial() == net.minecraft.block.material.Material.iron
                                                    || block.getMaterial()
                                                        == net.minecraft.block.material.Material.anvil))
                                            : this.toolMaterial.getHarvestLevel() >= 2)
                                        : this.toolMaterial.getHarvestLevel() >= 1)
                                    : this.toolMaterial.getHarvestLevel() >= 1)
                                : this.toolMaterial.getHarvestLevel() >= 2)
                            : this.toolMaterial.getHarvestLevel() >= 2)
                    : this.toolMaterial.getHarvestLevel() >= 2);
    }

    @Override
    public float func_150893_a(ItemStack itemStack, Block block) {
        return block.getMaterial() != net.minecraft.block.material.Material.iron
            && block.getMaterial() != net.minecraft.block.material.Material.anvil
            && block.getMaterial() != net.minecraft.block.material.Material.rock ? super.func_150893_a(itemStack, block)
                : this.efficiencyOnProperMaterial;
    }

    @Override
    public Set<String> getToolClasses(ItemStack itemStack) {
        return ImmutableSet.of("pickaxe");
    }

    @Override
    public float getDigSpeed(ItemStack itemStack, Block block, int meta) {
        if ((ForgeHooks.isToolEffective(itemStack, block, meta) || block == Blocks.obsidian
            || block == Blocks.redstone_ore
            || block == Blocks.lit_redstone_ore) && (itemStack.getItem() instanceof IChargeable)) {
            return super.getDigSpeed(itemStack, block, meta)
                + (((IChargeable) itemStack.getItem()).getChargeLevel(itemStack) * 12f);
        }

        return super.getDigSpeed(itemStack, block, meta);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            // TODO
            LogHelper.info("Right click with the Dark Matter Pickaxe");
        }

        return false;
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
        return Arrays.asList(ToolMode.STANDARD, ToolMode.WIDE, ToolMode.TALL);
    }
}
