package com.avian.entity;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.avian.engine.Visuals;
import com.avian.level.GameLevel;

public class TileEntity extends GameEntity 
{
	private BufferedImage texture, overTexture;
	
	public TileEntity(int id, GameLevel level, int xPos, int yPos, int drawX, int drawY, double theta, BufferedImage type)
	{
		super(level, xPos, yPos, drawX, drawY, theta);
		
		texture = type;
		
		createHitboxSystem(id);
	}
	
	public TileEntity(int id, GameLevel level, int xPos, int yPos, int drawX, int drawY, double theta, BufferedImage type, BufferedImage over)
	{
		super(level, xPos, yPos, drawX, drawY, theta);
		
		texture = type;
		overTexture = over;
		
		createHitboxSystem(id);
	}
	
	public void createHitboxSystem(int id)
	{
		if(id < 2)
		{
			return;
		}
		
		if(id == 2 || id == 12 || id == 13)
		{
			addHitboxes(new Hitbox[]{
					new Hitbox(0, -DRAWY / 4, DRAWX, DRAWY * 7 / 8, true)
			});
		}
		
		if(id == 3 || id == 4 || id == 6 || id == 7 || id == 8 || id == 9 || id == 14 || id == 15 || id > 16)
		{
			addHitboxes(new Hitbox[]{
					new Hitbox(0, 0, DRAWX, DRAWY, true)
			});
		}
		
		if(id == 5 || id == 10 || id == 11 || id == 16)
		{
			addHitboxes(new Hitbox[]{
					new Hitbox(0, DRAWY * 3 / 8, DRAWX, DRAWY / 16, true)
			});
		}
	}
	
	public void onActivate() 
	{
		
	}
	
	public void onDeactivate() 
	{
		
	}
	
	public void onRender(Visuals v) 
	{
		LEVEL.renderOnAtPoint(v, texture, XPOS, YPOS, DRAWX, DRAWY + 1);
	}
	
	public void lateRender(Visuals v)
	{
		if(overTexture != null)
		{
			LEVEL.renderOnAtPoint(v, overTexture, XPOS, YPOS, DRAWX, DRAWY + 1);
		}
		
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
	
	public void onTick()
	{
		SHOW_HITBOXES = LEVEL.STATE.FRAME.ENGINE.SETTINGS.BOOL_OPTIONS[3];
	}
	
	public void onCollide(GameEntity hit)
	{
		
	}
}
