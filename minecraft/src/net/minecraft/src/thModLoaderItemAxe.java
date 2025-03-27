package net.minecraft.src;

public class thModLoaderItemAxe extends thModLoaderItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{Block.planks, Block.bookShelf, Block.wood, Block.chest};

	protected thModLoaderItemAxe(int var1, thModLoaderListToolMaterial var2) {
		super(var1, 3, var2, blocksEffectiveAgainst);
	}
}
