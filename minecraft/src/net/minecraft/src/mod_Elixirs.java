package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class mod_Elixirs extends BaseMod {
	public static Item blood = new Item(thModLoader.GetNewItemIndex())
			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/elixirs/blood.png"))
			.setItemName("blood");
	public static Item knife = new ItemSword(thModLoader.GetNewItemIndex(), EnumToolMaterial.WOOD)
			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/elixirs/knife.png"))
			.setItemName("knife");
	public static Block bloodStone = new Block(thModLoader.GetNewBlockIndex(), ModLoader.addOverride("/terrain.png", "/elixirs/blood-stone.png"), Material.rock)
			.setHardness(2.0F)
			.setResistance(10.0F)
			.setStepSound(Block.soundStoneFootstep)
			.setBlockName("bloodStone");
	public static Item flesh = new ItemFlesh(thModLoader.GetNewItemIndex(), 0, false)
			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/elixirs/raw-flesh.png"))
			.setItemName("rawFlesh");
	public static Item flask = new Item(thModLoader.GetNewItemIndex())
			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/elixirs/flask.png"))
			.setItemName("flask");
	public static Item elixir = new ItemElixir(thModLoader.GetNewItemIndex())
			.setItemName("elixir");
	public static Item soul = new ItemSoul(thModLoader.GetNewItemIndex())
			.setItemName("soul");
	
	public static int emptySoulPng = ModLoader.addOverride("/gui/items.png", "/elixirs/soul--empty.png");
	
	public static int zombieElixirPng = ModLoader.addOverride("/gui/items.png", "/elixirs/elixir--zombie.png");
	public static int creeperElixirPng = ModLoader.addOverride("/gui/items.png", "/elixirs/elixir--creeper.png");
	public static int spiderElixirPng = ModLoader.addOverride("/gui/items.png", "/elixirs/elixir--spider.png");
	public static int woodcutterElixirPng = ModLoader.addOverride("/gui/items.png", "/elixirs/elixir--woodcutter.png");
	public static int fishElixirPng = ModLoader.addOverride("/gui/items.png", "/elixirs/elixir--fish.png");
	public static int mermanElixirPng = ModLoader.addOverride("/gui/items.png", "/elixirs/elixir--merman.png");
	public static int striderElixirPng = ModLoader.addOverride("/gui/items.png", "/elixirs/elixir--strider.png");
	public static int featherElixirPng = ModLoader.addOverride("/gui/items.png", "/elixirs/elixir--feather.png");
	public static int hareElixirPng = ModLoader.addOverride("/gui/items.png", "/elixirs/elixir--hare.png");
	public static int sheepElixirPng = ModLoader.addOverride("/gui/items.png", "/elixirs/elixir--sheep.png");
	
	public static StatusEffect poison;
	public static StatusEffect creeper;
	public static StatusEffect zombie;
	public static StatusEffect spider;
	public static StatusEffect woodcutter;
	public static StatusEffect fish;
	public static StatusEffect merman;
	public static StatusEffect strider;
	public static StatusEffect feather;
	public static StatusEffect hare;
	public static StatusEffect sheep;
	
	public mod_Elixirs() {		
		ModLoader.AddName(blood, "Blood");
		ModLoader.AddName(bloodStone, "Blood Stone");
		ModLoader.AddName(flask, "Flask");
		ModLoader.AddName(knife, "Dagger");
		
		// add all status effects
		poison = new StatusEffect("poison", 1000);
		poison.dps = 1;
		poison.mod = 100;
		
		creeper = new StatusEffect("creeper", 600);
		creeper.miningSpeed = 1.5F;
		creeper.startMessage = "You feel a bit explosive";
		creeper.endMessage = "Your fuse withers out.";
		
		zombie = new StatusEffect("zombie", 600);
		zombie.dmg = 1.5F;
		
		spider = new StatusEffect("spider", 600);
		spider.y = 2;
		spider.startMessage = "Your legs grow just a few inches";
		spider.endMessage = "Your ankles are no longer cold";
		
		woodcutter = new StatusEffect("woodcutter", 600);
		woodcutter.fortuneBlocks.add(new StatusEffectBlock(Block.wood.blockID, 3));
		woodcutter.startMessage = "You grow a thick beard";
		woodcutter.endMessage = "Your whiskers grow warry";
		
		fish = new StatusEffect("fish", 600);
		fish.waterSpeed = .1F;
		fish.waterBreath = true;
		fish.startMessage = "You look a little green around the gills";
		fish.endMessage = "The sharks begin to eye you";
		
		merman = new StatusEffect("merman", 600);
		merman.mineInWater = true;
		merman.startMessage = "You wish you had a trident";
		merman.endMessage = "A fork seems more appropriate now";
		
		strider = new StatusEffect("merman", 600);
		strider.waterWalking = true;
		strider.startMessage = "Ain't nothin' gonna break my stride!";
		strider.endMessage = "...but someone might slow me down";
		
		feather = new StatusEffect("feather", 600);
		feather.featherFall = true;
		feather.startMessage = "Light as a feather";
		feather.endMessage = "...stiff as a block";
		
		hare = new StatusEffect("hare", 600);
		hare.x = 2;
		hare.z = 2;
		hare.startMessage = "No tortoise in sight";
		hare.endMessage = "...how did he beat me!?";
		
		sheep = new StatusEffect("hare", 600);
		sheep.dmg = .5F;
		sheep.startMessage = "I wouldn't hurt a fly";
		sheep.endMessage = "The edge sets back in your jaw";
		
		StatusEffect spider2 = new StatusEffect(spider, 2);
		StatusEffect zombie2 = new StatusEffect(zombie, 2);
		StatusEffect creeper2 = new StatusEffect(creeper, 2);
		StatusEffect fish2 = new StatusEffect(fish, 2);
		
		// add souls
		ItemSoul.indexes.add(new ItemElixirIndex(emptySoulPng, "empty", "Empty Soul"));
		
		// add elixirs
		ItemElixir.indexes.add(new ItemElixirIndex(zombieElixirPng, "zombie", "Zombie Elixir", new StatusEffect[] {zombie}));
		ItemElixir.indexes.add(new ItemElixirIndex(creeperElixirPng, "creeper", "Creeper Elixir", new StatusEffect[] {creeper}));
		ItemElixir.indexes.add(new ItemElixirIndex(spiderElixirPng, "spider", "Spider Elixir", new StatusEffect[] {spider}));
		ItemElixir.indexes.add(new ItemElixirIndex(spiderElixirPng, "spider2", "Spider Elixir II", new StatusEffect[] {spider2}));
		ItemElixir.indexes.add(new ItemElixirIndex(zombieElixirPng, "zombie2", "Zombie Elixir II", new StatusEffect[] {zombie2}));
		ItemElixir.indexes.add(new ItemElixirIndex(creeperElixirPng, "creeper2", "Creeper Elixir II", new StatusEffect[] {creeper2}));
		ItemElixir.indexes.add(new ItemElixirIndex(woodcutterElixirPng, "woodcutter", "Woodcutter Elixir", new StatusEffect[] {woodcutter}));
		ItemElixir.indexes.add(new ItemElixirIndex(fishElixirPng, "fish", "Fish Elixir", new StatusEffect[] {fish}));
		ItemElixir.indexes.add(new ItemElixirIndex(fishElixirPng, "merman", "Merman Elixir", new StatusEffect[] {merman}));
		ItemElixir.indexes.add(new ItemElixirIndex(fishElixirPng, "fish2", "Fish Elixir II", new StatusEffect[] {fish2}));
		ItemElixir.indexes.add(new ItemElixirIndex(striderElixirPng, "strider", "Strider Elixir", new StatusEffect[] {strider}));
		ItemElixir.indexes.add(new ItemElixirIndex(featherElixirPng, "feather", "Feather Elixir", new StatusEffect[] {feather}));
		ItemElixir.indexes.add(new ItemElixirIndex(hareElixirPng, "hare", "Hare Elixir", new StatusEffect[] {hare}));
		ItemElixir.indexes.add(new ItemElixirIndex(sheepElixirPng, "sheep", "Sheepish Elixir", new StatusEffect[] {sheep}));
		
		// soul recipe
		ModLoader.AddRecipe(new ItemStack(soul, 1, 0), new Object[] {"BBB", "BSB", "BBB", Character.valueOf('B'), blood, Character.valueOf('S'), Block.slowSand});
		
		// zombie
		ModLoader.AddRecipe(new ItemStack(elixir, 1, 0), new Object[] {"R  R", "RSR", " F ", Character.valueOf('R'), flesh, Character.valueOf('S'), new ItemStack(soul, 1, 0), Character.valueOf('F'), flask});
		// creeper
		ModLoader.AddRecipe(new ItemStack(elixir, 1, 1), new Object[] {" G ", "GSG", " F ", Character.valueOf('G'), Item.gunpowder, Character.valueOf('S'), new ItemStack(soul, 1, 0), Character.valueOf('F'), flask});
		// spider
		ModLoader.AddRecipe(new ItemStack(elixir, 1, 2), new Object[] {"sSs", "sFs", Character.valueOf('s'), Item.silk, Character.valueOf('S'), new ItemStack(soul, 1, 0), Character.valueOf('F'), flask});
		// spider 2
		ModLoader.AddShapelessRecipe(new ItemStack(elixir, 1, 3), new Object[] {new ItemStack(elixir, 1, 2), Item.diamond});
		ModLoader.AddShapelessRecipe(new ItemStack(elixir, 1, 4), new Object[] {new ItemStack(elixir, 1, 0), Item.diamond});
		ModLoader.AddShapelessRecipe(new ItemStack(elixir, 1, 5), new Object[] {new ItemStack(elixir, 1, 1), Item.diamond});
		ModLoader.AddRecipe(new ItemStack(elixir, 1, 6), new Object[] {"WWW", "WSW", "WFW", Character.valueOf('W'), Block.wood, Character.valueOf('S'), new ItemStack(soul, 1, 0), Character.valueOf('F'), flask});
		ModLoader.AddRecipe(new ItemStack(elixir, 1, 7), new Object[] {"fSf", " F ", Character.valueOf('f'), Item.fishRaw, Character.valueOf('S'), new ItemStack(soul, 1, 0), Character.valueOf('F'), flask});
		ModLoader.AddRecipe(new ItemStack(elixir, 1, 8), new Object[] {" P ", "sSs", "sFs", Character.valueOf('s'), Block.sand, Character.valueOf('S'), new ItemStack(soul, 1, 0), Character.valueOf('F'), flask, Character.valueOf('P'), Item.pickaxeWood});
		ModLoader.AddShapelessRecipe(new ItemStack(elixir, 1, 9), new Object[] {new ItemStack(elixir, 1, 7), Item.diamond});
		ModLoader.AddRecipe(new ItemStack(elixir, 1, 10), new Object[] {"sSs", "sFs", Character.valueOf('s'), Block.sandStone, Character.valueOf('S'), new ItemStack(soul, 1, 0), Character.valueOf('F'), flask});
		ModLoader.AddRecipe(new ItemStack(elixir, 1, 11), new Object[] {"fSf", "fFf", Character.valueOf('f'), Item.feather, Character.valueOf('S'), new ItemStack(soul, 1, 0), Character.valueOf('F'), flask});
		ModLoader.AddRecipe(new ItemStack(elixir, 1, 12), new Object[] {"sSs", "sFs", Character.valueOf('s'), Block.blockSnow, Character.valueOf('S'), new ItemStack(soul, 1, 0), Character.valueOf('F'), flask});
		ModLoader.AddRecipe(new ItemStack(elixir, 1, 13), new Object[] {"W W", "WSW", " F ", Character.valueOf('W'), Block.cloth, Character.valueOf('S'), new ItemStack(soul, 1, 0), Character.valueOf('F'), flask});
		
		// blocks
		ModLoader.AddShapelessRecipe(new ItemStack(bloodStone), new Object[] {blood, Block.cobblestone});
		ModLoader.AddShapelessRecipe(new ItemStack(elixir, 1, 13), new Object[] {Block.dirt});
		
		// items
		ModLoader.AddRecipe(new ItemStack(flask), new Object[] {"g g", " g ", Character.valueOf('g'), Block.glass});
		ModLoader.AddRecipe(new ItemStack(knife), new Object[] {"I", "S", Character.valueOf('I'), Item.ingotIron, Character.valueOf('S'), Item.stick});
		
		ItemElixir e = (ItemElixir)elixir;
		for(int i = 0; i < e.indexes.size(); i++) {
			ItemElixirIndex idx = e.indexes.get(i);
			ModLoader.AddName(new ItemStack(e, 1, i), idx.displayName);
		}
		
		ItemSoul s = (ItemSoul)soul;
		for(int i = 0; i < s.indexes.size(); i++) {
			ItemElixirIndex idx = s.indexes.get(i);
			ModLoader.AddName(new ItemStack(s, 1, i), idx.displayName);
		}
	}

	@Override
	public String Version() {
		// TODO Auto-generated method stub
		return "0.0.1";
	}
}
