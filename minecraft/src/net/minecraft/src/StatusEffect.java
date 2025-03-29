package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatusEffect {
	public static Map<String, StatusEffect> StatusEffects = new HashMap<String, StatusEffect>();
	public String name;
	public String startMessage;
	public String endMessage;
	public double x;
	public double y;
	public double z;
	public float dmg;
	public int duration;
	public int dps;
	public int mod;
	public float miningSpeed;
	public boolean mineInWater = false;
	public boolean moveInWater = false;
	public boolean featherFall = false;
	public boolean waterWalking = false;
	public ArrayList<StatusEffectBlock> fortuneBlocks = new ArrayList<StatusEffectBlock>();
	public ArrayList<StatusEffectBlock> builderBlocks = new ArrayList<StatusEffectBlock>();
	
	public StatusEffect(String name, int duration) {
		this.name = name;
		this.duration = duration;
		StatusEffects.put(name, this);
	}
	
	public StatusEffect(StatusEffect e, int level) {
		this.name = e.name;
		this.duration = e.duration;
		this.x = e.x * level;
		this.y = e.y * level;
		this.z = e.z * level;
		this.dmg = e.dmg * level;
		this.duration = e.duration * level;
		this.dps = e.dps * level;
		this.mod = e.mod * level;
		this.miningSpeed = e.miningSpeed * level;
	}
}
