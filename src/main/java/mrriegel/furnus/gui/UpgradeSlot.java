package mrriegel.furnus.gui;

import mrriegel.furnus.block.AbstractMachine;
import mrriegel.furnus.item.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class UpgradeSlot extends Slot {
	EntityPlayer player;

	public UpgradeSlot(MachineContainer furnusContainer, EntityPlayer player, IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
		this.player = player;
	}

	@Override
	public boolean isItemValid(ItemStack p_75214_1_) {
		return p_75214_1_.getItem() == ModItems.upgrade && in(p_75214_1_, getSlotIndex(), (AbstractMachine) inventory);
	}

	public static boolean in(ItemStack stack, int slot, AbstractMachine t) {
		for (int i = 10; i < 15; i++) {
			if (t.getStackInSlot(i) != null && t.getStackInSlot(i).getItemDamage() == stack.getItemDamage() && i != slot)
				return false;
		}
		return true;
	}

	@Override
	public void onSlotChanged() {
		super.onSlotChanged();
		AbstractMachine t = (AbstractMachine) inventory;
		t.updateStats(player);
	}
}
