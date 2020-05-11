package com.avian.entity;

import java.awt.image.BufferedImage;

import com.avian.engine.Utilities;
import com.avian.engine.Visuals;
import com.avian.level.GameLevel;

public class EntityFreezer extends GameEntity 
{
	private BufferedImage[][] texture;
	private int cycle = 0;
	
	public boolean LOWERED;
	
	public EntityFreezer(GameLevel level, int xPos, int yPos, int drawX, int drawY, double theta) 
	{
		super(level, xPos, yPos, drawX, drawY, theta);
		addHitboxes(new Hitbox[]{
				new Hitbox(-DRAWX * 5 / 16, DRAWY / 16, DRAWX / 4, DRAWY / 4, true),	//LEFTMOST COLLIDER HITBOX
				new Hitbox(DRAWX * 5 / 16, DRAWY / 16, DRAWX / 4, DRAWY / 4, true),		//RIGHTMOST COLLIDER HITBOX
				new Hitbox(0, -DRAWY / 16, DRAWX * 7 / 8, DRAWY / 8, true),				//MIDLINE COLLIDER HITBOX
				new Hitbox(0, 0, DRAWX * 7 / 8, DRAWY * 7 / 8, false)					//WRAPPING HITBOX
		});
		
		texture = Visuals.cut(LEVEL.STATE.FRAME.ENGINE.UTIL.E_FREEZE_BOX, 64, 64);
	}
	
	public void onActivate() 
	{
		
	}
	
	public void onDeactivate() 
	{
		
	}
	
	public void onRender(Visuals v) 
	{
		if(LOWERED)
		{
			LEVEL.renderOnAtPoint(v, texture[2 + cycle][0], XPOS, YPOS, DRAWX, DRAWY);
		}
		else if(cycle == 0)
		{
			v.tween(new BufferedImage[]{texture[0][0], texture[1][0]}, AGE, 60, LEVEL.XLOC + XPOS - (DRAWX / 2), LEVEL.YLOC + YPOS - (DRAWY / 2), DRAWX, DRAWY, false);
		}
	}
	
	public void onTick() 
	{
		CAN_COLLIDE = !LOWERED;
		
		if(LOWERED && AGE % 5 == 0)
		{
			cycle = Utilities.clamp(cycle + 1, 0, 7);
		}
		else
		{
			cycle = Utilities.clamp(cycle - 1, 0, 7);
		}
	}
	
	public void onCollide(GameEntity hit)
	{
		
	}
}
