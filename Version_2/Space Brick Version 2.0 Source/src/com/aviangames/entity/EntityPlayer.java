package com.aviangames.entity;

import java.awt.image.BufferedImage;

import com.aviangames.elements.EngineScene;
import com.aviangames.enginejava.EngineCore;
import com.aviangames.enginejava.EngineVisuals;
import com.aviangames.utilities.Weapon;

public class EntityPlayer extends EngineEntity
{	
	public boolean FORWARD, BACK, LEFT, RIGHT, MOVING = false;
	
	public int[] magazines = new int[Weapon.WEAPONS.length];
	
	public boolean IS_FIRING = false;
	
	public boolean IS_RELOADING = false;
	
	private boolean firing = false;
	
	private BufferedImage[][] t_legs;
	
	private BufferedImage[][] t_arms;
	
	private BufferedImage[][] t_head;
	
	private int bobCycle = 0;
	
	private int walkCycle = 0;
	
	private int armsCycle = 0;
	
	public double ageAtWalkStart = 0;
	
	public int WEAPON = 0;
	
	public double ageAtFireStart = 0;
	
	public double ageAtReloadStart = 0;
	
	public EntityPlayer(EngineScene parent, Hitbox[] hitboxes, int x, int y, int w, int h) 
	{
		super(parent, hitboxes, x, y, w, h, 0.0);
		
		this.t_legs = this.level.getState().getEngine().getAssetLibrary().getImageDoubleArray("LEGS");
		this.t_arms = this.level.getState().getEngine().getAssetLibrary().getImageDoubleArray("ARMS");
		this.t_head = this.level.getState().getEngine().getAssetLibrary().getImageDoubleArray("HEAD");
		
		this.movementSpeed = TileEntity.SCALE / 16;
		
		for(int i = 0; i < this.magazines.length; i++)
		{
			this.magazines[i] = Weapon.WEAPONS[i].CLIPSIZE;
		}
		
		this.maxHealth = 200;
		this.health = this.maxHealth;
	}

	@Override
	public void onTick() 
	{
		this.MOVING = this.FORWARD || this.BACK || this.LEFT || this.RIGHT;
		
		if(this.MOVING)
		{
			if((this.FORWARD && (this.LEFT || this.RIGHT)) || (this.BACK && (this.LEFT || this.RIGHT)))
			{
				int movement = (int) Math.ceil(this.movementSpeed / Math.sqrt(2));
				
				for(int i = 0; i < movement; i++)
				{
					this.checkForCollisions();
					this.attemptMovement();
				}
			}
			else
			{
				for(int i = 0; i < this.movementSpeed; i++)
				{
					this.checkForCollisions();
					this.attemptMovement();
				}
			}
		}
		
		this.bobCycle = (int) (this.level.getElapsedTime() * 2 % 2);
		this.armsCycle = (int) ((this.level.getElapsedTime() - 0.25) * 2 % 2);
		this.walkCycle = (int) ((this.level.getElapsedTime() + this.ageAtWalkStart) * 8 % 6);
		
		if((this.FORWARD && this.direction == 2) || (this.BACK && this.direction == 3) || (this.LEFT && this.direction == 0) || (this.RIGHT && this.direction == 1))
		{
			this.walkCycle = (5 - this.walkCycle);
		}
		
		if(this.IS_FIRING && !this.firing && !this.IS_RELOADING)
		{
			this.onShoot();
			this.firing = true;
			this.ageAtFireStart = this.level.getElapsedTime();
			this.magazines[this.WEAPON]--;
		}
		
		if(this.level.getElapsedTime() >= Weapon.WEAPONS[this.WEAPON].FIRETIME + this.ageAtFireStart)
		{
			this.firing = false;
		}
		
		if(!this.IS_RELOADING && this.magazines[this.WEAPON] == 0 && this.WEAPON != 0)
		{
			this.IS_RELOADING = true;
			this.ageAtReloadStart = this.level.getElapsedTime();
		}
		
		if(this.IS_RELOADING && this.level.getElapsedTime() >= Weapon.WEAPONS[this.WEAPON].RELOAD + this.ageAtReloadStart)
		{
			this.IS_RELOADING = false;
			this.magazines[this.WEAPON] = Weapon.WEAPONS[this.WEAPON].CLIPSIZE;
		}
	}

	@Override
	public void onRender(EngineVisuals v) 
	{
		v.drawImage(this.t_legs[this.MOVING ? 2 + this.walkCycle : this.bobCycle][this.direction], this.level.XLOC + this.XPOS - this.dWidth / 2, this.level.YLOC + this.YPOS - this.dHeight / 2, this.dWidth, this.dHeight);
		v.drawImage(this.t_head[this.MOVING ? 2 + this.walkCycle % 3 : this.bobCycle][this.direction], this.level.XLOC + this.XPOS - this.dWidth / 2, this.level.YLOC + this.YPOS - this.dHeight / 2, this.dWidth, this.dHeight);
		
		int gWidth = (int) (this.dWidth * (17.0 / 20));
		int gHeight = (int) (this.dHeight * (17.0 / 20));
		
		if(this.WEAPON == 0)
		{
			v.drawImage(this.t_arms[this.direction * 4 + this.armsCycle][this.WEAPON], this.level.XLOC + this.XPOS - gWidth / 2, this.level.YLOC + this.YPOS - gHeight / 2, gWidth, gHeight);
		}
		else if(this.WEAPON == 1)
		{
			v.drawImage(this.t_arms[this.direction * 4 + this.armsCycle + 2][this.WEAPON], this.level.XLOC + this.XPOS - gWidth / 2, this.level.YLOC + this.YPOS - gHeight / 2, gWidth, gHeight);
			
			if(this.direction == 0)
			{
				v.drawImageAngled(this.t_arms[this.direction * 4 + this.armsCycle][this.WEAPON], this.level.XLOC + this.XPOS - gWidth / 2, this.level.YLOC + this.YPOS - gHeight / 2, gWidth, gHeight, (Math.PI * 0.5) - this.directionAngle, 21, 27, 256, 256);
			}
			else if(this.direction == 1)
			{
				v.drawImageAngled(this.t_arms[this.direction * 4 + this.armsCycle][this.WEAPON], this.level.XLOC + this.XPOS - gWidth / 2, this.level.YLOC + this.YPOS - gHeight / 2, gWidth, gHeight, (Math.PI * 1.5) - this.directionAngle, 43, 27, 256, 256);
			}
		}
		else
		{	
			if(this.direction == 0)
			{
				v.drawImageAngled(this.t_arms[this.direction * 4 + this.armsCycle + (this.firing ? 2 : 0)][this.WEAPON], this.level.XLOC + this.XPOS - gWidth / 2, this.level.YLOC + this.YPOS - gHeight / 2, gWidth, gHeight, (Math.PI * 0.5) - this.directionAngle, 21, 27, 256, 256);
			}
			else if(this.direction == 1)
			{
				v.drawImageAngled(this.t_arms[this.direction * 4 + this.armsCycle + (this.firing ? 2 : 0)][this.WEAPON], this.level.XLOC + this.XPOS - gWidth / 2, this.level.YLOC + this.YPOS - gHeight / 2, gWidth, gHeight, (Math.PI * 1.5) - this.directionAngle, 43, 27, 256, 256);
			}
			else
			{
				v.drawImage(this.t_arms[this.direction * 4 + this.armsCycle + (this.firing ? 2 : 0)][this.WEAPON], this.level.XLOC + this.XPOS - gWidth / 2, this.level.YLOC + this.YPOS - gHeight / 2, gWidth, gHeight);
			}
		}
		
		v.fillRect(this.level.XLOC + this.XPOS - (this.dWidth / 4), this.level.YLOC + this.YPOS - (int) (this.dHeight * (5.0 / 8.0)), this.dWidth / 2, this.dHeight / 8, 0.25, 0.0, 0.0, 1.0);
		v.fillRect(this.level.XLOC + this.XPOS - (this.dWidth / 4), this.level.YLOC + this.YPOS - (int) (this.dHeight * (5.0 / 8.0)), (int) (this.dWidth / 2 * ((double) this.health / this.maxHealth)), this.dHeight / 8, 0.6, 0.0, 0.0, 1.0);
	}

	@Override
	public void onCollide(EngineEntity entity) 
	{
		if(entity instanceof EntityProjectile)
		{
			EntityProjectile proj = (EntityProjectile) entity;
			
			if(!(proj.getOwner() == this))
			{
				this.health = (int) EngineCore.forceInvariance(this.health - proj.damage, 0, this.maxHealth);
			}
		}
	}
	
	public void onShoot()
	{
		if(this.WEAPON > 0)
		{
			int launchX = (int) (TileEntity.SCALE / 8 * Math.sin(this.directionAngle));
			int launchY = (int) (TileEntity.SCALE / 8 * Math.cos(this.directionAngle));
			this.level.attemptAddition(new EntityProjectile(this.level, this, this.WEAPON, this.XPOS + launchX, this.YPOS + launchY, this.directionAngle, Weapon.WEAPONS[this.WEAPON].DAMAGE));
			
			if(this.WEAPON == 3)
			{
				for(int i = 1; i <= 2; i++)
				{
					this.level.attemptAddition(new EntityProjectile(this.level, this, this.WEAPON, this.XPOS + launchX, this.YPOS + launchY, this.directionAngle + (0.1 * i), Weapon.WEAPONS[this.WEAPON].DAMAGE));
					this.level.attemptAddition(new EntityProjectile(this.level, this, this.WEAPON, this.XPOS + launchX, this.YPOS + launchY, this.directionAngle - (0.1 * i), Weapon.WEAPONS[this.WEAPON].DAMAGE));
				}
			}
		}
	}
	
	public void switchWeapon()
	{	
		if(this.IS_RELOADING)
		{
			return;
		}
		
		if(this.WEAPON == Weapon.WEAPONS.length - 1)
		{
			this.WEAPON = 1;
		}
		else
		{
			this.WEAPON++;
		}
	}
	
	public void attemptMovement()
	{
		if(this.FORWARD && !this.COL_DOWN)
		{
			this.YPOS--;
			this.level.YLOC++;
		}
		else if(this.BACK && !this.COL_UP)
		{
			this.YPOS++;
			this.level.YLOC--;
		}
		
		if(this.LEFT && !this.COL_RIGHT)
		{
			this.XPOS--;
			this.level.XLOC++;
		}
		else if(this.RIGHT && !this.COL_LEFT)
		{
			this.XPOS++;
			this.level.XLOC--;
		}
	}
}
