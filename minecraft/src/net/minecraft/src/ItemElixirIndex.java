package net.minecraft.src;

public class ItemElixirIndex {
	public int id;
	public int iconIdx;
	public String name;
	public String displayName;
	public StatusEffect[] statusEffects;
	
	public ItemElixirIndex(int idx, String name, String displayName, StatusEffect[] se) {
		this(idx, name, displayName);
		this.statusEffects = se;
	}
	
	public ItemElixirIndex(int idx, String name, String displayName) {
		this.iconIdx = idx;
		this.name = name;
		this.displayName = displayName;
	}
}
