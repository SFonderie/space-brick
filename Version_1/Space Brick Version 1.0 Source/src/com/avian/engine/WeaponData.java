package com.avian.engine;

public class WeaponData 
{
	public static final int NUM_OF_WEAPONS = 12;
	
	public static Weapon getWeapon(int id)
	{
		id = Utilities.clamp(id, 0, NUM_OF_WEAPONS);
		
		Weapon[] weapons = {
				new Weapon("NO WEAPON ARMED", 0, 0,  0,  0,   false),
				new Weapon("THERMAL SIDEARM", 1, 16, 12, 72,  false),
				new Weapon("GENERIC CARBINE", 2, 32, 8,  120, false),
				new Weapon("PUMPING SHOTGUN", 3, 8,  24, 120, true),
				new Weapon("PLASMA INDUCTOR", 4, 54, 4,  200, false),
				new Weapon("PHOTON LAUNCHER", 5, 18, 18, 100, true),
				new Weapon("FLORPITRON-5000", 6, 10, 40, 160, true),
				new Weapon("ENERGY CROSSBOW", 7, 4,  80, 80,  true)
		};
		
		return weapons[id];
	}
	
	public static class Weapon
	{	
		public int ID;
		public int CLIP_SIZE;
		public int FIRE_DELAY;
		public int RELOAD_TIME;
		
		public boolean SEMI;
		
		public String GUN_NAME;
		
		public Weapon(String name, int id, int clipSize, int fireDelay, int reloadTime, boolean semiAuto)
		{
			ID = id;
			CLIP_SIZE = clipSize;
			FIRE_DELAY = fireDelay;
			RELOAD_TIME = reloadTime;
			GUN_NAME = name;
			SEMI = semiAuto;
		}
	}
}
