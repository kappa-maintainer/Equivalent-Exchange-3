package com.pahimar.ee3.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.pahimar.ee3.EquivalentExchange3;
import com.pahimar.ee3.reference.GUIs;
import com.pahimar.ee3.reference.Names;
import com.pahimar.ee3.reference.RenderIds;
import com.pahimar.ee3.tileentity.TileEntityAludel;
import com.pahimar.ee3.tileentity.TileEntityEE;
import com.pahimar.ee3.tileentity.TileEntityGlassBell;

public class BlockGlassBell extends BlockTileEntityEE {

    public BlockGlassBell() {
        super(Material.glass);
        this.setHardness(1.0f);
        this.setBlockName(Names.Blocks.GLASS_BELL);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metaData) {
        return new TileEntityGlassBell();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return RenderIds.glassBell;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityGlassBell) {
            TileEntityGlassBell tileEntityGlassBell = (TileEntityGlassBell) world.getTileEntity(x, y, z);

            return tileEntityGlassBell.getState();
        }

        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
        float par8, float par9) {
        if (player.isSneaking()) {
            return false;
        } else {
            if (!world.isRemote) {
                if (world.getTileEntity(x, y, z) instanceof TileEntityGlassBell) {
                    if (world.getTileEntity(x, y - 1, z) instanceof TileEntityAludel) {
                        player.openGui(EquivalentExchange3.instance, GUIs.ALUDEL.ordinal(), world, x, y - 1, z);
                    } else {
                        player.openGui(EquivalentExchange3.instance, GUIs.GLASS_BELL.ordinal(), world, x, y, z);
                    }
                }
            }

            return true;
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        if (itemStack.hasDisplayName()) {
            ((TileEntityEE) world.getTileEntity(x, y, z)).setCustomName(itemStack.getDisplayName());
        }

        if (world.getTileEntity(x, y - 1, z) != null && world.getTileEntity(x, y - 1, z) instanceof TileEntityAludel) {
            ((TileEntityEE) world.getTileEntity(x, y, z)).setOrientation(ForgeDirection.UP);
        } else {
            ((TileEntityEE) world.getTileEntity(x, y, z)).setOrientation(world.getBlockMetadata(x, y, z));
        }

        world.setBlockMetadataWithNotify(x, y, z, 0, 3);
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int sideHit, float hitX, float hitY, float hitZ,
        int metaData) {
        return sideHit;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, int x, int y, int z) {
        if (iBlockAccess.getTileEntity(x, y, z) instanceof TileEntityGlassBell) {
            TileEntityGlassBell tileGlassBell = (TileEntityGlassBell) iBlockAccess.getTileEntity(x, y, z);

            switch (tileGlassBell.getOrientation()) {
                case DOWN: {
                    this.setBlockBounds(0.125F, 0.33F, 0.125F, 0.875F, 1.0F, 0.875F);
                    break;
                }
                case UP: {
                    this.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.66F, 0.875F);
                    break;
                }
                case NORTH: {
                    this.setBlockBounds(0.125F, 0.125F, 0.33F, 0.875F, 0.875F, 1.0F);
                    break;
                }
                case SOUTH: {
                    this.setBlockBounds(0.125F, 0.125F, 0.0F, 0.875F, 0.875F, 0.66F);
                    break;
                }
                case EAST: {
                    this.setBlockBounds(0.0F, 0.125F, 0.125F, 0.66F, 0.875F, 0.875F);
                    break;
                }
                case WEST: {
                    this.setBlockBounds(0.33F, 0.125F, 0.125F, 1.0F, 0.875F, 0.875F);
                    break;
                }
                case UNKNOWN: {
                    break;
                }
            }
        }
    }
}
