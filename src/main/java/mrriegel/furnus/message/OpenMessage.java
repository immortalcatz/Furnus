package mrriegel.furnus.message;

import io.netty.buffer.ByteBuf;
import mrriegel.furnus.Furnus;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class OpenMessage implements IMessage, IMessageHandler<OpenMessage, IMessage> {
	int x, y, z, id;

	public OpenMessage() {
	}

	public OpenMessage(int x, int y, int z, int id) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
	}

	@Override
	public IMessage onMessage(final OpenMessage message, final MessageContext ctx) {
		IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
		mainThread.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				ctx.getServerHandler().playerEntity.openGui(Furnus.instance, message.id, ctx.getServerHandler().playerEntity.worldObj, message.x, message.y, message.z);
			}
		});
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.id);
	}

}
