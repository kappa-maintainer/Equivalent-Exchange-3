package com.pahimar.ee3.inventory;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pahimar.ee3.item.ItemAlchemicalBag;
import com.pahimar.ee3.reference.Names;
import com.pahimar.ee3.util.NBTHelper;

import cpw.mods.fml.common.FMLCommonHandler;

public class ContainerAlchemicalBag extends ContainerEE {

    // Small Bag
    public static final int SMALL_BAG_INVENTORY_ROWS = 4;
    public static final int SMALL_BAG_INVENTORY_COLUMNS = 12;
    // Medium Bag
    public static final int MEDIUM_BAG_INVENTORY_ROWS = 7;
    public static final int MEDIUM_BAG_INVENTORY_COLUMNS = 12;
    // Large Bag
    public static final int LARGE_BAG_INVENTORY_ROWS = 9;
    public static final int LARGE_BAG_INVENTORY_COLUMNS = 13;
    private final EntityPlayer entityPlayer;
    public final InventoryAlchemicalBag inventoryAlchemicalBag;

    private int bagInventoryRows;
    private int bagInventoryColumns;

    public ContainerAlchemicalBag(EntityPlayer entityPlayer, InventoryAlchemicalBag inventoryAlchemicalBag) {
        this.entityPlayer = entityPlayer;
        this.inventoryAlchemicalBag = inventoryAlchemicalBag;

        if (inventoryAlchemicalBag.parentItemStack.getItemDamage() == 0) {
            bagInventoryRows = SMALL_BAG_INVENTORY_ROWS;
            bagInventoryColumns = SMALL_BAG_INVENTORY_COLUMNS;
        } else if (inventoryAlchemicalBag.parentItemStack.getItemDamage() == 1) {
            bagInventoryRows = MEDIUM_BAG_INVENTORY_ROWS;
            bagInventoryColumns = MEDIUM_BAG_INVENTORY_COLUMNS;
        } else if (inventoryAlchemicalBag.parentItemStack.getItemDamage() == 2) {
            bagInventoryRows = LARGE_BAG_INVENTORY_ROWS;
            bagInventoryColumns = LARGE_BAG_INVENTORY_COLUMNS;
        }

        // Add the Alchemical Chest slots to the container
        for (int bagRowIndex = 0; bagRowIndex < bagInventoryRows; ++bagRowIndex) {
            for (int bagColumnIndex = 0; bagColumnIndex < bagInventoryColumns; ++bagColumnIndex) {
                if (inventoryAlchemicalBag.parentItemStack.getItemDamage() == 0) {
                    this.addSlotToContainer(
                        new SlotAlchemicalBag(
                            this,
                            inventoryAlchemicalBag,
                            entityPlayer,
                            bagColumnIndex + bagRowIndex * bagInventoryColumns,
                            8 + bagColumnIndex * 18,
                            18 + bagRowIndex * 18));
                } else if (inventoryAlchemicalBag.parentItemStack.getItemDamage() == 1) {
                    this.addSlotToContainer(
                        new SlotAlchemicalBag(
                            this,
                            inventoryAlchemicalBag,
                            entityPlayer,
                            bagColumnIndex + bagRowIndex * bagInventoryColumns,
                            8 + bagColumnIndex * 18,
                            18 + bagRowIndex * 18));
                } else if (inventoryAlchemicalBag.parentItemStack.getItemDamage() == 2) {
                    this.addSlotToContainer(
                        new SlotAlchemicalBag(
                            this,
                            inventoryAlchemicalBag,
                            entityPlayer,
                            bagColumnIndex + bagRowIndex * bagInventoryColumns,
                            8 + bagColumnIndex * 18,
                            8 + bagRowIndex * 18));
                }
            }
        }

        // Add the player's inventory slots to the container
        for (int inventoryRowIndex = 0; inventoryRowIndex < PLAYER_INVENTORY_ROWS; ++inventoryRowIndex) {
            for (int inventoryColumnIndex = 0; inventoryColumnIndex
                < PLAYER_INVENTORY_COLUMNS; ++inventoryColumnIndex) {
                if (inventoryAlchemicalBag.parentItemStack.getItemDamage() == 0) {
                    this.addSlotToContainer(
                        new Slot(
                            entityPlayer.inventory,
                            inventoryColumnIndex + inventoryRowIndex * 9 + 9,
                            35 + inventoryColumnIndex * 18,
                            104 + inventoryRowIndex * 18));
                } else if (inventoryAlchemicalBag.parentItemStack.getItemDamage() == 1) {
                    this.addSlotToContainer(
                        new Slot(
                            entityPlayer.inventory,
                            inventoryColumnIndex + inventoryRowIndex * 9 + 9,
                            35 + inventoryColumnIndex * 18,
                            158 + inventoryRowIndex * 18));
                } else if (inventoryAlchemicalBag.parentItemStack.getItemDamage() == 2) {
                    this.addSlotToContainer(
                        new Slot(
                            entityPlayer.inventory,
                            inventoryColumnIndex + inventoryRowIndex * 9 + 9,
                            44 + inventoryColumnIndex * 18,
                            174 + inventoryRowIndex * 18));
                }
            }
        }

        // Add the player's action bar slots to the container
        for (int actionBarSlotIndex = 0; actionBarSlotIndex < PLAYER_INVENTORY_COLUMNS; ++actionBarSlotIndex) {
            if (inventoryAlchemicalBag.parentItemStack.getItemDamage() == 0) {
                if (!(entityPlayer.inventory.getStackInSlot(actionBarSlotIndex)
                    == inventoryAlchemicalBag.parentItemStack)) {
                    this.addSlotToContainer(
                        new Slot(entityPlayer.inventory, actionBarSlotIndex, 35 + actionBarSlotIndex * 18, 162));
                } else {
                    this.addSlotToContainer(
                        new Slot(entityPlayer.inventory, actionBarSlotIndex, 35 + actionBarSlotIndex * 18, 162) {

                            public boolean canTakeStack(EntityPlayer p_82869_1_) {
                                return false;
                            }
                        });
                }
            } else if (inventoryAlchemicalBag.parentItemStack.getItemDamage() == 1) {
                if (!(entityPlayer.inventory.getStackInSlot(actionBarSlotIndex)
                    == inventoryAlchemicalBag.parentItemStack)) {
                    this.addSlotToContainer(
                        new Slot(entityPlayer.inventory, actionBarSlotIndex, 35 + actionBarSlotIndex * 18, 216));
                } else {
                    this.addSlotToContainer(
                        new Slot(entityPlayer.inventory, actionBarSlotIndex, 35 + actionBarSlotIndex * 18, 216) {

                            public boolean canTakeStack(EntityPlayer p_82869_1_) {
                                return false;
                            }
                        });
                }
            } else if (inventoryAlchemicalBag.parentItemStack.getItemDamage() == 2) {
                if (!(entityPlayer.inventory.getStackInSlot(actionBarSlotIndex)
                    == inventoryAlchemicalBag.parentItemStack)) {
                    this.addSlotToContainer(
                        new Slot(entityPlayer.inventory, actionBarSlotIndex, 44 + actionBarSlotIndex * 18, 232));
                } else {
                    this.addSlotToContainer(
                        new Slot(entityPlayer.inventory, actionBarSlotIndex, 44 + actionBarSlotIndex * 18, 232) {

                            public boolean canTakeStack(EntityPlayer p_82869_1_) {
                                return false;
                            }
                        });
                }
            }
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);

        if (!entityPlayer.worldObj.isRemote) {
            // We can probably do this better now considering the InventoryAlchemicalBag has a findParent method
            InventoryPlayer invPlayer = entityPlayer.inventory;
            for (ItemStack itemStack : invPlayer.mainInventory) {
                if (itemStack != null) {
                    if (NBTHelper.hasKey(itemStack, Names.NBT.ALCHEMICAL_BAG_GUI_OPEN)) {
                        NBTHelper.removeTag(itemStack, Names.NBT.ALCHEMICAL_BAG_GUI_OPEN);
                    }
                }
            }

            saveInventory(entityPlayer);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }

    public boolean isItemStackParent(ItemStack itemStack) {
        if (NBTHelper.hasUUID(itemStack)) {
            UUID stackUUID = new UUID(
                itemStack.getTagCompound()
                    .getLong(Names.NBT.UUID_MOST_SIG),
                itemStack.getTagCompound()
                    .getLong(Names.NBT.UUID_LEAST_SIG));
            return inventoryAlchemicalBag.matchesUUID(stackUUID);
        }
        return false;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotIndex) {
        ItemStack newItemStack = null;
        Slot slot = (Slot) inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();
            newItemStack = itemStack.copy();

            // Attempt to shift click something from the bag inventory into the player inventory
            if (slotIndex < bagInventoryRows * bagInventoryColumns) {
                if (!this
                    .mergeItemStack(itemStack, bagInventoryRows * bagInventoryColumns, inventorySlots.size(), false)) {
                    return null;
                }
            }
            // Special case if we are dealing with an Alchemical Bag being shift clicked
            else if (itemStack.getItem() instanceof ItemAlchemicalBag) {
                // Attempt to shift click a bag from the player inventory into the hot bar inventory
                if (slotIndex
                    < (bagInventoryRows * bagInventoryColumns) + (PLAYER_INVENTORY_ROWS * PLAYER_INVENTORY_COLUMNS)) {
                    if (!this.mergeItemStack(
                        itemStack,
                        (bagInventoryRows * bagInventoryColumns) + (PLAYER_INVENTORY_ROWS * PLAYER_INVENTORY_COLUMNS),
                        inventorySlots.size(),
                        false)) {
                        return null;
                    }
                }
                // Attempt to shift click a bag from the hot bar inventory into the player inventory
                else if (!this.mergeItemStack(
                    itemStack,
                    bagInventoryRows * bagInventoryColumns,
                    (bagInventoryRows * bagInventoryColumns) + (PLAYER_INVENTORY_ROWS * PLAYER_INVENTORY_COLUMNS),
                    false)) {
                        return null;
                    }
            }
            // Attempt to shift click a non-Alchemical Bag into the bag inventory
            else if (!this.mergeItemStack(itemStack, 0, bagInventoryRows * bagInventoryColumns, false)) {
                return null;
            }

            if (itemStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }

        return newItemStack;
    }

    public void saveInventory(EntityPlayer entityPlayer) {
        inventoryAlchemicalBag.onGuiSaved(entityPlayer);
    }

    private class SlotAlchemicalBag extends Slot {

        private final EntityPlayer entityPlayer;
        private ContainerAlchemicalBag containerAlchemicalBag;

        public SlotAlchemicalBag(ContainerAlchemicalBag containerAlchemicalBag, IInventory inventory,
            EntityPlayer entityPlayer, int slotIndex, int x, int y) {
            super(inventory, slotIndex, x, y);
            this.entityPlayer = entityPlayer;
            this.containerAlchemicalBag = containerAlchemicalBag;
        }

        @Override
        public void onSlotChanged() {
            super.onSlotChanged();

            if (FMLCommonHandler.instance()
                .getEffectiveSide()
                .isServer()) {
                containerAlchemicalBag.saveInventory(entityPlayer);
            }
        }

        /**
         * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
         */
        @Override
        public boolean isItemValid(ItemStack itemStack) {
            return !(itemStack.getItem() instanceof ItemAlchemicalBag);
        }
    }
}
