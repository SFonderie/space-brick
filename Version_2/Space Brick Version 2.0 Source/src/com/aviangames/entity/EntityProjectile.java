package com.aviangames.entity;

import java.awt.image.BufferedImage;

import com.aviangames.elements.EngineScene;
import com.aviangames.enginejava.EngineVisuals;

public class EntityProjectile extends EngineEntity
{
	private EngineEntity owner;
	
	private BufferedImage[] t_shot;
	
	private double ageAtSpawn;
	
	private int id;
	
	public int damage;
	
	public EntityProjectile(EngineScene parent, EngineEntity owner, int id, int x, int y, double t, int damage) 
	{
		super(parent, new Hitbox[] { new Hitbox(0, 0, getSize(id) / 4, getSize(id) / 4, false) }, x, y, getSize(id), getSize(id), t);
		
		this.owner = owner;
		this.t_shot = this.level.getState().getEngine().getAssetLibrary().getImageArray("SHOT");
		this.ageAtSpawn = this.level.getElapsedTime();
		this.movementSpeed = getSpeed(id);
		this.id = id;
		
		this.damage = damage;
		
		this.maxHealth = 100;
		this.health = this.maxHealth;
	}

	@Override
	public void onTick()
	{
		if(this.level.getElapsedTime() > this.ageAtSpawn + getMaxAge(this.id))
		{
			this.kill();
		}
		
		if(this.id == 7)
		{
			this.hitboxes[0].width += 12;
			this.hitboxes[0].height += 12;
			this.dWidth += 12;
			this.dHeight += 12;
		}
		
		this.checkForCollisions();
		this.XPOS += this.movementSpeed * Math.sin(this.directionAngle);
		this.YPOS += this.movementSpeed * Math.cos(this.directionAngle);
	}

	@Override
	public void onRender(EngineVisuals v)
	{
		v.drawImageAngled(this.t_shot[this.id], this.level.XLOC + this.XPOS - (this.dWidth / 2), this.level.YLOC + this.YPOS - (this.dHeight / 2), this.dWidth, this.dHeight, (Math.PI * 0.5) - this.directionAngle, 32, 32, 256, 256);
	}

	@Override
	public void onCollide(EngineEntity entity)
	{
		if(this.owner == entity)
		{
			return;
		}
		
		if(!(entity instanceof EntityProjectile))
		{
			this.kill();
		}
	}
	
	private static int getSize(int id)
	{
		if(id == 0)
		{
			return 0;
		}
		
		if(id == 4 || id == 5 || id == 6 || id == 9 || id == 11)
		{
			return TileEntity.SCALE / 2;
		}
		
		if(id == 1 || id == 2 || id == 3 || id == 7)
		{
			return TileEntity.SCALE / 4;
		}
		
		return TileEntity.SCALE;
	}
	
	private static double getMaxAge(int id)
	{
		if(id == 0)
		{
			return 0.0;
		}
		
		if(id == 7)
		{
			return 1.0;
		}
		
		return 5.0;
	}
	
	public static int getSpeed(int id)
	{
		double scaler = TileEntity.SCALE / 128;
		
		if(id == 1 || id == 5 || id == 8 || id == 10)
		{
			return (int) (30 * scaler);
		}
		
		if(id == 2 || id == 3)
		{
			return (int) (40 * scaler);
		}
		
		if(id == 4 || id == 11)
		{
			return (int) (50 * scaler);
		}
		
		if(id == 6)
		{
			return (int) (20 * scaler);
		}
		
		if(id == 7 || id == 9)
		{
			return (int) (15 * scaler);
		}
		
		return 0;
	}
	
	public EngineEntity getOwner()
	{
		return this.owner;
	}
}
