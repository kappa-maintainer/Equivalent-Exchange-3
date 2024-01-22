package com.pahimar.ee3.util;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.pahimar.ee3.reference.Comparators;
import com.pahimar.ee3.reference.Names;

public class ItemStackUtils {

    public static ItemStack clone(ItemStack itemStack, int stackSize) {

        if (itemStack != null) {
            ItemStack clonedItemStack = itemStack.copy();
            clonedItemStack.stackSize = stackSize;
            return clonedItemStack;
        } else {
            return null;
        }
    }

    /**
     * Compares two ItemStacks for equality, testing itemID, metaData, stackSize, and their NBTTagCompounds (if they are
     * present)
     *
     * @param first  The first ItemStack being tested for equality
     * @param second The second ItemStack being tested for equality
     * @return true if the two ItemStacks are equivalent, false otherwise
     */
    public static boolean equals(ItemStack first, ItemStack second) {
        return (Comparators.ID_COMPARATOR.compare(first, second) == 0);
    }

    public static boolean equalsIgnoreStackSize(ItemStack itemStack1, ItemStack itemStack2) {
        return equals(clone(itemStack1, 1), clone(itemStack2, 1));
    }

    public static int compare(ItemStack itemStack1, ItemStack itemStack2) {
        return Comparators.ID_COMPARATOR.compare(itemStack1, itemStack2);
    }

    public static String toString(ItemStack itemStack) {

        if (itemStack != null) {
            if (itemStack.hasTagCompound()) {
                return String.format(
                    "%sxitemStack[%s@%s:%s]",
                    itemStack.stackSize,
                    itemStack.getUnlocalizedName(),
                    itemStack.getItemDamage(),
                    itemStack.getTagCompound());
            } else {
                return String.format(
                    "%sxitemStack[%s@%s]",
                    itemStack.stackSize,
                    itemStack.getUnlocalizedName(),
                    itemStack.getItemDamage());
            }
        }

        return "null";
    }

    public static void setOwner(ItemStack itemStack, EntityPlayer entityPlayer) {

        setOwnerName(itemStack, entityPlayer);
        setOwnerUUID(itemStack, entityPlayer);
    }

    public static String getOwnerName(ItemStack itemStack) {
        return NBTHelper.getString(itemStack, Names.NBT.OWNER);
    }

    public static UUID getOwnerUUID(ItemStack itemStack) {

        if (NBTHelper.getLong(itemStack, Names.NBT.OWNER_UUID_MOST_SIG) != null
            && NBTHelper.getLong(itemStack, Names.NBT.OWNER_UUID_LEAST_SIG) != null) {
            return new UUID(
                NBTHelper.getLong(itemStack, Names.NBT.OWNER_UUID_MOST_SIG),
                NBTHelper.getLong(itemStack, Names.NBT.OWNER_UUID_LEAST_SIG));
        }

        return null;
    }

    public static void setOwnerUUID(ItemStack itemStack, EntityPlayer entityPlayer) {

        NBTHelper.setLong(
            itemStack,
            Names.NBT.OWNER_UUID_MOST_SIG,
            entityPlayer.getUniqueID()
                .getMostSignificantBits());
        NBTHelper.setLong(
            itemStack,
            Names.NBT.OWNER_UUID_LEAST_SIG,
            entityPlayer.getUniqueID()
                .getLeastSignificantBits());
    }

    public static void setOwnerName(ItemStack itemStack, EntityPlayer entityPlayer) {
        NBTHelper.setString(itemStack, Names.NBT.OWNER, entityPlayer.getDisplayName());
    }
}
