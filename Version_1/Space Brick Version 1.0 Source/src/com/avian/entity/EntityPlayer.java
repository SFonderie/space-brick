package com.avian.entity;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import com.avian.engine.Utilities;
import com.avian.engine.Visuals;
import com.avian.engine.WeaponData;
import com.avian.level.GameLevel;

public class EntityPlayer extends GameEntity
{
	private BufferedImage[][] head, torso, arms;
	private boolean[] weaponObtained = new boolean[WeaponData.NUM_OF_WEAPONS];
	private int[] ammoInClip = new int[WeaponData.NUM_OF_WEAPONS];
	
	public int MAIN_CYCLE, ARMS_CYCLE, WALK_CYCLE, EXPRESSION;
	public int LOOK_X, LOOK_Y;
	public int AGE_AT_FIRE, AGE_AT_RELOAD;
	public boolean FORWARD, BACKWARD, LEFT, RIGHT;
	public boolean IS_FIRING, IS_RELOADING, CROSSHAIR;
	
	public WeaponData.Weapon CURRENT_WEAPON;
	
	public int MAX_HEALTH, HEALTH, CURRENT_AMMO;
	public int[] TOTAL_AMMO = new int[WeaponData.NUM_OF_WEAPONS];
	
	public EntityPlayer(GameLevel level, int xPos, int yPos, int drawX, int drawY, double theta)
	{
		super(level, xPos, yPos, drawX, drawY, theta);
		addHitboxes(new Hitbox[]{
				new Hitbox(0, 0, DRAWX / 2, DRAWY, false), 		//WRAPPING HITBOX
				new Hitbox(0, 0, DRAWX / 2, DRAWY / 2, true)	//COLLIDER HITBOX
		});
		
		head = Visuals.cut(LEVEL.STATE.FRAME.ENGINE.UTIL.E_PLAYER_HEAD, 64, 64);
		torso = Visuals.cut(LEVEL.STATE.FRAME.ENGINE.UTIL.E_PLAYER_TORSO, 64, 64);
		arms = Visuals.cut(LEVEL.STATE.FRAME.ENGINE.UTIL.E_PLAYER_ARMS, 64, 64);
		
		MOVE_SPEED = 8;
		EXPRESSION = 0;
		LOOK_X = 960;
		LOOK_Y = 900;
		
		FORWARD = false;
		BACKWARD = false;
		LEFT = false;
		RIGHT = false;
		
		IS_FIRING = false;
		IS_RELOADING = false;
		CROSSHAIR = false;
		
		CURRENT_WEAPON = WeaponData.getWeapon(0);
		
		MAX_HEALTH = 100;
		HEALTH = 100;
		
		CURRENT_AMMO = 0;
	}
	
	public void onActivate() 
	{
		
	}

	public void onDeactivate()
	{
		
	}
	
	public void onFire()
	{
		if(CURRENT_AMMO == 0 && TOTAL_AMMO[CURRENT_WEAPON.ID] == 0)
		{
			return;
		}
		
		AGE_AT_FIRE = AGE;
		fireProjectile();
		CURRENT_AMMO--;
		
		if(CURRENT_AMMO == 0)
		{
			reload();
			return;
		}
	}

	public void reload()
	{
		if(IS_RELOADING || CURRENT_AMMO >= CURRENT_WEAPON.CLIP_SIZE || (TOTAL_AMMO[CURRENT_WEAPON.ID] == 0 && LEVEL.STATE.FRAME.ENGINE.SETTINGS.DIFFICULTY >= 0))
		{
			return;
		}
		
		IS_RELOADING = true;
		AGE_AT_RELOAD = AGE;
	}
	
	public void onRender(Visuals v)
	{
		if((FORWARD && !COL_DOWN) || (BACKWARD && !COL_UP) || (LEFT && !COL_RIGHT) || (RIGHT && ! COL_LEFT))
		{
			int walkCycle = WALK_CYCLE;
			
			if(DIRECTION == 0 && LEFT)
			{
				walkCycle = (5 - WALK_CYCLE);
			}
			
			if(DIRECTION == 1 && RIGHT)
			{
				walkCycle = (5 - WALK_CYCLE);
			}
			
			v.drawImage(torso[2 + walkCycle][DIRECTION], XPOS + LEVEL.XLOC - (DRAWX / 2), YPOS + LEVEL.YLOC - (DRAWY / 2), DRAWX, DRAWY);
		}
		else
		{
			v.drawImage(torso[MAIN_CYCLE][DIRECTION], XPOS + LEVEL.XLOC - (DRAWX / 2), YPOS + LEVEL.YLOC - (DRAWY / 2), DRAWX, DRAWY);
		}
		
		v.drawImage(head[(DIRECTION * 2) + MAIN_CYCLE][EXPRESSION], XPOS + LEVEL.XLOC - (DRAWX / 2), YPOS + LEVEL.YLOC - (DRAWY / 2), DRAWX, DRAWY);
		
		BufferedImage rotated = new BufferedImage(64, 64, 2);
		int shift = !(AGE >= AGE_AT_FIRE + CURRENT_WEAPON.FIRE_DELAY - 2) ? 2 : 0;
		
		switch(DIRECTION)
		{
		case 0:
			if(CURRENT_WEAPON.ID == 0)
			{
				v.drawImage(arms[ARMS_CYCLE][CURRENT_WEAPON.ID], XPOS + LEVEL.XLOC - (DRAWX / 2), YPOS + LEVEL.YLOC - (DRAWY / 2), DRAWX, DRAWY);
				break;
			}
			else if(CURRENT_WEAPON.ID == 1)
			{
				v.drawImage(arms[2 + ARMS_CYCLE][CURRENT_WEAPON.ID], XPOS + LEVEL.XLOC - (DRAWX / 2), YPOS + LEVEL.YLOC - (DRAWY / 2), DRAWX, DRAWY);
				new AffineTransformOp(AffineTransform.getRotateInstance((Math.PI * 0.5) - THETA, 20, 25), AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(arms[ARMS_CYCLE][CURRENT_WEAPON.ID], rotated);
				v.drawImage(rotated, XPOS + LEVEL.XLOC - (DRAWX / 2), YPOS + LEVEL.YLOC - (DRAWY / 2), DRAWX, DRAWY);
				break;
			}
			
			new AffineTransformOp(AffineTransform.getRotateInstance((Math.PI * 0.5) - THETA, 20, 25), AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(arms[shift + ARMS_CYCLE][CURRENT_WEAPON.ID], rotated);
			v.drawImage(rotated, XPOS + LEVEL.XLOC - (DRAWX / 2), YPOS + LEVEL.YLOC - (DRAWY / 2), DRAWX, DRAWY);
			break;
		case 1: 
			if(CURRENT_WEAPON.ID == 0)
			{
				v.drawImage(arms[4 + ARMS_CYCLE][CURRENT_WEAPON.ID], XPOS + LEVEL.XLOC - (DRAWX / 2), YPOS + LEVEL.YLOC - (DRAWY / 2), DRAWX, DRAWY);
				break;
			}
			else if(CURRENT_WEAPON.ID == 1)
			{
				v.drawImage(arms[6 + ARMS_CYCLE][CURRENT_WEAPON.ID], XPOS + LEVEL.XLOC - (DRAWX / 2), YPOS + LEVEL.YLOC - (DRAWY / 2), DRAWX, DRAWY);
				new AffineTransformOp(AffineTransform.getRotateInstance((Math.PI * 1.5) - THETA, 43, 25), AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(arms[4 + ARMS_CYCLE][CURRENT_WEAPON.ID], rotated);
				v.drawImage(rotated, XPOS + LEVEL.XLOC - (DRAWX / 2), YPOS + LEVEL.YLOC - (DRAWY / 2), DRAWX, DRAWY);
				break;
			}
			
			new AffineTransformOp(AffineTransform.getRotateInstance((Math.PI * 1.5) - THETA, 43, 25), AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(arms[4 + shift + ARMS_CYCLE][CURRENT_WEAPON.ID], rotated);
			v.drawImage(rotated, XPOS + LEVEL.XLOC - (DRAWX / 2), YPOS + LEVEL.YLOC - (DRAWY / 2), DRAWX, DRAWY);
			break;
		case 2:
			v.drawImage(arms[8 + shift + ARMS_CYCLE][CURRENT_WEAPON.ID], XPOS + LEVEL.XLOC - (DRAWX / 2), YPOS + LEVEL.YLOC - (DRAWY / 2), DRAWX, DRAWY);
			break;
		case 3:
			v.drawImage(arms[12 + shift + ARMS_CYCLE][CURRENT_WEAPON.ID], XPOS + LEVEL.XLOC - (DRAWX / 2), YPOS + LEVEL.YLOC - (DRAWY / 2), DRAWX, DRAWY);
			break;
		}
	}
	
	public void onTick()
	{
		facePoint(LOOK_X, LOOK_Y);
		
		move();
		
		MAIN_CYCLE = (AGE / 30) % 2;
		ARMS_CYCLE = ((AGE + 10) / 30) % 2;
		WALK_CYCLE = (AGE / (40 / MOVE_SPEED)) % 6;
		
		CURRENT_AMMO = Utilities.clamp(CURRENT_AMMO, 0, CURRENT_WEAPON.CLIP_SIZE);
		TOTAL_AMMO[CURRENT_WEAPON.ID] = Utilities.clamp(TOTAL_AMMO[CURRENT_WEAPON.ID], 0, CURRENT_WEAPON.CLIP_SIZE * 2 * (6 - LEVEL.STATE.FRAME.ENGINE.SETTINGS.DIFFICULTY));
		
		if(IS_FIRING && AGE > AGE_AT_FIRE + CURRENT_WEAPON.FIRE_DELAY && CURRENT_WEAPON.ID != 0 && !IS_RELOADING)
		{
			onFire();
			
			if(CURRENT_WEAPON.SEMI)
			{
				IS_FIRING = false;
			}
		}
		
		if(IS_RELOADING && AGE > AGE_AT_RELOAD + CURRENT_WEAPON.RELOAD_TIME)
		{
			IS_RELOADING = false;
			
			if(LEVEL.STATE.FRAME.ENGINE.SETTINGS.DIFFICULTY >= 0 && CURRENT_WEAPON.ID > 0)
			{
				int ammoUsed = CURRENT_WEAPON.CLIP_SIZE - CURRENT_AMMO;
				
				if(TOTAL_AMMO[CURRENT_WEAPON.ID] < ammoUsed)
				{
					CURRENT_AMMO += TOTAL_AMMO[CURRENT_WEAPON.ID];
					TOTAL_AMMO[CURRENT_WEAPON.ID] = 0;
					return;
				}
				
				CURRENT_AMMO = CURRENT_WEAPON.CLIP_SIZE;
				TOTAL_AMMO[CURRENT_WEAPON.ID] -= ammoUsed;
				return;
			}
			
			CURRENT_AMMO = CURRENT_WEAPON.CLIP_SIZE;
		}
	}
	
	public void onCollide(GameEntity hit)
	{
		
	}
	
	public void onCollectWeapon(int id)
	{
		if(weaponObtained[id])
		{
			return;
		}
		
		id = Utilities.clamp(id, 0, WeaponData.NUM_OF_WEAPONS);
		CURRENT_WEAPON = WeaponData.getWeapon(id);
		weaponObtained[id] = true;
		
		CURRENT_AMMO = CURRENT_WEAPON.CLIP_SIZE;
		ammoInClip[CURRENT_WEAPON.ID] = CURRENT_WEAPON.CLIP_SIZE;
		TOTAL_AMMO[CURRENT_WEAPON.ID] += CURRENT_WEAPON.CLIP_SIZE * (6 - LEVEL.STATE.FRAME.ENGINE.SETTINGS.DIFFICULTY);
	}
	
	public void onSwitchWeapon()
	{
		if(IS_RELOADING || CURRENT_WEAPON.ID == 0)
		{
			return;
		}
		
		AGE_AT_FIRE = 0;
		
		int newId = Utilities.wrap(CURRENT_WEAPON.ID + 1, 1, WeaponData.NUM_OF_WEAPONS - 1);
		
		while(!weaponObtained[newId])
		{
			newId = Utilities.wrap(newId + 1, 1, WeaponData.NUM_OF_WEAPONS - 1);
		}
		
		ammoInClip[CURRENT_WEAPON.ID] = CURRENT_AMMO;
		CURRENT_WEAPON = WeaponData.getWeapon(newId);
		CURRENT_AMMO = ammoInClip[newId];
	}
	
	protected void move()
	{
		if(FORWARD && !COL_DOWN) 
		{
			LEVEL.YLOC += MOVE_SPEED;
			YPOS -= MOVE_SPEED;
		}
		if(BACKWARD && !COL_UP)
		{
			LEVEL.YLOC -= MOVE_SPEED;
			YPOS += MOVE_SPEED;
		}
		if(LEFT && !COL_RIGHT)
		{
			LEVEL.XLOC += MOVE_SPEED;
			XPOS -= MOVE_SPEED;
		}
		if(RIGHT && !COL_LEFT) 
		{
			LEVEL.XLOC -= MOVE_SPEED;
			XPOS += MOVE_SPEED;
		}
	}
	
	private void fireProjectile() 
	{
		int launchX = 0;
		int launchY = -48;
		
		if(DIRECTION < 2)
		{
			launchX = (int) (20 * Math.sin(THETA));
			launchY = (int) (48 * Math.cos(THETA));
		}
		else if (DIRECTION == 2)
		{
			if(CURRENT_WEAPON.ID == 1)
			{
				launchY = 20;
				launchX = -26;
			}
			else
			{
				launchY = 24;
				launchX = -18;
			}
		}
		
		switch(CURRENT_WEAPON.ID)
		{
		case 1:
			LEVEL.addActor(new EntityPlayerProjectile(LEVEL, this, XPOS + launchX, YPOS + launchY, 64, 64, THETA, 20, 0));
			return;
		case 2:
			LEVEL.addActor(new EntityPlayerProjectile(LEVEL, this, XPOS + launchX, YPOS + launchY, 64, 64, THETA, 30, 1));
			return;
		case 3:
			for(int i = 0; i < 5; i++)
			{
				LEVEL.addActor(new EntityPlayerProjectile(LEVEL, this, XPOS + launchX, YPOS + launchY, 64, 64, THETA + ((i - 2) * 0.1f), 30, 1));
			}
			return;
		case 4:
			LEVEL.addActor(new EntityPlayerProjectile(LEVEL, this, XPOS + launchX, YPOS + launchY, 64, 64, THETA, 24, 2));
			return;
		case 5:
			LEVEL.addActor(new EntityPlayerProjectile(LEVEL, this, XPOS + launchX, YPOS + launchY, 128, 128, THETA, 20, 3));
			return;
		case 6:
			LEVEL.addActor(new EntityPlayerProjectile(LEVEL, this, XPOS + launchX, YPOS + launchY, 128, 128, THETA, 10, 4));
			return;
		case 7:
			LEVEL.addActor(new EntityPlayerProjectile(LEVEL, this, XPOS + launchX, YPOS + launchY, 128, 128, THETA, 45, 5));
			return;
		default: return;
		}
	}
}
