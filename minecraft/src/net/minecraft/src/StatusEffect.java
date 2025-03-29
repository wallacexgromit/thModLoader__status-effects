package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatusEffect {
	public static Map<String, StatusEffect> StatusEffects = new HashMap<String, StatusEffect>();
	public String name;
	public String startMessage;
	public String endMessage;
	public int duration = 0;
	public double x = 0;
	public double y = 0;
	public double z = 0;
	public float dmg = 0;
	public int dps = 0;
	public int mod = 0;
	public float miningSpeed = 0;
	public boolean mineInWater = false;
	public float waterSpeed = 0;
	public boolean featherFall = false;
	public boolean waterWalking = false;
	public boolean waterBreath = false;
	public ArrayList<StatusEffectBlock> fortuneBlocks = new ArrayList<StatusEffectBlock>();
	public ArrayList<StatusEffectBlock> builderBlocks = new ArrayList<StatusEffectBlock>();
	
	public StatusEffect(String name, int duration) {
		this.name = name;
		this.duration = duration;
		StatusEffects.put(name, this);
	}
	
	public StatusEffect(StatusEffect e, int level) {
		this.name = e.name;
		this.startMessage = e.startMessage;
		this.endMessage = e.endMessage;
		this.duration = e.duration;
		this.x = e.x * level;
		this.y = e.y * level;
		this.z = e.z * level;
		this.dmg = e.dmg * level;
		this.dps = e.dps * level;
		this.mod = e.mod;
		this.miningSpeed = e.miningSpeed * level;
		this.mineInWater = e.mineInWater;
		this.waterSpeed = e.waterSpeed * level;
		this.featherFall = e.featherFall;
		this.waterWalking = e.waterWalking;
		this.waterBreath = e.waterBreath;
		this.fortuneBlocks = e.fortuneBlocks;
		this.builderBlocks = e.builderBlocks;
	}
}
