package mrriegel.furnus.gui;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.block.AbstractMachine;
import mrriegel.furnus.block.AbstractMachine.Direction;
import mrriegel.furnus.block.AbstractMachine.Mode;
import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.block.TilePulvus;
import mrriegel.furnus.handler.GuiHandler;
import mrriegel.furnus.handler.PacketHandler;
import mrriegel.furnus.message.OpenMessage;
import mrriegel.furnus.message.PutMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

public class IOFGui extends GuiScreen {
	private static final ResourceLocation GuiTextures = new ResourceLocation(Furnus.MODID + ":textures/gui/iof.png");
	AbstractMachine tile;
	Button top, front, left, right, bottom, back;
	int imageWidth = 101;
	int imageHeight = 101;
	int guiLeft, guiTop;
	String id;

	Mode topMode, frontMode, leftMode, rightMode, bottomMode, backMode;

	public IOFGui(AbstractMachine tileEntity, int iD) {
		tile = tileEntity;
		if (iD == 1)
			id = "I";
		else if (iD == 2)
			id = "O";
		else if (iD == 3)
			id = "F";

		topMode = AbstractMachine.getMap(id, tile).get(Direction.TOP);
		frontMode = AbstractMachine.getMap(id, tile).get(Direction.FRONT);
		leftMode = AbstractMachine.getMap(id, tile).get(Direction.LEFT);
		rightMode = AbstractMachine.getMap(id, tile).get(Direction.RIGHT);
		bottomMode = AbstractMachine.getMap(id, tile).get(Direction.BOTTOM);
		backMode = AbstractMachine.getMap(id, tile).get(Direction.BACK);
	}

	@Override
	public void initGui() {
		super.initGui();
		guiLeft = (this.width - this.imageWidth) / 2;
		guiTop = (this.height - this.imageHeight) / 2;
		top = new Button(0, guiLeft + 40, guiTop + 20, 20, 20, topMode.toString().substring(0, 1).toUpperCase());
		buttonList.add(top);
		front = new Button(1, guiLeft + 40, guiTop + 42, 20, 20, frontMode.toString().substring(0, 1).toUpperCase());
		buttonList.add(front);
		left = new Button(2, guiLeft + 18, guiTop + 42, 20, 20, leftMode.toString().substring(0, 1).toUpperCase());
		buttonList.add(left);
		right = new Button(3, guiLeft + 62, guiTop + 42, 20, 20, rightMode.toString().substring(0, 1).toUpperCase());
		buttonList.add(right);
		bottom = new Button(4, guiLeft + 40, guiTop + 64, 20, 20, bottomMode.toString().substring(0, 1).toUpperCase());
		buttonList.add(bottom);
		back = new Button(5, guiLeft + 62, guiTop + 64, 20, 20, backMode.toString().substring(0, 1).toUpperCase());
		buttonList.add(back);
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		switch (p_146284_1_.id) {
		case 0:
			topMode = topMode.next();
			top.displayString = topMode.toString().substring(0, 1).toUpperCase();
			AbstractMachine.getMap(id, tile).put(Direction.TOP, topMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.TOP.toString(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), id, topMode.toString()));
			break;
		case 1:
			frontMode = frontMode.next();
			front.displayString = frontMode.toString().substring(0, 1).toUpperCase();
			AbstractMachine.getMap(id, tile).put(Direction.FRONT, frontMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.FRONT.toString(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), id, frontMode.toString()));
			break;
		case 2:
			leftMode = leftMode.next();
			left.displayString = leftMode.toString().substring(0, 1).toUpperCase();
			AbstractMachine.getMap(id, tile).put(Direction.LEFT, leftMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.LEFT.toString(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), id, leftMode.toString()));
			break;
		case 3:
			rightMode = rightMode.next();
			right.displayString = rightMode.toString().substring(0, 1).toUpperCase();
			AbstractMachine.getMap(id, tile).put(Direction.RIGHT, rightMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.RIGHT.toString(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), id, rightMode.toString()));
			break;
		case 4:
			bottomMode = bottomMode.next();
			bottom.displayString = bottomMode.toString().substring(0, 1).toUpperCase();
			AbstractMachine.getMap(id, tile).put(Direction.BOTTOM, bottomMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.BOTTOM.toString(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), id, bottomMode.toString()));
			break;
		case 5:
			backMode = backMode.next();
			back.displayString = backMode.toString().substring(0, 1).toUpperCase();
			AbstractMachine.getMap(id, tile).put(Direction.BACK, backMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.BACK.toString(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), id, backMode.toString()));
			break;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiTextures);
		guiLeft = (this.width - this.imageWidth) / 2;
		guiTop = (this.height - this.imageHeight) / 2;
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.imageWidth, this.imageHeight);
		super.drawScreen(mouseX, mouseY, partialTicks);
		String pre = "gui.furnus.";
		mc.fontRendererObj.drawString(id.equals("F") ? I18n.format(pre + "fuel") : id.equals("I") ? I18n.format(pre + "input") : I18n.format(pre + "output"), guiLeft + 8, guiTop + 6, 4210752);
		if (top.isMouseOver())
			drawHoveringText(Lists.newArrayList(I18n.format(pre + "top") + " - " + I18n.format(pre + topMode.toString())), mouseX, mouseY);
		if (bottom.isMouseOver())
			drawHoveringText(Lists.newArrayList(I18n.format(pre + "bottom") + " - " + I18n.format(pre + bottomMode.toString())), mouseX, mouseY);
		if (right.isMouseOver())
			drawHoveringText(Lists.newArrayList(I18n.format(pre + "right") + " - " + I18n.format(pre + rightMode.toString())), mouseX, mouseY);
		if (left.isMouseOver())
			drawHoveringText(Lists.newArrayList(I18n.format(pre + "left") + " - " + I18n.format(pre + leftMode.toString())), mouseX, mouseY);
		if (back.isMouseOver())
			drawHoveringText(Lists.newArrayList(I18n.format(pre + "back") + " - " + I18n.format(pre + backMode.toString())), mouseX, mouseY);
		if (front.isMouseOver())
			drawHoveringText(Lists.newArrayList(I18n.format(pre + "front") + " - " + I18n.format(pre + frontMode.toString())), mouseX, mouseY);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void onGuiClosed() {
		int id = tile instanceof TileFurnus ? GuiHandler.FURNUS : tile instanceof TilePulvus ? GuiHandler.PULVUS : -1;
		PacketHandler.INSTANCE.sendToServer(new OpenMessage(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), id));
	}

	class Button extends GuiButton {

		public Button(int p_i1021_1_, int p_i1021_2_, int p_i1021_3_, int p_i1021_4_, int p_i1021_5_, String p_i1021_6_) {
			super(p_i1021_1_, p_i1021_2_, p_i1021_3_, p_i1021_4_, p_i1021_5_, p_i1021_6_);
		}

		@Override
		public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
			if (this.visible) {
				p_146112_1_.getTextureManager().bindTexture(BUTTON_TEXTURES);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.hovered = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
				int k = this.getHoverState(this.hovered);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.blendFunc(770, 771);
				this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
				this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
				p_146112_1_.getTextureManager().bindTexture(GuiTextures);
				this.drawTexturedModalRect(this.xPosition, this.yPosition, 101, 0 + (displayString.equals(Mode.ENABLED.toString().substring(0, 1).toUpperCase()) ? 0 : displayString.equals(Mode.X.toString().substring(0, 1).toUpperCase()) ? 20 : 40), 20, 20);

			}
		}

	}
}
