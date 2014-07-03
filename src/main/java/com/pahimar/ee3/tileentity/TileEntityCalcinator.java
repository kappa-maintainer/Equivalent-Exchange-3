package com.pahimar.ee3.tileentity;

import com.pahimar.ee3.network.PacketHandler;
import com.pahimar.ee3.network.message.MessageTileCalcinator;
import com.pahimar.ee3.reference.Names;
import com.pahimar.ee3.util.CalcinationHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityCalcinator extends TileEntityEE implements ISidedInventory {
    public static final int INVENTORY_SIZE = 4;
    public static final int FUEL_INVENTORY_INDEX = 0;
    public static final int INPUT_INVENTORY_INDEX = 1;
    public static final int OUTPUT_LEFT_INVENTORY_INDEX = 2;
    public static final int OUTPUT_RIGHT_INVENTORY_INDEX = 3;
    private static final int DEFAULT_ITEM_SUCK_COOL_DOWN = 20;
    public int deviceCookTime;              // How much longer the Calcinator will cook
    public int fuelBurnTime;                // The fuel value for the currently burning fuel
    public int itemCookTime;                // How long the current item has been "cooking"

    public byte leftStackSize, leftStackMeta, rightStackSize, rightStackMeta;

    public int itemSuckCoolDown = 0;
    /**
     * The ItemStacks that hold the items currently being used in the Calcinator
     */
    private ItemStack[] inventory;

    public TileEntityCalcinator() {
        inventory = new ItemStack[INVENTORY_SIZE];
    }

    public static boolean suckInItems(TileEntityCalcinator tileEntityCalcinator) {
        EntityItem entityitem = TileEntityHopper.func_145897_a(tileEntityCalcinator.getWorldObj(), tileEntityCalcinator.xCoord, tileEntityCalcinator.yCoord + 1.0D, tileEntityCalcinator.zCoord);

        return entityitem != null && TileEntityHopper.func_145898_a(tileEntityCalcinator, entityitem);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return side == ForgeDirection.DOWN.ordinal() ? new int[]{FUEL_INVENTORY_INDEX, OUTPUT_LEFT_INVENTORY_INDEX, OUTPUT_RIGHT_INVENTORY_INDEX} : new int[]{INPUT_INVENTORY_INDEX, OUTPUT_LEFT_INVENTORY_INDEX, OUTPUT_RIGHT_INVENTORY_INDEX};
    }

    @Override
    public boolean canInsertItem(int slotIndex, ItemStack itemStack, int side) {
        return isItemValidForSlot(slotIndex, itemStack);
    }

    @Override
    public boolean canExtractItem(int slotIndex, ItemStack itemStack, int side) {
        return slotIndex == OUTPUT_LEFT_INVENTORY_INDEX || slotIndex == OUTPUT_RIGHT_INVENTORY_INDEX;
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex) {
        sendDustPileData();
        return inventory[slotIndex];
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int decrementAmount) {
        ItemStack itemStack = getStackInSlot(slotIndex);
        if (itemStack != null) {
            if (itemStack.stackSize <= decrementAmount) {
                setInventorySlotContents(slotIndex, null);
            } else {
                itemStack = itemStack.splitStack(decrementAmount);
                if (itemStack.stackSize == 0) {
                    setInventorySlotContents(slotIndex, null);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotIndex) {
        ItemStack itemStack = getStackInSlot(slotIndex);
        if (itemStack != null) {
            setInventorySlotContents(slotIndex, null);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack) {
        inventory[slotIndex] = itemStack;
        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
            itemStack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() {
        return this.hasCustomName() ? this.getCustomName() : Names.Containers.CALCINATOR_NAME;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void openInventory() {
        // NOOP
    }

    @Override
    public void closeInventory() {
        // NOOP
    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {
        return false;
    }

    @Override
    public boolean receiveClientEvent(int eventId, int eventData) {
        if (eventId == 1) {
            this.state = (byte) eventData;
            // NAME UPDATE
            // this.worldObj.updateAllLightTypes(this.xCoord, this.yCoord, this.zCoord);
            this.worldObj.func_147451_t(this.xCoord, this.yCoord, this.zCoord);
            return true;
        } else if (eventId == 2) {
            this.leftStackSize = (byte) eventData;
            return true;
        } else if (eventId == 3) {
            this.leftStackMeta = (byte) eventData;
            return true;
        } else if (eventId == 4) {
            this.rightStackSize = (byte) eventData;
            return true;
        } else if (eventId == 5) {
            this.rightStackMeta = (byte) eventData;
            return true;
        } else {
            return super.receiveClientEvent(eventId, eventData);
        }
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scale) {
        return this.itemCookTime * scale / 200;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int scale) {
        if (this.fuelBurnTime > 0) {
            return this.deviceCookTime * scale / this.fuelBurnTime;
        }

        return 0;
    }

    @Override
    public Packet getDescriptionPacket() {
        return PacketHandler.INSTANCE.getPacketFrom(new MessageTileCalcinator(this));
    }

    @Override
    public void updateEntity() {
        boolean isBurning = this.deviceCookTime > 0;
        boolean sendUpdate = false;

        // If the Calcinator still has burn time, decrement it
        if (this.deviceCookTime > 0) {
            this.deviceCookTime--;
        }

        if (!this.worldObj.isRemote) {
            // Start "cooking" a new item, if we can
            if (this.deviceCookTime == 0 && this.canCalcinate()) {
                this.fuelBurnTime = this.deviceCookTime = TileEntityFurnace.getItemBurnTime(this.inventory[FUEL_INVENTORY_INDEX]);

                if (this.deviceCookTime > 0) {
                    sendUpdate = true;

                    if (this.inventory[FUEL_INVENTORY_INDEX] != null) {
                        --this.inventory[FUEL_INVENTORY_INDEX].stackSize;

                        if (this.inventory[FUEL_INVENTORY_INDEX].stackSize == 0) {
                            this.inventory[FUEL_INVENTORY_INDEX] = this.inventory[FUEL_INVENTORY_INDEX].getItem().getContainerItem(inventory[FUEL_INVENTORY_INDEX]);
                        }
                    }
                }
            }

            // Continue "cooking" the same item, if we can
            if (this.deviceCookTime > 0 && this.canCalcinate()) {
                this.itemCookTime++;

                if (this.itemCookTime == 200) {
                    this.itemCookTime = 0;
                    this.calcinateItem();
                    sendUpdate = true;
                }
            } else {
                this.itemCookTime = 0;
            }

            // If the state has changed, catch that something changed
            if (isBurning != this.deviceCookTime > 0) {
                sendUpdate = true;
            }

            //Item sucking
            if (this.itemSuckCoolDown > 0) {
                itemSuckCoolDown--;
            } else {
                if (suckInItems(this)) {
                    markDirty();
                }
                itemSuckCoolDown = DEFAULT_ITEM_SUCK_COOL_DOWN;
            }
        }

        if (sendUpdate) {
            this.markDirty();
            this.state = this.deviceCookTime > 0 ? (byte) 1 : (byte) 0;
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.state);
            sendDustPileData();
            this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        }
    }

    public void calcinateItem() {
        if (this.canCalcinate()) {
            ItemStack alchemicalDustStack = CalcinationHelper.getCalcinationResult(this.inventory[INPUT_INVENTORY_INDEX]);
            addItemStackToOutput(alchemicalDustStack.copy());

            this.inventory[INPUT_INVENTORY_INDEX].stackSize--;

            if (this.inventory[INPUT_INVENTORY_INDEX].stackSize <= 0) {
                this.inventory[INPUT_INVENTORY_INDEX] = null;
            }
        }
    }

    private boolean canCalcinate() {
        if (inventory[INPUT_INVENTORY_INDEX] == null) {
            return false;
        } else {
            ItemStack alchemicalDustStack = CalcinationHelper.getCalcinationResult(this.inventory[INPUT_INVENTORY_INDEX]);

            /**
             * If we don't get a calcination result, then return false
             */
            if (alchemicalDustStack == null) {
                return false;
            }

            /**
             * If either slot is empty, return true (we have a valid calcination result
             */
            if (this.inventory[OUTPUT_LEFT_INVENTORY_INDEX] == null || this.inventory[OUTPUT_RIGHT_INVENTORY_INDEX] == null) {
                return true;
            }

            boolean leftEquals = this.inventory[OUTPUT_LEFT_INVENTORY_INDEX].isItemEqual(alchemicalDustStack);
            int leftResult = this.inventory[OUTPUT_LEFT_INVENTORY_INDEX].stackSize + alchemicalDustStack.stackSize;

            boolean rightEquals = this.inventory[OUTPUT_RIGHT_INVENTORY_INDEX].isItemEqual(alchemicalDustStack);
            int rightResult = this.inventory[OUTPUT_RIGHT_INVENTORY_INDEX].stackSize + alchemicalDustStack.stackSize;

            if (!leftEquals && !rightEquals) {
                return false;
            } else if (leftEquals && !rightEquals) {
                return leftResult <= getInventoryStackLimit() && leftResult <= alchemicalDustStack.getMaxStackSize();
            } else if (!leftEquals) {
                return rightResult <= getInventoryStackLimit() && rightResult <= alchemicalDustStack.getMaxStackSize();
            } else {
                return leftResult <= getInventoryStackLimit() && leftResult <= alchemicalDustStack.getMaxStackSize() || rightResult <= getInventoryStackLimit() && rightResult <= alchemicalDustStack.getMaxStackSize();
            }
        }
    }

    private void addItemStackToOutput(ItemStack alchemicalDustStack) {
        int maxStackSize = Math.min(getInventoryStackLimit(), alchemicalDustStack.getMaxStackSize());

        if (this.inventory[OUTPUT_LEFT_INVENTORY_INDEX] == null) {
            this.inventory[OUTPUT_LEFT_INVENTORY_INDEX] = alchemicalDustStack;
            return;
        }
        if (this.inventory[OUTPUT_LEFT_INVENTORY_INDEX].isItemEqual(alchemicalDustStack)
                && this.inventory[OUTPUT_LEFT_INVENTORY_INDEX].stackSize < maxStackSize) {
            int addedSize = Math.min(alchemicalDustStack.stackSize, maxStackSize - this.inventory[OUTPUT_LEFT_INVENTORY_INDEX].stackSize);
            alchemicalDustStack.stackSize -= addedSize;
            this.inventory[OUTPUT_LEFT_INVENTORY_INDEX].stackSize += addedSize;
            if (alchemicalDustStack == null || alchemicalDustStack.stackSize == 0) {
                return;
            }
        }
        if (this.inventory[OUTPUT_RIGHT_INVENTORY_INDEX] == null) {
            this.inventory[OUTPUT_RIGHT_INVENTORY_INDEX] = alchemicalDustStack;
            return;
        }
        if (this.inventory[OUTPUT_RIGHT_INVENTORY_INDEX].isItemEqual(alchemicalDustStack)
                && this.inventory[OUTPUT_RIGHT_INVENTORY_INDEX].stackSize < maxStackSize) {
            int addedSize = Math.min(alchemicalDustStack.stackSize, maxStackSize - this.inventory[OUTPUT_RIGHT_INVENTORY_INDEX].stackSize);
            alchemicalDustStack.stackSize -= addedSize;
            this.inventory[OUTPUT_RIGHT_INVENTORY_INDEX].stackSize += addedSize;
        }
    }

    private int getLeftStackSize() {
        if (this.inventory[OUTPUT_LEFT_INVENTORY_INDEX] != null) {
            return this.inventory[OUTPUT_LEFT_INVENTORY_INDEX].stackSize;
        }

        return 0;
    }

    private int getLeftStackMeta() {
        if (this.inventory[OUTPUT_LEFT_INVENTORY_INDEX] != null) {
            return this.inventory[OUTPUT_LEFT_INVENTORY_INDEX].getItemDamage();
        }

        return 0;
    }

    private int getRightStackSize() {
        if (this.inventory[OUTPUT_RIGHT_INVENTORY_INDEX] != null) {
            return this.inventory[OUTPUT_RIGHT_INVENTORY_INDEX].stackSize;
        }

        return 0;
    }

    private int getRightStackMeta() {
        if (this.inventory[OUTPUT_RIGHT_INVENTORY_INDEX] != null) {
            return this.inventory[OUTPUT_RIGHT_INVENTORY_INDEX].getItemDamage();
        }

        return 0;
    }

    private void sendDustPileData() {
        if (this.getBlockType() != null) {
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 2, getLeftStackSize());
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 3, getLeftStackMeta());
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 4, getRightStackSize());
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 5, getRightStackMeta());
        }
    }
}
