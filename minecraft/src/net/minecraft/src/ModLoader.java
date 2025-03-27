package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public final class ModLoader {
	private static final List<TextureFX> animList = new LinkedList();
	private static final Map<Integer, BaseMod> blockModels = new HashMap();
	private static final Map<Integer, Boolean> blockSpecialInv = new HashMap();
	private static final File cfgdir = new File(Minecraft.getMinecraftDir(), "/config/");
	private static final File cfgfile = new File(cfgdir, "ModLoader.cfg");
	public static Level cfgLoggingLevel = Level.FINER;
	private static Map<String, Class<? extends Entity>> classMap = null;
	private static long clock = 0L;
	public static final boolean DEBUG = false;
	private static Field field_animList = null;
	private static Field field_armorList = null;
	private static Field field_blockList = null;
	private static Field field_modifiers = null;
	private static Field field_TileEntityRenderers = null;
	private static boolean hasInit = false;
	private static int highestEntityId = 3000;
	private static final Map<BaseMod, Boolean> inGameHooks = new HashMap();
	private static final Map<BaseMod, Boolean> inGUIHooks = new HashMap();
	private static Minecraft instance = null;
	private static int itemSpriteIndex = 0;
	private static int itemSpritesLeft = 0;
	private static final Map<BaseMod, Map<KeyBinding, boolean[]>> keyList = new HashMap();
	private static final File logfile = new File(Minecraft.getMinecraftDir(), "ModLoader.txt");
	private static final Logger logger = Logger.getLogger("ModLoader");
	private static FileHandler logHandler = null;
	private static Method method_RegisterEntityID = null;
	private static Method method_RegisterTileEntity = null;
	private static final File modDir = new File(Minecraft.getMinecraftDir(), "/mods/");
	private static final LinkedList<BaseMod> modList = new LinkedList();
	private static int nextBlockModelID = 1000;
	private static final Map<Integer, Map<String, Integer>> overrides = new HashMap();
	public static final Properties props = new Properties();
	private static BiomeGenBase[] standardBiomes;
	private static int terrainSpriteIndex = 0;
	private static int terrainSpritesLeft = 0;
	private static String texPack = null;
	private static boolean texturesAdded = false;
	private static final boolean[] usedItemSprites = new boolean[256];
	private static final boolean[] usedTerrainSprites = new boolean[256];
	public static final String VERSION = "ModLoader Beta 1.7.3";

	public static void AddAchievementDesc(Achievement var0, String var1, String var2) {
		try {
			if(var0.statName.contains(".")) {
				String[] var3 = var0.statName.split("\\.");
				if(var3.length == 2) {
					String var4 = var3[1];
					AddLocalization("achievement." + var4, var1);
					AddLocalization("achievement." + var4 + ".desc", var2);
					setPrivateValue(StatBase.class, var0, 1, StringTranslate.getInstance().translateKey("achievement." + var4));
					setPrivateValue(Achievement.class, var0, 3, StringTranslate.getInstance().translateKey("achievement." + var4 + ".desc"));
				} else {
					setPrivateValue(StatBase.class, var0, 1, var1);
					setPrivateValue(Achievement.class, var0, 3, var2);
				}
			} else {
				setPrivateValue(StatBase.class, var0, 1, var1);
				setPrivateValue(Achievement.class, var0, 3, var2);
			}
		} catch (IllegalArgumentException var5) {
			logger.throwing("ModLoader", "AddAchievementDesc", var5);
			ThrowException(var5);
		} catch (SecurityException var6) {
			logger.throwing("ModLoader", "AddAchievementDesc", var6);
			ThrowException(var6);
		} catch (NoSuchFieldException var7) {
			logger.throwing("ModLoader", "AddAchievementDesc", var7);
			ThrowException(var7);
		}

	}

	public static int AddAllFuel(int var0) {
		logger.finest("Finding fuel for " + var0);
		int var1 = 0;

		for(Iterator var2 = modList.iterator(); var2.hasNext() && var1 == 0; var1 = ((BaseMod)var2.next()).AddFuel(var0)) {
		}

		if(var1 != 0) {
			logger.finest("Returned " + var1);
		}

		return var1;
	}

	public static void AddAllRenderers(Map<Class<? extends Entity>, Render> var0) {
		if(!hasInit) {
			init();
			logger.fine("Initialized");
		}

		Iterator var1 = modList.iterator();

		while(var1.hasNext()) {
			BaseMod var2 = (BaseMod)var1.next();
			var2.AddRenderer(var0);
		}

	}

	public static void addAnimation(TextureFX var0) {
		logger.finest("Adding animation " + var0.toString());
		Iterator var1 = animList.iterator();

		while(var1.hasNext()) {
			TextureFX var2 = (TextureFX)var1.next();
			if(var2.tileImage == var0.tileImage && var2.iconIndex == var0.iconIndex) {
				animList.remove(var0);
				break;
			}
		}

		animList.add(var0);
	}

	public static int AddArmor(String var0) {
		try {
			String[] var1 = (String[])((String[])field_armorList.get((Object)null));
			List var2 = Arrays.asList(var1);
			ArrayList var3 = new ArrayList();
			var3.addAll(var2);
			if(!var3.contains(var0)) {
				var3.add(var0);
			}

			int var4 = var3.indexOf(var0);
			field_armorList.set((Object)null, var3.toArray(new String[0]));
			return var4;
		} catch (IllegalArgumentException var5) {
			logger.throwing("ModLoader", "AddArmor", var5);
			ThrowException("An impossible error has occured!", var5);
		} catch (IllegalAccessException var6) {
			logger.throwing("ModLoader", "AddArmor", var6);
			ThrowException("An impossible error has occured!", var6);
		}

		return -1;
	}

	public static void AddLocalization(String var0, String var1) {
		Properties var2 = null;

		try {
			var2 = (Properties)getPrivateValue(StringTranslate.class, StringTranslate.getInstance(), 1);
		} catch (SecurityException var4) {
			logger.throwing("ModLoader", "AddLocalization", var4);
			ThrowException(var4);
		} catch (NoSuchFieldException var5) {
			logger.throwing("ModLoader", "AddLocalization", var5);
			ThrowException(var5);
		}

		if(var2 != null) {
			var2.put(var0, var1);
		}

	}

	private static void addMod(ClassLoader var0, String var1) {
		try {
			String var2 = var1.split("\\.")[0];
			if(var2.contains("$")) {
				return;
			}

			if(props.containsKey(var2) && (props.getProperty(var2).equalsIgnoreCase("no") || props.getProperty(var2).equalsIgnoreCase("off"))) {
				return;
			}

			Package var3 = ModLoader.class.getPackage();
			if(var3 != null) {
				var2 = var3.getName() + "." + var2;
			}

			Class var4 = var0.loadClass(var2);
			if(!BaseMod.class.isAssignableFrom(var4)) {
				return;
			}

			setupProperties(var4);
			BaseMod var5 = (BaseMod)var4.newInstance();
			if(var5 != null) {
				modList.add(var5);
				logger.fine("Mod Loaded: \"" + var5.toString() + "\" from " + var1);
				System.out.println("Mod Loaded: " + var5.toString());
			}
		} catch (Throwable var6) {
			logger.fine("Failed to load mod from \"" + var1 + "\"");
			System.out.println("Failed to load mod from \"" + var1 + "\"");
			logger.throwing("ModLoader", "addMod", var6);
			ThrowException(var6);
		}

	}

	public static void AddName(Object var0, String var1) {
		String var2 = null;
		Exception var3;
		if(var0 instanceof Item) {
			Item var4 = (Item)var0;
			if(var4.getItemName() != null) {
				var2 = var4.getItemName() + ".name";
			}
		} else if(var0 instanceof Block) {
			Block var5 = (Block)var0;
			if(var5.getBlockName() != null) {
				var2 = var5.getBlockName() + ".name";
			}
		} else if(var0 instanceof ItemStack) {
			ItemStack var6 = (ItemStack)var0;
			if(var6.getItemName() != null) {
				var2 = var6.getItemName() + ".name";
			}
		} else {
			var3 = new Exception(var0.getClass().getName() + " cannot have name attached to it!");
			logger.throwing("ModLoader", "AddName", var3);
			ThrowException(var3);
		}

		if(var2 != null) {
			AddLocalization(var2, var1);
		} else {
			var3 = new Exception(var0 + " is missing name tag!");
			logger.throwing("ModLoader", "AddName", var3);
			ThrowException(var3);
		}

	}

	public static int addOverride(String var0, String var1) {
		try {
			int var2 = getUniqueSpriteIndex(var0);
			addOverride(var0, var1, var2);
			return var2;
		} catch (Throwable var3) {
			logger.throwing("ModLoader", "addOverride", var3);
			ThrowException(var3);
			throw new RuntimeException(var3);
		}
	}

	public static void addOverride(String var0, String var1, int var2) {
		boolean var3 = true;
		boolean var4 = false;
		byte var5;
		int var6;
		if(var0.equals("/terrain.png")) {
			var5 = 0;
			var6 = terrainSpritesLeft;
		} else {
			if(!var0.equals("/gui/items.png")) {
				return;
			}

			var5 = 1;
			var6 = itemSpritesLeft;
		}

		System.out.println("Overriding " + var0 + " with " + var1 + " @ " + var2 + ". " + var6 + " left.");
		logger.finer("addOverride(" + var0 + "," + var1 + "," + var2 + "). " + var6 + " left.");
		Object var7 = (Map)overrides.get(Integer.valueOf(var5));
		if(var7 == null) {
			var7 = new HashMap();
			overrides.put(Integer.valueOf(var5), (Map)var7);
		}

		((Map)var7).put(var1, Integer.valueOf(var2));
	}

	public static void AddRecipe(ItemStack var0, Object... var1) {
		CraftingManager.getInstance().addRecipe(var0, var1);
	}

	public static void AddShapelessRecipe(ItemStack var0, Object... var1) {
		CraftingManager.getInstance().addShapelessRecipe(var0, var1);
	}

	public static void AddSmelting(int var0, ItemStack var1) {
		FurnaceRecipes.smelting().addSmelting(var0, var1);
	}

	public static void AddSpawn(Class<? extends EntityLiving> var0, int var1, EnumCreatureType var2) {
		AddSpawn(var0, var1, var2, (BiomeGenBase[])null);
	}

	public static void AddSpawn(Class<? extends EntityLiving> var0, int var1, EnumCreatureType var2, BiomeGenBase... var3) {
		if(var0 == null) {
			throw new IllegalArgumentException("entityClass cannot be null");
		} else if(var2 == null) {
			throw new IllegalArgumentException("spawnList cannot be null");
		} else {
			if(var3 == null) {
				var3 = standardBiomes;
			}

			for(int var4 = 0; var4 < var3.length; ++var4) {
				List var5 = var3[var4].getSpawnableList(var2);
				if(var5 != null) {
					boolean var6 = false;
					Iterator var7 = var5.iterator();

					while(var7.hasNext()) {
						SpawnListEntry var8 = (SpawnListEntry)var7.next();
						if(var8.entityClass == var0) {
							var8.spawnRarityRate = var1;
							var6 = true;
							break;
						}
					}

					if(!var6) {
						var5.add(new SpawnListEntry(var0, var1));
					}
				}
			}

		}
	}

	public static void AddSpawn(String var0, int var1, EnumCreatureType var2) {
		AddSpawn(var0, var1, var2, (BiomeGenBase[])null);
	}

	public static void AddSpawn(String var0, int var1, EnumCreatureType var2, BiomeGenBase... var3) {
		Class var4 = (Class)classMap.get(var0);
		if(var4 != null && EntityLiving.class.isAssignableFrom(var4)) {
			AddSpawn(var4, var1, var2, var3);
		}

	}

	public static boolean DispenseEntity(World var0, double var1, double var3, double var5, int var7, int var8, ItemStack var9) {
		boolean var10 = false;

		for(Iterator var11 = modList.iterator(); var11.hasNext() && !var10; var10 = ((BaseMod)var11.next()).DispenseEntity(var0, var1, var3, var5, var7, var8, var9)) {
		}

		return var10;
	}

	public static List<BaseMod> getLoadedMods() {
		return Collections.unmodifiableList(modList);
	}

	public static Logger getLogger() {
		return logger;
	}

	public static Minecraft getMinecraftInstance() {
		if(instance == null) {
			try {
				ThreadGroup var0 = Thread.currentThread().getThreadGroup();
				int var1 = var0.activeCount();
				Thread[] var2 = new Thread[var1];
				var0.enumerate(var2);

				for(int var3 = 0; var3 < var2.length; ++var3) {
					if(var2[var3].getName().equals("Minecraft main thread")) {
						instance = (Minecraft)getPrivateValue(Thread.class, var2[var3], "target");
						break;
					}
				}
			} catch (SecurityException var4) {
				logger.throwing("ModLoader", "getMinecraftInstance", var4);
				throw new RuntimeException(var4);
			} catch (NoSuchFieldException var5) {
				logger.throwing("ModLoader", "getMinecraftInstance", var5);
				throw new RuntimeException(var5);
			}
		}

		return instance;
	}

	public static <T, E> T getPrivateValue(Class<? super E> var0, E var1, int var2) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		try {
			Field var3 = var0.getDeclaredFields()[var2];
			var3.setAccessible(true);
			return (T)var3.get(var1);
		} catch (IllegalAccessException var4) {
			logger.throwing("ModLoader", "getPrivateValue", var4);
			ThrowException("An impossible error has occured!", var4);
			return null;
		}
	}

	public static <T, E> T getPrivateValue(Class<? super E> var0, E var1, String var2) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		try {
			Field var3 = var0.getDeclaredField(var2);
			var3.setAccessible(true);
			return (T)var3.get(var1);
		} catch (IllegalAccessException var4) {
			logger.throwing("ModLoader", "getPrivateValue", var4);
			ThrowException("An impossible error has occured!", var4);
			return null;
		}
	}

	public static int getUniqueBlockModelID(BaseMod var0, boolean var1) {
		int var2 = nextBlockModelID++;
		blockModels.put(Integer.valueOf(var2), var0);
		blockSpecialInv.put(Integer.valueOf(var2), Boolean.valueOf(var1));
		return var2;
	}

	public static int getUniqueEntityId() {
		return highestEntityId++;
	}

	private static int getUniqueItemSpriteIndex() {
		while(itemSpriteIndex < usedItemSprites.length) {
			if(!usedItemSprites[itemSpriteIndex]) {
				usedItemSprites[itemSpriteIndex] = true;
				--itemSpritesLeft;
				return itemSpriteIndex++;
			}

			++itemSpriteIndex;
		}

		Exception var0 = new Exception("No more empty item sprite indices left!");
		logger.throwing("ModLoader", "getUniqueItemSpriteIndex", var0);
		ThrowException(var0);
		return 0;
	}

	public static int getUniqueSpriteIndex(String var0) {
		if(var0.equals("/gui/items.png")) {
			return getUniqueItemSpriteIndex();
		} else if(var0.equals("/terrain.png")) {
			return getUniqueTerrainSpriteIndex();
		} else {
			Exception var1 = new Exception("No registry for this texture: " + var0);
			logger.throwing("ModLoader", "getUniqueItemSpriteIndex", var1);
			ThrowException(var1);
			return 0;
		}
	}

	private static int getUniqueTerrainSpriteIndex() {
		while(terrainSpriteIndex < usedTerrainSprites.length) {
			if(!usedTerrainSprites[terrainSpriteIndex]) {
				usedTerrainSprites[terrainSpriteIndex] = true;
				--terrainSpritesLeft;
				return terrainSpriteIndex++;
			}

			++terrainSpriteIndex;
		}

		Exception var0 = new Exception("No more empty terrain sprite indices left!");
		logger.throwing("ModLoader", "getUniqueItemSpriteIndex", var0);
		ThrowException(var0);
		return 0;
	}

	private static void init() {
		hasInit = true;
		String var0 = "1111111111111111111111111111111111111101111111011111111111111001111111111111111111111111111011111111100110000011111110000000001111111001100000110000000100000011000000010000001100000000000000110000000000000000000000000000000000000000000000001100000000000000";
		String var1 = "1111111111111111111111111111110111111111111111111111110111111111111111111111000111111011111111111111001111111110111111111111100011111111000010001111011110000000111111000000000011111100000000001111000000000111111000000000001101000000000001111111111111000011";

		for(int var2 = 0; var2 < 256; ++var2) {
			usedItemSprites[var2] = var0.charAt(var2) == 49;
			if(!usedItemSprites[var2]) {
				++itemSpritesLeft;
			}

			usedTerrainSprites[var2] = var1.charAt(var2) == 49;
			if(!usedTerrainSprites[var2]) {
				++terrainSpritesLeft;
			}
		}

		try {
			instance = (Minecraft)getPrivateValue(Minecraft.class, (Minecraft)null, 1);
			instance.entityRenderer = new EntityRendererProxy(instance);
			classMap = (Map)getPrivateValue(EntityList.class, (EntityList)null, 0);
			field_modifiers = Field.class.getDeclaredField("modifiers");
			field_modifiers.setAccessible(true);
			field_blockList = Session.class.getDeclaredFields()[0];
			field_blockList.setAccessible(true);
			field_TileEntityRenderers = TileEntityRenderer.class.getDeclaredFields()[0];
			field_TileEntityRenderers.setAccessible(true);
			field_armorList = RenderPlayer.class.getDeclaredFields()[3];
			field_modifiers.setInt(field_armorList, field_armorList.getModifiers() & -17);
			field_armorList.setAccessible(true);
			field_animList = RenderEngine.class.getDeclaredFields()[6];
			field_animList.setAccessible(true);
			Field[] var15 = BiomeGenBase.class.getDeclaredFields();
			LinkedList var3 = new LinkedList();

			for(int var4 = 0; var4 < var15.length; ++var4) {
				Class var5 = var15[var4].getType();
				if((var15[var4].getModifiers() & 8) != 0 && var5.isAssignableFrom(BiomeGenBase.class)) {
					BiomeGenBase var6 = (BiomeGenBase)var15[var4].get((Object)null);
					if(!(var6 instanceof BiomeGenHell) && !(var6 instanceof BiomeGenSky)) {
						var3.add(var6);
					}
				}
			}

			standardBiomes = (BiomeGenBase[])((BiomeGenBase[])var3.toArray(new BiomeGenBase[0]));

			try {
				method_RegisterTileEntity = TileEntity.class.getDeclaredMethod("a", new Class[]{Class.class, String.class});
			} catch (NoSuchMethodException var8) {
				method_RegisterTileEntity = TileEntity.class.getDeclaredMethod("addMapping", new Class[]{Class.class, String.class});
			}

			method_RegisterTileEntity.setAccessible(true);

			try {
				method_RegisterEntityID = EntityList.class.getDeclaredMethod("a", new Class[]{Class.class, String.class, Integer.TYPE});
			} catch (NoSuchMethodException var7) {
				method_RegisterEntityID = EntityList.class.getDeclaredMethod("addMapping", new Class[]{Class.class, String.class, Integer.TYPE});
			}

			method_RegisterEntityID.setAccessible(true);
		} catch (SecurityException var10) {
			logger.throwing("ModLoader", "init", var10);
			ThrowException(var10);
			throw new RuntimeException(var10);
		} catch (NoSuchFieldException var11) {
			logger.throwing("ModLoader", "init", var11);
			ThrowException(var11);
			throw new RuntimeException(var11);
		} catch (NoSuchMethodException var12) {
			logger.throwing("ModLoader", "init", var12);
			ThrowException(var12);
			throw new RuntimeException(var12);
		} catch (IllegalArgumentException var13) {
			logger.throwing("ModLoader", "init", var13);
			ThrowException(var13);
			throw new RuntimeException(var13);
		} catch (IllegalAccessException var14) {
			logger.throwing("ModLoader", "init", var14);
			ThrowException(var14);
			throw new RuntimeException(var14);
		}

		try {
			loadConfig();
			if(props.containsKey("loggingLevel")) {
				cfgLoggingLevel = Level.parse(props.getProperty("loggingLevel"));
			}

			if(props.containsKey("grassFix")) {
				RenderBlocks.cfgGrassFix = Boolean.parseBoolean(props.getProperty("grassFix"));
			}

			logger.setLevel(cfgLoggingLevel);
			if((logfile.exists() || logfile.createNewFile()) && logfile.canWrite() && logHandler == null) {
				logHandler = new FileHandler(logfile.getPath());
				logHandler.setFormatter(new SimpleFormatter());
				logger.addHandler(logHandler);
			}

			logger.fine("ModLoader Beta 1.7.3 Initializing...");
			System.out.println("ModLoader Beta 1.7.3 Initializing...");
			File var16 = new File(ModLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			modDir.mkdirs();
			readFromModFolder(modDir);
			readFromClassPath(var16);
			System.out.println("Done.");
			props.setProperty("loggingLevel", cfgLoggingLevel.getName());
			props.setProperty("grassFix", Boolean.toString(RenderBlocks.cfgGrassFix));
			Iterator var17 = modList.iterator();

			while(var17.hasNext()) {
				BaseMod var18 = (BaseMod)var17.next();
				var18.ModsLoaded();
				if(!props.containsKey(var18.getClass().getName())) {
					props.setProperty(var18.getClass().getName(), "on");
				}
			}

			instance.gameSettings.keyBindings = RegisterAllKeys(instance.gameSettings.keyBindings);
			instance.gameSettings.loadOptions();
			initStats();
			saveConfig();
		} catch (Throwable var9) {
			logger.throwing("ModLoader", "init", var9);
			ThrowException("ModLoader has failed to initialize.", var9);
			if(logHandler != null) {
				logHandler.close();
			}

			throw new RuntimeException(var9);
		}
	}

	private static void initStats() {
		int var0;
		String var1;
		for(var0 = 0; var0 < Block.blocksList.length; ++var0) {
			if(!StatList.field_25169_C.containsKey(Integer.valueOf(16777216 + var0)) && Block.blocksList[var0] != null && Block.blocksList[var0].getEnableStats()) {
				var1 = StringTranslate.getInstance().translateKeyFormat("stat.mineBlock", new Object[]{Block.blocksList[var0].translateBlockName()});
				StatList.mineBlockStatArray[var0] = (new StatCrafting(16777216 + var0, var1, var0)).registerStat();
				StatList.field_25185_d.add(StatList.mineBlockStatArray[var0]);
			}
		}

		for(var0 = 0; var0 < Item.itemsList.length; ++var0) {
			if(!StatList.field_25169_C.containsKey(Integer.valueOf(16908288 + var0)) && Item.itemsList[var0] != null) {
				var1 = StringTranslate.getInstance().translateKeyFormat("stat.useItem", new Object[]{Item.itemsList[var0].getStatName()});
				StatList.field_25172_A[var0] = (new StatCrafting(16908288 + var0, var1, var0)).registerStat();
				if(var0 >= Block.blocksList.length) {
					StatList.field_25186_c.add(StatList.field_25172_A[var0]);
				}
			}

			if(!StatList.field_25169_C.containsKey(Integer.valueOf(16973824 + var0)) && Item.itemsList[var0] != null && Item.itemsList[var0].isDamagable()) {
				var1 = StringTranslate.getInstance().translateKeyFormat("stat.breakItem", new Object[]{Item.itemsList[var0].getStatName()});
				StatList.field_25170_B[var0] = (new StatCrafting(16973824 + var0, var1, var0)).registerStat();
			}
		}

		HashSet var2 = new HashSet();
		Iterator var3 = CraftingManager.getInstance().getRecipeList().iterator();

		Object var4;
		while(var3.hasNext()) {
			var4 = var3.next();
			var2.add(Integer.valueOf(((IRecipe)var4).getRecipeOutput().itemID));
		}

		var3 = FurnaceRecipes.smelting().getSmeltingList().values().iterator();

		while(var3.hasNext()) {
			var4 = var3.next();
			var2.add(Integer.valueOf(((ItemStack)var4).itemID));
		}

		var3 = var2.iterator();

		while(var3.hasNext()) {
			int var5 = ((Integer)var3.next()).intValue();
			if(!StatList.field_25169_C.containsKey(Integer.valueOf(16842752 + var5)) && Item.itemsList[var5] != null) {
				String var6 = StringTranslate.getInstance().translateKeyFormat("stat.craftItem", new Object[]{Item.itemsList[var5].getStatName()});
				StatList.field_25158_z[var5] = (new StatCrafting(16842752 + var5, var6, var5)).registerStat();
			}
		}

	}

	public static boolean isGUIOpen(Class<? extends GuiScreen> var0) {
		Minecraft var1 = getMinecraftInstance();
		return var0 == null ? var1.currentScreen == null : (var1.currentScreen == null && var0 != null ? false : var0.isInstance(var1.currentScreen));
	}

	public static boolean isModLoaded(String var0) {
		Class var1 = null;

		try {
			var1 = Class.forName(var0);
		} catch (ClassNotFoundException var4) {
			return false;
		}

		if(var1 != null) {
			Iterator var2 = modList.iterator();

			while(var2.hasNext()) {
				BaseMod var3 = (BaseMod)var2.next();
				if(var1.isInstance(var3)) {
					return true;
				}
			}
		}

		return false;
	}

	public static void loadConfig() throws IOException {
		cfgdir.mkdir();
		if((cfgfile.exists() || cfgfile.createNewFile()) && cfgfile.canRead()) {
			FileInputStream var0 = new FileInputStream(cfgfile);
			props.load(var0);
			var0.close();
		}

	}

	public static BufferedImage loadImage(RenderEngine var0, String var1) throws Exception {
		TexturePackList var2 = (TexturePackList)getPrivateValue(RenderEngine.class, var0, 11);
		InputStream var3 = var2.selectedTexturePack.getResourceAsStream(var1);
		if(var3 == null) {
			throw new Exception("Image not found: " + var1);
		} else {
			BufferedImage var4 = ImageIO.read(var3);
			if(var4 == null) {
				throw new Exception("Image corrupted: " + var1);
			} else {
				return var4;
			}
		}
	}

	public static void OnItemPickup(EntityPlayer var0, ItemStack var1) {
		Iterator var2 = modList.iterator();

		while(var2.hasNext()) {
			BaseMod var3 = (BaseMod)var2.next();
			var3.OnItemPickup(var0, var1);
		}

	}

	public static void OnTick(Minecraft var0) {
		if(!hasInit) {
			init();
			logger.fine("Initialized");
		}

		if(texPack == null || var0.gameSettings.skin != texPack) {
			texturesAdded = false;
			texPack = var0.gameSettings.skin;
		}

		if(!texturesAdded && var0.renderEngine != null) {
			RegisterAllTextureOverrides(var0.renderEngine);
			texturesAdded = true;
		}

		long var1 = 0L;
		Iterator var3;
		Entry var4;
		if(var0.theWorld != null) {
			var1 = var0.theWorld.getWorldTime();
			var3 = inGameHooks.entrySet().iterator();

			label93:
			while(true) {
				do {
					if(!var3.hasNext()) {
						break label93;
					}

					var4 = (Entry)var3.next();
				} while(clock == var1 && ((Boolean)var4.getValue()).booleanValue());

				if(!((BaseMod)var4.getKey()).OnTickInGame(var0)) {
					var3.remove();
				}
			}
		}

		if(var0.currentScreen != null) {
			var3 = inGUIHooks.entrySet().iterator();

			label80:
			while(true) {
				do {
					if(!var3.hasNext()) {
						break label80;
					}

					var4 = (Entry)var3.next();
				} while(clock == var1 && ((Boolean)var4.getValue()).booleanValue() & var0.theWorld != null);

				if(!((BaseMod)var4.getKey()).OnTickInGUI(var0, var0.currentScreen)) {
					var3.remove();
				}
			}
		}

		if(clock != var1) {
			Iterator var5 = keyList.entrySet().iterator();

			label66:
			while(var5.hasNext()) {
				Entry var6 = (Entry)var5.next();
				Iterator var7 = ((Map)var6.getValue()).entrySet().iterator();

				while(true) {
					Entry var8;
					boolean var9;
					boolean[] var10;
					boolean var11;
					do {
						do {
							if(!var7.hasNext()) {
								continue label66;
							}

							var8 = (Entry)var7.next();
							var9 = Keyboard.isKeyDown(((KeyBinding)var8.getKey()).keyCode);
							var10 = (boolean[])((boolean[])var8.getValue());
							var11 = var10[1];
							var10[1] = var9;
						} while(!var9);
					} while(var11 && !var10[0]);

					((BaseMod)var6.getKey()).KeyboardEvent((KeyBinding)var8.getKey());
				}
			}
		}

		clock = var1;
	}

	public static void OpenGUI(EntityPlayer var0, GuiScreen var1) {
		if(!hasInit) {
			init();
			logger.fine("Initialized");
		}

		Minecraft var2 = getMinecraftInstance();
		if(var2.thePlayer == var0 && var1 != null) {
			var2.displayGuiScreen(var1);
		}

	}

	public static void PopulateChunk(IChunkProvider var0, int var1, int var2, World var3) {
		if(!hasInit) {
			init();
			logger.fine("Initialized");
		}

		Random var4 = new Random(var3.getRandomSeed());
		long var5 = var4.nextLong() / 2L * 2L + 1L;
		long var7 = var4.nextLong() / 2L * 2L + 1L;
		var4.setSeed((long)var1 * var5 + (long)var2 * var7 ^ var3.getRandomSeed());
		Iterator var9 = modList.iterator();

		while(var9.hasNext()) {
			BaseMod var10 = (BaseMod)var9.next();
			if(var0.makeString().equals("RandomLevelSource")) {
				var10.GenerateSurface(var3, var4, var1 << 4, var2 << 4);
			} else if(var0.makeString().equals("HellRandomLevelSource")) {
				var10.GenerateNether(var3, var4, var1 << 4, var2 << 4);
			}
		}

	}

	private static void readFromClassPath(File var0) throws FileNotFoundException, IOException {
		ArrayList<String> modNames = new ArrayList<String>();
		logger.finer("Adding mods from " + var0.getCanonicalPath());
		ClassLoader var1 = ModLoader.class.getClassLoader();
		String var2;
		if(var0.isFile() && (var0.getName().endsWith(".jar") || var0.getName().endsWith(".zip"))) {
			logger.finer("Zip found.");
			FileInputStream var6 = new FileInputStream(var0);
			ZipInputStream var8 = new ZipInputStream(var6);
			ZipEntry var9 = null;

			while(true) {
				var9 = var8.getNextEntry();
				if(var9 == null) {
					var6.close();
					break;
				}

				var2 = var9.getName();
				if(!var9.isDirectory() && var2.startsWith("mod_") && var2.endsWith(".class")) {
					modNames.add(var2);
					// addMod(var1, var2);
				}
			}
		} else if(var0.isDirectory()) {
			Package var3 = ModLoader.class.getPackage();
			if(var3 != null) {
				String var4 = var3.getName().replace('.', File.separatorChar);
				var0 = new File(var0, var4);
			}

			logger.finer("Directory found.");
			File[] var7 = var0.listFiles();
			if(var7 != null) {
				for(int var5 = 0; var5 < var7.length; ++var5) {
					var2 = var7[var5].getName();
					if(var7[var5].isFile() && var2.startsWith("mod_") && var2.endsWith(".class")) {
						modNames.add(var2);
						// addMod(var1, var2);
					}
				}
			}
		}
	    String currentDir = System.getProperty("user.dir");
	    System.out.println("Current directory: " + currentDir);
		ArrayList<String> loadNames = new ArrayList<String>();
		// get load order, if any
		String filePath = "load-order.txt"; // Replace with the actual path
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Process each line here
                // System.out.println(line);
            	loadNames.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        // iterate the load order, and remove any we find
        for(String ln : loadNames) {
			if(modNames.contains(ln)) {
				addMod(var1, ln);
				modNames.remove(ln);
			}
		}
        
		// iterate all mod names in alphabetical order
		Collections.sort(modNames, String.CASE_INSENSITIVE_ORDER);
		for(String mn : modNames) {
			addMod(var1, mn);
		}

	}

	private static void readFromModFolder(File var0) throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		ClassLoader var1 = Minecraft.class.getClassLoader();
		Method var2 = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
		var2.setAccessible(true);
		if(!var0.isDirectory()) {
			throw new IllegalArgumentException("folder must be a Directory.");
		} else {
			File[] var3 = var0.listFiles();
			int var4;
			File var5;
			if(var1 instanceof URLClassLoader) {
				for(var4 = 0; var4 < var3.length; ++var4) {
					var5 = var3[var4];
					if(var5.isDirectory() || var5.isFile() && (var5.getName().endsWith(".jar") || var5.getName().endsWith(".zip"))) {
						var2.invoke(var1, new Object[]{var5.toURI().toURL()});
					}
				}
			}

			for(var4 = 0; var4 < var3.length; ++var4) {
				var5 = var3[var4];
				if(var5.isDirectory() || var5.isFile() && (var5.getName().endsWith(".jar") || var5.getName().endsWith(".zip"))) {
					logger.finer("Adding mods from " + var5.getCanonicalPath());
					String var6;
					if(!var5.isFile()) {
						if(var5.isDirectory()) {
							Package var10 = ModLoader.class.getPackage();
							if(var10 != null) {
								String var11 = var10.getName().replace('.', File.separatorChar);
								var5 = new File(var5, var11);
							}

							logger.finer("Directory found.");
							File[] var12 = var5.listFiles();
							if(var12 != null) {
								for(int var13 = 0; var13 < var12.length; ++var13) {
									var6 = var12[var13].getName();
									if(var12[var13].isFile() && var6.startsWith("mod_") && var6.endsWith(".class")) {
										addMod(var1, var6);
									}
								}
							}
						}
					} else {
						logger.finer("Zip found.");
						FileInputStream var7 = new FileInputStream(var5);
						ZipInputStream var8 = new ZipInputStream(var7);
						ZipEntry var9 = null;

						while(true) {
							var9 = var8.getNextEntry();
							if(var9 == null) {
								var8.close();
								var7.close();
								break;
							}

							var6 = var9.getName();
							if(!var9.isDirectory() && var6.startsWith("mod_") && var6.endsWith(".class")) {
								addMod(var1, var6);
							}
						}
					}
				}
			}

		}
	}

	public static KeyBinding[] RegisterAllKeys(KeyBinding[] var0) {
		LinkedList var1 = new LinkedList();
		var1.addAll(Arrays.asList(var0));
		Iterator var2 = keyList.values().iterator();

		while(var2.hasNext()) {
			Map var3 = (Map)var2.next();
			var1.addAll(var3.keySet());
		}

		return (KeyBinding[])((KeyBinding[])var1.toArray(new KeyBinding[0]));
	}

	public static void RegisterAllTextureOverrides(RenderEngine var0) {
		animList.clear();
		Minecraft var1 = getMinecraftInstance();
		Iterator var2 = modList.iterator();

		while(var2.hasNext()) {
			BaseMod var3 = (BaseMod)var2.next();
			var3.RegisterAnimation(var1);
		}

		var2 = animList.iterator();

		while(var2.hasNext()) {
			TextureFX var12 = (TextureFX)var2.next();
			var0.registerTextureFX(var12);
		}

		var2 = overrides.entrySet().iterator();

		while(var2.hasNext()) {
			Entry var13 = (Entry)var2.next();
			Iterator var4 = ((Map)var13.getValue()).entrySet().iterator();

			while(var4.hasNext()) {
				Entry var5 = (Entry)var4.next();
				String var6 = (String)var5.getKey();
				int var7 = ((Integer)var5.getValue()).intValue();
				int var8 = ((Integer)var13.getKey()).intValue();

				try {
					BufferedImage var9 = loadImage(var0, var6);
					ModTextureStatic var10 = new ModTextureStatic(var7, var8, var9);
					var0.registerTextureFX(var10);
				} catch (Exception var11) {
					logger.throwing("ModLoader", "RegisterAllTextureOverrides", var11);
					ThrowException(var11);
					throw new RuntimeException(var11);
				}
			}
		}

	}

	public static void RegisterBlock(Block var0) {
		RegisterBlock(var0, (Class)null);
	}

	public static void RegisterBlock(Block var0, Class<? extends ItemBlock> var1) {
		try {
			if(var0 == null) {
				throw new IllegalArgumentException("block parameter cannot be null.");
			}

			List var2 = (List)field_blockList.get((Object)null);
			var2.add(var0);
			int var3 = var0.blockID;
			ItemBlock var4 = null;
			if(var1 != null) {
				var4 = (ItemBlock)var1.getConstructor(new Class[]{Integer.TYPE}).newInstance(new Object[]{Integer.valueOf(var3 - 256)});
			} else {
				var4 = new ItemBlock(var3 - 256);
			}

			if(Block.blocksList[var3] != null && Item.itemsList[var3] == null) {
				Item.itemsList[var3] = var4;
			}
		} catch (IllegalArgumentException var5) {
			logger.throwing("ModLoader", "RegisterBlock", var5);
			ThrowException(var5);
		} catch (IllegalAccessException var6) {
			logger.throwing("ModLoader", "RegisterBlock", var6);
			ThrowException(var6);
		} catch (SecurityException var7) {
			logger.throwing("ModLoader", "RegisterBlock", var7);
			ThrowException(var7);
		} catch (InstantiationException var8) {
			logger.throwing("ModLoader", "RegisterBlock", var8);
			ThrowException(var8);
		} catch (InvocationTargetException var9) {
			logger.throwing("ModLoader", "RegisterBlock", var9);
			ThrowException(var9);
		} catch (NoSuchMethodException var10) {
			logger.throwing("ModLoader", "RegisterBlock", var10);
			ThrowException(var10);
		}

	}

	public static void RegisterEntityID(Class<? extends Entity> var0, String var1, int var2) {
		try {
			method_RegisterEntityID.invoke((Object)null, new Object[]{var0, var1, Integer.valueOf(var2)});
		} catch (IllegalArgumentException var4) {
			logger.throwing("ModLoader", "RegisterEntityID", var4);
			ThrowException(var4);
		} catch (IllegalAccessException var5) {
			logger.throwing("ModLoader", "RegisterEntityID", var5);
			ThrowException(var5);
		} catch (InvocationTargetException var6) {
			logger.throwing("ModLoader", "RegisterEntityID", var6);
			ThrowException(var6);
		}

	}

	public static void RegisterKey(BaseMod var0, KeyBinding var1, boolean var2) {
		Map<KeyBinding, boolean[]> var3 = (Map)keyList.get(var0);
		if(var3 == null) {
			var3 = new HashMap();
		}

		((Map)var3).put(var1, new boolean[]{var2, false});
		keyList.put(var0, var3);
	}

	public static void RegisterTileEntity(Class<? extends TileEntity> var0, String var1) {
		RegisterTileEntity(var0, var1, (TileEntitySpecialRenderer)null);
	}

	public static void RegisterTileEntity(Class<? extends TileEntity> var0, String var1, TileEntitySpecialRenderer var2) {
		try {
			method_RegisterTileEntity.invoke((Object)null, new Object[]{var0, var1});
			if(var2 != null) {
				TileEntityRenderer var3 = TileEntityRenderer.instance;
				Map var4 = (Map)field_TileEntityRenderers.get(var3);
				var4.put(var0, var2);
				var2.setTileEntityRenderer(var3);
			}
		} catch (IllegalArgumentException var5) {
			logger.throwing("ModLoader", "RegisterTileEntity", var5);
			ThrowException(var5);
		} catch (IllegalAccessException var6) {
			logger.throwing("ModLoader", "RegisterTileEntity", var6);
			ThrowException(var6);
		} catch (InvocationTargetException var7) {
			logger.throwing("ModLoader", "RegisterTileEntity", var7);
			ThrowException(var7);
		}

	}

	public static void RemoveSpawn(Class<? extends EntityLiving> var0, EnumCreatureType var1) {
		RemoveSpawn(var0, var1, (BiomeGenBase[])null);
	}

	public static void RemoveSpawn(Class<? extends EntityLiving> var0, EnumCreatureType var1, BiomeGenBase... var2) {
		if(var0 == null) {
			throw new IllegalArgumentException("entityClass cannot be null");
		} else if(var1 == null) {
			throw new IllegalArgumentException("spawnList cannot be null");
		} else {
			if(var2 == null) {
				var2 = standardBiomes;
			}

			for(int var3 = 0; var3 < var2.length; ++var3) {
				List var4 = var2[var3].getSpawnableList(var1);
				if(var4 != null) {
					Iterator var5 = var4.iterator();

					while(var5.hasNext()) {
						SpawnListEntry var6 = (SpawnListEntry)var5.next();
						if(var6.entityClass == var0) {
							var5.remove();
						}
					}
				}
			}

		}
	}

	public static void RemoveSpawn(String var0, EnumCreatureType var1) {
		RemoveSpawn(var0, var1, (BiomeGenBase[])null);
	}

	public static void RemoveSpawn(String var0, EnumCreatureType var1, BiomeGenBase... var2) {
		Class var3 = (Class)classMap.get(var0);
		if(var3 != null && EntityLiving.class.isAssignableFrom(var3)) {
			RemoveSpawn(var3, var1, var2);
		}

	}

	public static boolean RenderBlockIsItemFull3D(int var0) {
		return !blockSpecialInv.containsKey(Integer.valueOf(var0)) ? var0 == 16 : ((Boolean)blockSpecialInv.get(Integer.valueOf(var0))).booleanValue();
	}

	public static void RenderInvBlock(RenderBlocks var0, Block var1, int var2, int var3) {
		BaseMod var4 = (BaseMod)blockModels.get(Integer.valueOf(var3));
		if(var4 != null) {
			var4.RenderInvBlock(var0, var1, var2, var3);
		}

	}

	public static boolean RenderWorldBlock(RenderBlocks var0, IBlockAccess var1, int var2, int var3, int var4, Block var5, int var6) {
		BaseMod var7 = (BaseMod)blockModels.get(Integer.valueOf(var6));
		return var7 == null ? false : var7.RenderWorldBlock(var0, var1, var2, var3, var4, var5, var6);
	}

	public static void saveConfig() throws IOException {
		cfgdir.mkdir();
		if((cfgfile.exists() || cfgfile.createNewFile()) && cfgfile.canWrite()) {
			FileOutputStream var0 = new FileOutputStream(cfgfile);
			props.store(var0, "ModLoader Config");
			var0.close();
		}

	}

	public static void SetInGameHook(BaseMod var0, boolean var1, boolean var2) {
		if(var1) {
			inGameHooks.put(var0, Boolean.valueOf(var2));
		} else {
			inGameHooks.remove(var0);
		}

	}

	public static void SetInGUIHook(BaseMod var0, boolean var1, boolean var2) {
		if(var1) {
			inGUIHooks.put(var0, Boolean.valueOf(var2));
		} else {
			inGUIHooks.remove(var0);
		}

	}

	public static <T, E> void setPrivateValue(Class<? super T> var0, T var1, int var2, E var3) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		try {
			Field var4 = var0.getDeclaredFields()[var2];
			var4.setAccessible(true);
			int var5 = field_modifiers.getInt(var4);
			if((var5 & 16) != 0) {
				field_modifiers.setInt(var4, var5 & -17);
			}

			var4.set(var1, var3);
		} catch (IllegalAccessException var6) {
			logger.throwing("ModLoader", "setPrivateValue", var6);
			ThrowException("An impossible error has occured!", var6);
		}

	}

	public static <T, E> void setPrivateValue(Class<? super T> var0, T var1, String var2, E var3) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		try {
			Field var4 = var0.getDeclaredField(var2);
			int var5 = field_modifiers.getInt(var4);
			if((var5 & 16) != 0) {
				field_modifiers.setInt(var4, var5 & -17);
			}

			var4.setAccessible(true);
			var4.set(var1, var3);
		} catch (IllegalAccessException var6) {
			logger.throwing("ModLoader", "setPrivateValue", var6);
			ThrowException("An impossible error has occured!", var6);
		}

	}

	private static void setupProperties(Class<? extends BaseMod> var0) throws IllegalArgumentException, IllegalAccessException, IOException, SecurityException, NoSuchFieldException {
		Properties var1 = new Properties();
		File var2 = new File(cfgdir, var0.getName() + ".cfg");
		if(var2.exists() && var2.canRead()) {
			var1.load(new FileInputStream(var2));
		}

		StringBuilder var3 = new StringBuilder();
		Field[] var4 = var0.getFields();
		int var5 = var4.length;

		for(int var6 = 0; var6 < var5; ++var6) {
			Field var7 = var4[var6];
			if((var7.getModifiers() & 8) != 0 && var7.isAnnotationPresent(MLProp.class)) {
				Class var8 = var7.getType();
				MLProp var9 = (MLProp)var7.getAnnotation(MLProp.class);
				String var10 = var9.name().length() == 0 ? var7.getName() : var9.name();
				Object var11 = var7.get((Object)null);
				StringBuilder var12 = new StringBuilder();
				if(var9.min() != -Double.POSITIVE_INFINITY) {
					var12.append(String.format(",>=%.1f", new Object[]{Double.valueOf(var9.min())}));
				}

				if(var9.max() != Double.POSITIVE_INFINITY) {
					var12.append(String.format(",<=%.1f", new Object[]{Double.valueOf(var9.max())}));
				}

				StringBuilder var13 = new StringBuilder();
				if(var9.info().length() > 0) {
					var13.append(" -- ");
					var13.append(var9.info());
				}

				var3.append(String.format("%s (%s:%s%s)%s\n", new Object[]{var10, var8.getName(), var11, var12, var13}));
				if(var1.containsKey(var10)) {
					String var14 = var1.getProperty(var10);
					Object var15 = null;
					if(var8.isAssignableFrom(String.class)) {
						var15 = var14;
					} else if(var8.isAssignableFrom(Integer.TYPE)) {
						var15 = Integer.valueOf(Integer.parseInt(var14));
					} else if(var8.isAssignableFrom(Short.TYPE)) {
						var15 = Short.valueOf(Short.parseShort(var14));
					} else if(var8.isAssignableFrom(Byte.TYPE)) {
						var15 = Byte.valueOf(Byte.parseByte(var14));
					} else if(var8.isAssignableFrom(Boolean.TYPE)) {
						var15 = Boolean.valueOf(Boolean.parseBoolean(var14));
					} else if(var8.isAssignableFrom(Float.TYPE)) {
						var15 = Float.valueOf(Float.parseFloat(var14));
					} else if(var8.isAssignableFrom(Double.TYPE)) {
						var15 = Double.valueOf(Double.parseDouble(var14));
					}

					if(var15 != null) {
						if(var15 instanceof Number) {
							double var16 = ((Number)var15).doubleValue();
							if(var9.min() != -Double.POSITIVE_INFINITY && var16 < var9.min() || var9.max() != Double.POSITIVE_INFINITY && var16 > var9.max()) {
								continue;
							}
						}

						logger.finer(var10 + " set to " + var15);
						if(!var15.equals(var11)) {
							var7.set((Object)null, var15);
						}
					}
				} else {
					logger.finer(var10 + " not in config, using default: " + var11);
					var1.setProperty(var10, var11.toString());
				}
			}
		}

		if(!var1.isEmpty() && (var2.exists() || var2.createNewFile()) && var2.canWrite()) {
			var1.store(new FileOutputStream(var2), var3.toString());
		}

	}

	public static void TakenFromCrafting(EntityPlayer var0, ItemStack var1) {
		Iterator var2 = modList.iterator();

		while(var2.hasNext()) {
			BaseMod var3 = (BaseMod)var2.next();
			var3.TakenFromCrafting(var0, var1);
		}

	}

	public static void TakenFromFurnace(EntityPlayer var0, ItemStack var1) {
		Iterator var2 = modList.iterator();

		while(var2.hasNext()) {
			BaseMod var3 = (BaseMod)var2.next();
			var3.TakenFromFurnace(var0, var1);
		}

	}

	public static void ThrowException(String var0, Throwable var1) {
		Minecraft var2 = getMinecraftInstance();
		if(var2 != null) {
			var2.displayUnexpectedThrowable(new UnexpectedThrowable(var0, var1));
		} else {
			throw new RuntimeException(var1);
		}
	}

	private static void ThrowException(Throwable var0) {
		ThrowException("Exception occured in ModLoader", var0);
	}
}
