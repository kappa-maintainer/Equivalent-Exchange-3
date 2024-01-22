package com.pahimar.ee3.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;

import com.pahimar.ee3.api.blacklist.BlacklistRegistryProxy;
import com.pahimar.ee3.reference.Messages;
import com.pahimar.ee3.reference.Names;

public class CommandSetCurrentItemLearnable extends CommandBase {

    @Override
    public String getCommandName() {
        return Names.Commands.SET_CURRENT_ITEM_LEARNABLE;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender commandSender) {
        return Messages.Commands.SET_CURRENT_ITEM_LEARNABLE_USAGE;
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args) {

        ItemStack itemStack = ((EntityPlayer) commandSender).getCurrentEquippedItem();

        if (itemStack != null) {
            if (!BlacklistRegistryProxy.isLearnable(itemStack)) {
                BlacklistRegistryProxy.setAsLearnable(itemStack);
                func_152373_a(
                    commandSender,
                    this,
                    Messages.Commands.SET_CURRENT_ITEM_LEARNABLE_SUCCESS,
                    new Object[] { commandSender.getCommandSenderName(), itemStack.func_151000_E() });
            } else {
                commandSender.addChatMessage(
                    new ChatComponentTranslation(Messages.Commands.SET_CURRENT_ITEM_LEARNABLE_NO_EFFECT, itemStack));
            }
        } else {
            throw new WrongUsageException(Messages.Commands.NO_ITEM);
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender commandSender, String[] args) {
        return null;
    }
}
