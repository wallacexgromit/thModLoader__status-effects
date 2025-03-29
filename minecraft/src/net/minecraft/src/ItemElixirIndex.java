package net.minecraft.src;

public class ItemElixirIndex {
	public int id;
	public int iconIdx;
	public String name;
	public StatusEffect[] statusEffects;
	
	public ItemElixirIndex(int idx, String name, StatusEffect[] se) {
		this(idx, name);
		this.statusEffects = se;
	}
	
	public ItemElixirIndex(int idx, String name) {
		this.iconIdx = idx;
		this.name = name;
	}
}
