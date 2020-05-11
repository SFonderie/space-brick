package com.aviangames.entity;

import java.awt.image.BufferedImage;

import com.aviangames.elements.EngineScene;
import com.aviangames.enginejava.EngineCore;
import com.aviangames.enginejava.EngineVisuals;
import com.aviangames.utilities.Weapon;

public class EntityBoss extends EngineEntity
{
	private BufferedImage[][] t_boss;
	
	private int bobCycle = 0;
	
	private int nextShotTime = 0;
	
	public EntityBoss(EngineScene parent, Hitbox[] hitboxes, int x, int y, int w, int h, double t)
	{
		super(parent, hitboxes, x, y, w, h, t);
		
		this.t_boss = this.level.getState().getEngine().getAssetLibrary().getImageDoubleArray("BOSS");
		
		this.maxHealth = 400;
		this.health = this.maxHealth;
	}

	@Override
	public void onTick() 
	{
		this.bobCycle = (int) ((this.level.getElapsedTime() + 0.5) * 2 % 2);
		this.faceEntity(this.level.player);
		
		if(this.level.getElapsedTime() > this.nextShotTime)
		{
			this.level.attemptAddition(new EntityProjectile(this.level, this, 6, this.XPOS, this.YPOS, this.directionAngle, Weapon.WEAPONS[6].DAMAGE));
			this.nextShotTime += 1;
		}
	}

	@Override
	public void onRender(EngineVisuals v) 
	{
		v.drawImage(this.t_boss[this.bobCycle][this.direction], this.level.XLOC + this.XPOS - (this.dWidth / 2), this.level.YLOC + this.YPOS - (this.dHeight / 2), this.dWidth, this.dHeight);
		
		v.fillRect(this.level.XLOC + this.XPOS - (this.dWidth / 2), this.level.YLOC + this.YPOS - (int) (this.dHeight * (5.0 / 8.0)), this.dWidth, this.dHeight / 8, 0.25, 0.0, 0.0, 1.0);
		v.fillRect(this.level.XLOC + this.XPOS - (this.dWidth / 2), this.level.YLOC + this.YPOS - (int) (this.dHeight * (5.0 / 8.0)), (int) (this.dWidth * ((double) this.health / this.maxHealth)), this.dHeight / 8, 0.6, 0.0, 0.0, 1.0);
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
}
