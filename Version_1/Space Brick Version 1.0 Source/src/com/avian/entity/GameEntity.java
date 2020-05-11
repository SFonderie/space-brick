package com.avian.entity;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;

import com.avian.engine.GameCore;
import com.avian.engine.Visuals;
import com.avian.level.GameLevel;

public abstract class GameEntity implements GameCore
{
	public GameLevel LEVEL;
	public int XPOS, YPOS;
	public int DRAWX, DRAWY;
	public int DIRECTION;
	public int MOVE_SPEED;
	public double THETA;
	public int AGE;
	
	public Hitbox[] HITBOXES;
	
	public boolean CAN_COLLIDE, COLLIDING, COL_UP, COL_DOWN, COL_LEFT, COL_RIGHT;
	public boolean SHOW_HITBOXES, KILL_ON_NEXT;
	
	public final long SPAWN_ID;
	
	public GameEntity(GameLevel level, int xPos, int yPos, int drawX, int drawY, double theta)
	{
		LEVEL = level;
		XPOS = xPos;
		YPOS = yPos;
		DRAWX = drawX;
		DRAWY = drawY;
		THETA = theta;
		AGE = 0;
		
		CAN_COLLIDE = true;
		COLLIDING = false;
		COL_UP = false;
		COL_DOWN = false;
		COL_LEFT = false;
		COL_RIGHT = false;
		SHOW_HITBOXES = LEVEL.STATE.FRAME.ENGINE.SETTINGS.BOOL_OPTIONS[3];
		KILL_ON_NEXT = false;
		
		HITBOXES = new Hitbox[0];
		Random rand = new Random();
		SPAWN_ID = rand.nextLong();
	}
	
	public GameEntity(GameEntity clone)
	{
		LEVEL = clone.LEVEL;
		XPOS = clone.XPOS;
		YPOS = clone.YPOS;
		DRAWX = clone.DRAWX;
		DRAWY = clone.DRAWY;
		THETA = clone.THETA;
		AGE = 0;
		
		COL_UP = false;
		COL_DOWN = false;
		COL_LEFT = false;
		COL_RIGHT = false;
		KILL_ON_NEXT = false;
		
		Random rand = new Random();
		SPAWN_ID = rand.nextLong();
	}
	
	public abstract void onCollide(GameEntity hit);
	
	protected final void addHitboxes(Hitbox[] hitboxes)
	{
		HITBOXES = hitboxes;
	}
	
	public final void checkForCollisionWith(GameEntity entity)
	{	
		if(this == entity || this.HITBOXES.length == 0 || entity.HITBOXES.length == 0 || this.HITBOXES == null || entity.HITBOXES == null)
		{
			return;
		}
		
		boolean didUp = false;
		boolean didDn = false;
		boolean didLf = false;
		boolean didRt = false;
		boolean didAn = false;
		boolean event = false;
		
		for(int i = 0; i < this.HITBOXES.length; i++)
		{
			for(int j = 0; j < entity.HITBOXES.length; j++)
			{
				Rectangle check = new Rectangle(this.HITBOXES[i].X_DISP + this.XPOS - (this.HITBOXES[i].WIDTH / 2), this.HITBOXES[i].Y_DISP + this.YPOS - (this.HITBOXES[i].HEIGHT / 2), this.HITBOXES[i].WIDTH, this.HITBOXES[i].HEIGHT);
				Rectangle upper = new Rectangle(entity.HITBOXES[j].X_DISP + entity.XPOS - (entity.HITBOXES[j].WIDTH / 2), entity.HITBOXES[j].Y_DISP + entity.YPOS - (entity.HITBOXES[j].HEIGHT / 2) - 1, entity.HITBOXES[j].WIDTH, 1);
				Rectangle lower = new Rectangle(entity.HITBOXES[j].X_DISP + entity.XPOS - (entity.HITBOXES[j].WIDTH / 2), entity.HITBOXES[j].Y_DISP + entity.YPOS + (entity.HITBOXES[j].HEIGHT / 2), entity.HITBOXES[j].WIDTH, 1);
				Rectangle lefty = new Rectangle(entity.HITBOXES[j].X_DISP + entity.XPOS - (entity.HITBOXES[j].WIDTH / 2) - 1, entity.HITBOXES[j].Y_DISP + entity.YPOS - (entity.HITBOXES[j].HEIGHT / 2), 1, entity.HITBOXES[j].HEIGHT);
				Rectangle right = new Rectangle(entity.HITBOXES[j].X_DISP + entity.XPOS + (entity.HITBOXES[j].WIDTH / 2), entity.HITBOXES[j].Y_DISP + entity.YPOS - (entity.HITBOXES[j].HEIGHT / 2), 1, entity.HITBOXES[j].HEIGHT);
				
				boolean up = check.getBounds2D().intersects(upper);
				boolean dn = check.getBounds2D().intersects(lower);
				boolean lf = check.getBounds2D().intersects(lefty);
				boolean rt = check.getBounds2D().intersects(right);
				boolean any = false;
				
				any = up || dn || lf || rt;
				event = event || any;
				
				if(this.HITBOXES[i].IS_SOLID && entity.HITBOXES[j].IS_SOLID)
				{
					didUp = didUp || up;
					didDn = didDn || dn;
					didLf = didLf || lf;
					didRt = didRt || rt;
					didAn = didAn || any;
				}
			}
		}
		
		COL_UP = COL_UP || didUp;
		COL_DOWN = COL_DOWN || didDn;
		COL_LEFT = COL_LEFT || didLf;
		COL_RIGHT = COL_RIGHT || didRt;
		COLLIDING = COLLIDING || didAn;
		
		if(event)
		{
			this.onCollide(entity);
		}
	}
	
	public final void toggleHitboxRendering()
	{
		SHOW_HITBOXES = !SHOW_HITBOXES;
	}
	
	public final void facePoint(int x, int y)
	{
		int deltaX = x - (XPOS + LEVEL.XLOC);
		int deltaY = y - (YPOS + LEVEL.YLOC);
		double dir = Math.asin(deltaX / Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
		
		if(deltaX >= 0)
		{
			if(deltaY >= 0)
			{
				THETA = dir;
			}
			else
			{
				THETA = Math.PI - dir;
			}
		}
		else
		{
			if(deltaY >= 0)
			{
				THETA = (Math.PI * 2) + dir;
			}
			else
			{
				THETA = Math.PI - dir;
			}
		}
	}
	
	private final void kill()
	{
		onDeactivate();
		LEVEL.removeActor(SPAWN_ID);
	}
	
	public final void tick()
	{
		if(KILL_ON_NEXT)
		{
			kill();
			return;
		}
		
		if(THETA >= (Math.PI * 0.3) && THETA <= (Math.PI * 0.75))
		{
			DIRECTION = 0;
		}
		else if(THETA >= (Math.PI * 1.25) && THETA <= (Math.PI * 1.7))
		{
			DIRECTION = 1;
		}
		else if(THETA > (Math.PI * 1.7) || THETA < (Math.PI * 0.3))
		{
			DIRECTION = 2;
		}
		else if(THETA > (Math.PI * 0.75) && THETA < (Math.PI * 1.25))
		{
			DIRECTION = 3;
		}
		
		AGE++;
		
		onTick();
		
		SHOW_HITBOXES = LEVEL.STATE.FRAME.ENGINE.SETTINGS.BOOL_OPTIONS[3];
	}
	
	public final void render(Visuals v)
	{
		onRender(v);
		
		if(SHOW_HITBOXES)
		{
			v.GRAPHICS.setColor(Color.RED);
			v.GRAPHICS.drawRect(
					(int) ((LEVEL.XLOC + XPOS - 1) * (1f / LEVEL.STATE.FRAME.NATIVE_WIDTH) * LEVEL.STATE.FRAME.WIDTH) + LEVEL.STATE.FRAME.BORDER_WIDTH,
					(int) ((LEVEL.YLOC + YPOS - 1) * (1f / LEVEL.STATE.FRAME.NATIVE_HEIGHT) * LEVEL.STATE.FRAME.HEIGHT) + LEVEL.STATE.FRAME.BORDER_HEIGHT,
					(int) (2 * (1f / LEVEL.STATE.FRAME.NATIVE_WIDTH) * LEVEL.STATE.FRAME.WIDTH), 
					(int) (2 * (1f / LEVEL.STATE.FRAME.NATIVE_WIDTH) * LEVEL.STATE.FRAME.WIDTH));
			
			for(int i = 0; i < HITBOXES.length; i++)
			{
				if(HITBOXES[i].IS_SOLID)
				{
					v.GRAPHICS.setColor(Color.RED);
				}
				else
				{
					v.GRAPHICS.setColor(Color.MAGENTA);
				}
				
				v.GRAPHICS.drawRect(
						(int) ((LEVEL.XLOC + XPOS + HITBOXES[i].X_DISP - (HITBOXES[i].WIDTH / 2)) * (1f / LEVEL.STATE.FRAME.NATIVE_WIDTH) * LEVEL.STATE.FRAME.WIDTH) + LEVEL.STATE.FRAME.BORDER_WIDTH,
						(int) ((LEVEL.YLOC + YPOS + HITBOXES[i].Y_DISP - (HITBOXES[i].HEIGHT/ 2)) * (1f / LEVEL.STATE.FRAME.NATIVE_HEIGHT) * LEVEL.STATE.FRAME.HEIGHT) + LEVEL.STATE.FRAME.BORDER_HEIGHT,
						(int) (HITBOXES[i].WIDTH * (1f / LEVEL.STATE.FRAME.NATIVE_WIDTH) * LEVEL.STATE.FRAME.WIDTH), 
						(int) (HITBOXES[i].HEIGHT * (1f / LEVEL.STATE.FRAME.NATIVE_WIDTH) * LEVEL.STATE.FRAME.WIDTH));
			}
			
			v.GRAPHICS.setColor(Color.BLACK);
		}
	}
	
	protected void move()
	{
		if(!COL_DOWN) 
		{
			YPOS -= MOVE_SPEED;
		}
		if(!COL_UP)
		{
			YPOS += MOVE_SPEED;
		}
		if(!COL_RIGHT)
		{
			XPOS -= MOVE_SPEED;
		}
		if(!COL_LEFT) 
		{
			XPOS += MOVE_SPEED;
		}
	}
}
