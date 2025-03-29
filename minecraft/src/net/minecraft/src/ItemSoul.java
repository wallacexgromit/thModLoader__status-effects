package net.minecraft.src;

import java.util.ArrayList;

public class ItemSoul extends Item {
	public static ArrayList<ItemElixirIndex> indexes = new ArrayList<ItemElixirIndex>();

	public ItemSoul(int var1) {
		super(var1);
		this.setHasSubtypes(true);
	}
	
	public int getIconFromDamage(int var1) {
		ItemElixirIndex idx = getSoulIndex(var1);
		if(idx != null) {
			return idx.iconIdx;
		}
		else {
			return 0;
		}
	}

	public String getItemNameIS(ItemStack var1) {
		ItemElixirIndex idx = getSoulIndex(var1.getItemDamage());
		String name = super.getItemName();
		if(idx != null) {
			return name + "." + idx.name;
		}
		else {
			return name;
		}
	}
	
	private static ItemElixirIndex getSoulIndex(int dmg) {
		if(dmg < indexes.size()) {
			return indexes.get(dmg);
		}
		return null;
	}
}
