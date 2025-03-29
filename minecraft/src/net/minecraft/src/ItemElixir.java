package net.minecraft.src;

import java.util.ArrayList;

public class ItemElixir extends ItemFood {
	public static ArrayList<ItemElixirIndex> indexes = new ArrayList<ItemElixirIndex>();

	public ItemElixir(int var1) {
		super(var1, 0, false);
		this.setHasSubtypes(true);
	}
	
	public int getIconFromDamage(int var1) {
		ItemElixirIndex idx = getElixirIndex(var1);
		if(idx != null) {
			return idx.iconIdx;
		}
		else {
			return 0;
		}
	}

	public String getItemNameIS(ItemStack var1) {
		ItemElixirIndex idx = getElixirIndex(var1.getItemDamage());
		String name = super.getItemName();
		if(idx != null) {
			return name + "." + idx.name;
		}
		else {
			return name;
		}
	}
	
	private static ItemElixirIndex getElixirIndex(int dmg) {
		if(dmg < indexes.size()) {
			return indexes.get(dmg);
		}
		return null;
	}

	public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
		--var1.stackSize;
		ItemElixirIndex idx = getElixirIndex(var1.getItemDamage());
		if(idx != null) {
			for(StatusEffect se : idx.statusEffects) {
				var3.addStatusEffect(se);
			}
		}
		
		return var1;
	}
}
