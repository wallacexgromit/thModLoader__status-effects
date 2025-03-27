package net.minecraft.src;

public class thModLoaderItemSpade extends thModLoaderItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{Block.grass, Block.dirt, Block.sand, Block.gravel, Block.snow, Block.blockSnow, Block.blockClay, Block.tilledField};

	public thModLoaderItemSpade(int var1, thModLoaderListToolMaterial var2) {
		super(var1, 1, var2, blocksEffectiveAgainst);
	}

	public boolean canHarvestBlock(Block var1) {
		return var1 == Block.snow ? true : var1 == Block.blockSnow;
	}
}
