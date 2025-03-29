package net.minecraft.src;

public class thModLoader {
	public static int GetNewItemIndex() {
		int var0 = 0;
		Item[] var1 = Item.itemsList;
		int var2 = var1.length;

		for(int var3 = 0; var3 < var2; ++var3) {
			Item var4 = var1[var3];
			if(var4 != null && var3 > var0) {
				var0 = var3;
			}
		}
		
		// var0 - 256 + 1
		return var0 - 255;
	}

	public static Item AddItem(String var0, String var1, Item var2, Boolean var3) {
		return AddItem(GetNewItemIndex(), var0, var1, var2, var3);
	}

	public static Item AddItem(int var0, String var1, String var2, Item var3, Boolean var4) {
		Item var5 = new Item(var0);
		if(var1 != null && var1 != "") {
			var5.setItemName(var1);
		}

		if(var3 != null) {
			var5.setContainerItem(var3);
		}

		if(var4.booleanValue()) {
			var5.setFull3D();
		}

		ModLoader.AddName(var5, var2);
		return var5;
	}
	
	public static int GetNewBlockIndex() {
		int var0 = 0;
		Block[] var1 = Block.blocksList;
		int var2 = var1.length;

		for(int var3 = 0; var3 < var2; ++var3) {
			Block var4 = var1[var3];
			if(var4 != null) {
				var0 = var3;
			}
		}

		return var0 + 1;
	}
}
