package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class mod_Goulish extends BaseMod {
	public static Item blood = new Item(thModLoader.GetNewItemIndex())
			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/souls/blood.png"))
			.setItemName("blood");
	public static Item knife = new ItemSword(thModLoader.GetNewItemIndex(), EnumToolMaterial.WOOD)
			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/souls/knife.png"))
			.setItemName("knife");
	public static Block bloodStone = new Block(thModLoader.GetNewBlockIndex(), ModLoader.addOverride("/terrain.png", "/souls/blood-stone.png"), Material.rock)
			.setHardness(2.0F)
			.setResistance(10.0F)
			.setStepSound(Block.soundStoneFootstep)
			.setBlockName("bloodStone");
	public static Item flesh = new ItemFlesh(thModLoader.GetNewItemIndex(), 0, false)
			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/souls/raw-flesh.png"))
			.setItemName("rawFlesh");
	public static Item flask = new Item(thModLoader.GetNewItemIndex())
			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/souls/flask.png"))
			.setItemName("flask");
	public static Item elixir = new ItemElixir(thModLoader.GetNewItemIndex())
			.setItemName("elixir");
	public static Item soul = new ItemSoul(thModLoader.GetNewItemIndex())
			.setItemName("soul");
	
	public static int emptySoulPng = ModLoader.addOverride("/gui/items.png", "/souls/soul--empty.png");
	public static int zombieSoulPng = ModLoader.addOverride("/gui/items.png", "/souls/soul--zombie.png");
	public static int creeperSoulPng = ModLoader.addOverride("/gui/items.png", "/souls/soul--creeper.png");
	public static int spiderSoulPng = ModLoader.addOverride("/gui/items.png", "/souls/soul--spider.png");
	
	public static int zombieElixirPng = ModLoader.addOverride("/gui/items.png", "/souls/elixir--zombie.png");
	public static int creeperElixirPng = ModLoader.addOverride("/gui/items.png", "/souls/elixir--creeper.png");
	public static int spiderElixirPng = ModLoader.addOverride("/gui/items.png", "/souls/elixir--spider.png");
	
	public static StatusEffect poison;
	public static StatusEffect creeper;
	public static StatusEffect zombie;
	public static StatusEffect spider;
	
	
	public mod_Goulish() {		
		ModLoader.AddName(blood, "Blood");
		ModLoader.AddName(bloodStone, "Blood Stone");
		ModLoader.AddName(flask, "Flask");
		
		// add all status effects
		poison = new StatusEffect("poison", 1000);
		poison.dps = 1;
		poison.mod = 100;
		
		creeper = new StatusEffect("creeper", 600);
		creeper.miningSpeed = 1.5F;
		creeper.startMessage = "You feel a bit explosive";
		creeper.endMessage = "Your fuse withers out.";
		creeper.fortuneBlocks.add(new StatusEffectBlock(Block.stone.blockID, 10));
		creeper.builderBlocks.add(new StatusEffectBlock(Block.dirt.blockID, 10));
		
		zombie = new StatusEffect("zombie", 600);
		zombie.dmg = 1.5F;
		
		spider = new StatusEffect("spider", 600);
		spider.y = 2;
		spider.startMessage = "Your legs grow just a few inches";
		spider.endMessage = "Your ankles are no longer cold";
		
		// add souls
		ItemSoul.indexes.add(new ItemElixirIndex(emptySoulPng, "empty"));
		ItemSoul.indexes.add(new ItemElixirIndex(zombieSoulPng, "zombie"));
		ItemSoul.indexes.add(new ItemElixirIndex(creeperSoulPng, "creeper"));
		ItemSoul.indexes.add(new ItemElixirIndex(spiderSoulPng, "spider"));
		
		// add elixirs
		ItemElixir.indexes.add(new ItemElixirIndex(zombieElixirPng, "zombie", new StatusEffect[] {zombie}));
		ItemElixir.indexes.add(new ItemElixirIndex(creeperElixirPng, "creeper", new StatusEffect[] {creeper}));
		ItemElixir.indexes.add(new ItemElixirIndex(spiderElixirPng, "spider", new StatusEffect[] {spider}));
		
		// zombie
		ModLoader.AddShapelessRecipe(new ItemStack(elixir, 1, 0), new Object[] {flask, new ItemStack(soul, 1, 0)});
		// creeper
		ModLoader.AddShapelessRecipe(new ItemStack(elixir, 1, 1), new Object[] {flask, new ItemStack(soul, 1, 2)});
		
		// testing
		ModLoader.AddShapelessRecipe(new ItemStack(elixir, 1, 1), new Object[]{Block.dirt});
		
		// blocks
		ModLoader.AddShapelessRecipe(new ItemStack(bloodStone), new Object[] {blood, Block.cobblestone});
		
		// items
		ModLoader.AddRecipe(new ItemStack(flask), new Object[] {"g g", " g ", Character.valueOf('g'), Block.glass});
		ModLoader.AddRecipe(new ItemStack(knife), new Object[] {"I", "S", Character.valueOf('I'), Item.ingotIron, Character.valueOf('S'), Item.stick});
		
		// souls		
		ModLoader.AddRecipe(new ItemStack(soul, 1, 0), new Object[] {"BBB", "BSB", "BBB", Character.valueOf('B'), blood, Character.valueOf('S'), Block.slowSand});
		ModLoader.AddRecipe(new ItemStack(soul, 1, 1), new Object[] {"sSs", "s s", Character.valueOf('s'), Item.silk, Character.valueOf('S'), new ItemStack(soul, 1, 0)});
		ModLoader.AddRecipe(new ItemStack(soul, 1, 2), new Object[] {" g ", "gSg", " g ", Character.valueOf('g'), Item.gunpowder, Character.valueOf('S'), new ItemStack(soul, 1, 0)});
		ModLoader.AddRecipe(new ItemStack(soul, 1, 3), new Object[] {"f f", "fSf", Character.valueOf('f'), flesh, Character.valueOf('S'), new ItemStack(soul, 1, 0)});
	}

	@Override
	public String Version() {
		// TODO Auto-generated method stub
		return "0.0.1";
	}
}
