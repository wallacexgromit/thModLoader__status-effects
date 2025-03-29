package net.minecraft.src;

public class ItemFlesh extends ItemFood {
	private int healAmount;

	public ItemFlesh(int var1, int var2, boolean var3) {
		super(var1, var2, var3);
	}

	public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
		--var1.stackSize;
		var3.heal(this.healAmount);
		
		var3.addStatusEffect(mod_Goulish.poison);
		
		return var1;
	}
}
