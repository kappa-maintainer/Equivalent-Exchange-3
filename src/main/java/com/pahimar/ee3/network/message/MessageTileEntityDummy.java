package com.pahimar.ee3.network.message;

import java.util.UUID;

import net.minecraft.tileentity.TileEntity;

import com.pahimar.ee3.tileentity.TileEntityDummyArray;
import com.pahimar.ee3.tileentity.TileEntityEE;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageTileEntityDummy implements IMessage, IMessageHandler<MessageTileEntityDummy, IMessage> {

    public int x, y, z;
    public byte orientation;
    public byte state;
    public String customName;
    public UUID ownerUUID;
    public int trueXCoord, trueYCoord, trueZCoord;

    public MessageTileEntityDummy() {

    }

    public MessageTileEntityDummy(TileEntityDummyArray tileEntityDummyArray) {
        this.x = tileEntityDummyArray.xCoord;
        this.y = tileEntityDummyArray.yCoord;
        this.z = tileEntityDummyArray.zCoord;
        this.orientation = (byte) tileEntityDummyArray.getOrientation()
            .ordinal();
        this.state = (byte) tileEntityDummyArray.getState();
        this.customName = tileEntityDummyArray.getCustomName();
        this.ownerUUID = tileEntityDummyArray.getOwnerUUID();
        this.trueXCoord = tileEntityDummyArray.getTrueXCoord();
        this.trueYCoord = tileEntityDummyArray.getTrueYCoord();
        this.trueZCoord = tileEntityDummyArray.getTrueZCoord();
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.orientation = buf.readByte();
        this.state = buf.readByte();
        int customNameLength = buf.readInt();
        this.customName = new String(
            buf.readBytes(customNameLength)
                .array());
        if (buf.readBoolean()) {
            this.ownerUUID = new UUID(buf.readLong(), buf.readLong());
        } else {
            this.ownerUUID = null;
        }
        this.trueXCoord = buf.readInt();
        this.trueYCoord = buf.readInt();
        this.trueZCoord = buf.readInt();
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeByte(orientation);
        buf.writeByte(state);
        buf.writeInt(customName.length());
        buf.writeBytes(customName.getBytes());
        if (ownerUUID != null) {
            buf.writeBoolean(true);
            buf.writeLong(ownerUUID.getMostSignificantBits());
            buf.writeLong(ownerUUID.getLeastSignificantBits());
        } else {
            buf.writeBoolean(false);
        }
        buf.writeInt(trueXCoord);
        buf.writeInt(trueYCoord);
        buf.writeInt(trueZCoord);
    }

    /**
     * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if
     * no reply
     * is needed.
     *
     * @param message The message
     * @param ctx
     * @return an optional return message
     */
    @Override
    public IMessage onMessage(MessageTileEntityDummy message, MessageContext ctx) {
        TileEntity tileEntity = FMLClientHandler.instance()
            .getClient().theWorld.getTileEntity(message.x, message.y, message.z);

        if (tileEntity instanceof TileEntityEE) {
            ((TileEntityEE) tileEntity).setOrientation(message.orientation);
            ((TileEntityEE) tileEntity).setState(message.state);
            ((TileEntityEE) tileEntity).setCustomName(message.customName);
            ((TileEntityEE) tileEntity).setOwnerUUID(message.ownerUUID);

            if (tileEntity instanceof TileEntityDummyArray) {
                ((TileEntityDummyArray) tileEntity)
                    .setTrueCoords(message.trueXCoord, message.trueYCoord, message.trueZCoord);
            }
        }

        return null;
    }
}
