package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class thModLoaderListToolMaterial {
	public static List<thModLoaderListToolMaterial> materials = new ArrayList();
	private final int harvestLevel;
	private final int maxUses;
	private final float efficiencyOnProperMaterial;
	private final int damageVsEntity;

	public thModLoaderListToolMaterial(int var1, int var2, float var3, int var4) {
		this.harvestLevel = var1;
		this.maxUses = var2;
		this.efficiencyOnProperMaterial = var3;
		this.damageVsEntity = var4;
		materials.add(this);
	}

	public int getMaxUses() {
		return this.maxUses;
	}

	public float getEfficiencyOnProperMaterial() {
		return this.efficiencyOnProperMaterial;
	}

	public int getDamageVsEntity() {
		return this.damageVsEntity;
	}

	public int getHarvestLevel() {
		return this.harvestLevel;
	}
}
