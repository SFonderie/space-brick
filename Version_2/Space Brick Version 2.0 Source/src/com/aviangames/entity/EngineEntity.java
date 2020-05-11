package com.aviangames.entity;

import java.awt.Rectangle;

import com.aviangames.elements.EngineScene;
import com.aviangames.enginejava.EngineCore;
import com.aviangames.enginejava.EngineVisuals;

public abstract class EngineEntity
{
	public boolean CAN_COLLIDE, COLLIDING, COL_UP, COL_DOWN, COL_LEFT, COL_RIGHT;
	
	public boolean SHOW_HITBOXES;
	
	public int XPOS;
	
	public int YPOS;
	
	protected int dWidth;
	
	protected int dHeight;
	
	protected int direction;
	
	protected int movementSpeed;
	
	protected double directionAngle;
	
	protected Hitbox[] hitboxes;
	
	protected EngineScene level;
	
	protected int health, maxHealth = 100;
	
	public EngineEntity(EngineScene parent, Hitbox[] hitboxes, int x, int y, int w, int h, double t)
	{
		this.level = parent;
		this.hitboxes = hitboxes;
		this.XPOS = x;
		this.YPOS = y;
		this.dWidth = w;
		this.dHeight = h;
		this.directionAngle = t;
		
		this.CAN_COLLIDE = true;
		this.COLLIDING = false;
		this.COL_UP = false;
		this.COL_DOWN = false;
		this.COL_LEFT = false;
		this.COL_RIGHT = false;
		this.SHOW_HITBOXES = false;
	}
	
	public abstract void onTick();
	
	public abstract void onRender(EngineVisuals v);
	
	public abstract void onCollide(EngineEntity entity);
	
	public final void checkForCollisions()
	{
		if(!this.CAN_COLLIDE)
		{
			return;
		}
		
		this.COL_UP = false;
		this.COL_DOWN = false;
		this.COL_LEFT = false;
		this.COL_RIGHT = false;
		this.COLLIDING = false;
		
		for(TileEntity entity : this.level.ATILES)
		{
			this.checkForCollisionWith(entity);
		}
		
		for(EngineEntity entity : this.level.ACTORS)
		{
			this.checkForCollisionWith(entity);
		}
	}
	
	public final void checkForCollisionWith(EngineEntity entity)
	{	
		if(this == entity || this.hitboxes.length == 0 || entity.hitboxes.length == 0 || this.hitboxes == null || entity.hitboxes == null || !entity.CAN_COLLIDE)
		{
			return;
		}
		
		boolean didUp = false;
		boolean didDn = false;
		boolean didLf = false;
		boolean didRt = false;
		boolean didAn = false;
		boolean event = false;
		
		for(int i = 0; i < this.hitboxes.length; i++)
		{
			for(int j = 0; j < entity.hitboxes.length; j++)
			{
				Rectangle check = new Rectangle(this.hitboxes[i].xDisp + this.XPOS - (this.hitboxes[i].width / 2), this.hitboxes[i].yDisp + this.YPOS - (this.hitboxes[i].height / 2), this.hitboxes[i].width, this.hitboxes[i].height);
				Rectangle upper = new Rectangle(entity.hitboxes[j].xDisp + entity.XPOS - (entity.hitboxes[j].width / 2), entity.hitboxes[j].yDisp + entity.YPOS - (entity.hitboxes[j].height / 2) - 1, entity.hitboxes[j].width, 1);
				Rectangle lower = new Rectangle(entity.hitboxes[j].xDisp + entity.XPOS - (entity.hitboxes[j].width / 2), entity.hitboxes[j].yDisp + entity.YPOS + (entity.hitboxes[j].height / 2), entity.hitboxes[j].width, 1);
				Rectangle lefty = new Rectangle(entity.hitboxes[j].xDisp + entity.XPOS - (entity.hitboxes[j].width / 2) - 1, entity.hitboxes[j].yDisp + entity.YPOS - (entity.hitboxes[j].height / 2), 1, entity.hitboxes[j].height);
				Rectangle right = new Rectangle(entity.hitboxes[j].xDisp + entity.XPOS + (entity.hitboxes[j].width / 2), entity.hitboxes[j].yDisp + entity.YPOS - (entity.hitboxes[j].height / 2), 1, entity.hitboxes[j].height);
				Rectangle whole = new Rectangle(entity.hitboxes[j].xDisp + entity.XPOS - (entity.hitboxes[j].width / 2), entity.hitboxes[j].yDisp + entity.YPOS - (entity.hitboxes[j].height / 2), entity.hitboxes[j].width, entity.hitboxes[j].height);
				
				boolean up = check.getBounds2D().intersects(upper);
				boolean dn = check.getBounds2D().intersects(lower);
				boolean lf = check.getBounds2D().intersects(lefty);
				boolean rt = check.getBounds2D().intersects(right);
				boolean any = check.getBounds2D().intersects(whole);
				
				any = up || dn || lf || rt;
				event = event || any;
				
				if(this.hitboxes[i].solid && entity.hitboxes[j].solid)
				{
					didUp = didUp || up;
					didDn = didDn || dn;
					didLf = didLf || lf;
					didRt = didRt || rt;
					didAn = didAn || any;
				}
			}
		}
		
		this.COL_UP = this.COL_UP || didUp;
		this.COL_DOWN = this.COL_DOWN || didDn;
		this.COL_LEFT = this.COL_LEFT || didLf;
		this.COL_RIGHT = this.COL_RIGHT || didRt;
		this.COLLIDING = this.COLLIDING || didAn;
		
		if(event)
		{
			this.onCollide(entity);
			entity.onCollide(this);
		}
	}
	
	public void facePoint(int x, int y)
	{
		this.directionAngle = EngineCore.getAngle(this.XPOS + this.level.XLOC, this.YPOS + this.level.YLOC, x, y);
	}
	
	public void faceEntity(EngineEntity entity)
	{
		this.directionAngle = EngineCore.getAngle(this.XPOS, this.YPOS, entity.XPOS, entity.YPOS);
	}
	
	public final void toggleHitboxRendering()
	{
		SHOW_HITBOXES = !SHOW_HITBOXES;
	}
	
	public final void kill()
	{
		this.level.attemptRemoval(this);
	}
	
	public final void tick()
	{
		if(this.health <= 0)
		{
			this.kill();
		}
		
		if(this.directionAngle >= (Math.PI * 0.3) && this.directionAngle <= (Math.PI * 0.75))
		{
			this.direction = 0;
		}
		else if(this.directionAngle >= (Math.PI * 1.25) && this.directionAngle <= (Math.PI * 1.7))
		{
			this.direction = 1;
		}
		else if(this.directionAngle > (Math.PI * 1.7) || this.directionAngle < (Math.PI * 0.3))
		{
			this.direction = 2;
		}
		else if(this.directionAngle > (Math.PI * 0.75) && this.directionAngle < (Math.PI * 1.25))
		{
			this.direction = 3;
		}
		
		this.onTick();
	}
	
	public final void renderHitboxes(EngineVisuals v)
	{
		for(Hitbox box : this.hitboxes)
		{
			v.setColor(1.0, 0.0, box.solid ? 0.0 : 1.0, 1.0);
			v.getGFX().drawRect(this.level.XLOC + this.XPOS + box.xDisp - (box.width / 2), this.level.YLOC + this.YPOS + box.yDisp - (box.height / 2), box.width, box.height);
		}
	}
}
