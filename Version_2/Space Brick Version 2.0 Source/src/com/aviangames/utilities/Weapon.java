package com.aviangames.utilities;

public final class Weapon 
{
	public static final Weapon[] WEAPONS = {
			new Weapon(1.0, 0, 0.0, "UNARMED", 0),
			new Weapon(5.0, 16, 1.2, "THERMAL SIDEARM", 8),
			new Weapon(7.5, 32, 2.4, "MURICA SUPREME", 4),
			new Weapon(2.5, 8, 3.0, "REDNECK HOME DEFENSE", 2),
			new Weapon(0.75, 4, 1.33, "TOKEN CROSSBOW", 12),
			new Weapon(15.0, 54, 4.0, "ALIEN BEAM THING", 3),
			new Weapon(25.0, 80, 5.0, "LASER CANNON", 3),
			new Weapon(2.0, 10, 1.5, "INCEPTION NOISE", 12),
			new Weapon(3.0, 18, 1.5, "YELLOW DOOMSDAY", 8),
			new Weapon(1.5, 10, 2.5, "FLORPITRON 5000", 10),
			new Weapon(1.0, 6, 1.8, "SHINY NOOB TUBE", 12),
			new Weapon(0.5, 1, 2.0, "MAXIMUM SPEED", 20)
	};
	
	public final double FIRETIME;
	
	public final int CLIPSIZE;
	
	public final double RELOAD;
	
	public final String TITLE;
	
	public final int DAMAGE;
	
	public Weapon(double firerate, int clipsize, double reload, String title, int damage)
	{
		this.FIRETIME = 1 / firerate;
		this.CLIPSIZE = clipsize;
		this.RELOAD = reload;
		this.TITLE = title;
		this.DAMAGE = damage;
	}
}
