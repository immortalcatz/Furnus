package mrriegel.furnus.gui;

import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.block.TilePulvus;
import mrriegel.furnus.handler.ConfigHandler;
import mrriegel.furnus.handler.CrunchHandler;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class OutputSlot extends Slot {
	private EntityPlayer thePlayer;
	private int field_75228_b;

	public OutputSlot(EntityPlayer p_i1813_1_, IInventory p_i1813_2_, int p_i1813_3_, int p_i1813_4_, int p_i1813_5_) {
		super(p_i1813_2_, p_i1813_3_, p_i1813_4_, p_i1813_5_);
		this.thePlayer = p_i1813_1_;
	}

	@Override
	public boolean isItemValid(ItemStack p_75214_1_) {
		return false;
	}

	@Override
	public ItemStack decrStackSize(int p_75209_1_) {
		if (this.getHasStack()) {
			this.field_75228_b += Math.min(p_75209_1_, this.getStack().stackSize);
		}

		return super.decrStackSize(p_75209_1_);
	}

	@Override
	public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
		this.onCrafting(p_82870_2_);
		super.onPickupFromSlot(p_82870_1_, p_82870_2_);
	}

	@Override
	protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
		this.field_75228_b += p_75210_2_;
		this.onCrafting(p_75210_1_);
	}

	@Override
	protected void onCrafting(ItemStack stack) {
		stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75228_b);
		if (!this.thePlayer.worldObj.isRemote) {
			int i = this.field_75228_b;
			float m = inventory instanceof TileFurnus ? FurnaceRecipes.instance().getSmeltingExperience(stack) : inventory instanceof TilePulvus ? CrunchHandler.instance().getExperience(stack) : 0f;
			float f = thePlayer.capabilities.isCreativeMode ? 0.0f : m + m * ((MachineContainer) thePlayer.openContainer).tile.getXp() * (float) ConfigHandler.xpMulti;
			int j;

			if (f == 0.0F) {
				i = 0;
			} else if (f < 1.0F) {
				j = MathHelper.floor_float(i * f);

				if (j < MathHelper.ceiling_float_int(i * f) && (float) Math.random() < i * f - j) {
					++j;
				}
				i = j;
			}
			while (i > 0) {
				j = EntityXPOrb.getXPSplit(i);
				i -= j;
				this.thePlayer.worldObj.spawnEntityInWorld(new EntityXPOrb(this.thePlayer.worldObj, this.thePlayer.posX, this.thePlayer.posY + 0.5D, this.thePlayer.posZ + 0.5D, j));
			}
		}

		this.field_75228_b = 0;

		if (inventory instanceof TileFurnus)
			FMLCommonHandler.instance().firePlayerSmeltedEvent(thePlayer, stack);
		if (stack.getItem() == Items.IRON_INGOT) {
			this.thePlayer.addStat(AchievementList.ACQUIRE_IRON, 1);
		}
		if (stack.getItem() == Items.COOKED_FISH) {
			this.thePlayer.addStat(AchievementList.COOK_FISH, 1);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		Slot e = ((Slot) obj);
		return e.xDisplayPosition == xDisplayPosition && e.yDisplayPosition == yDisplayPosition;
	}

}