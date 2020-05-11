package com.avian.entity;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import com.avian.engine.Visuals;
import com.avian.level.GameLevel;

public class EntityPlayerProjectile extends GameEntity 
{
	private BufferedImage[][] textures;
	private EntityPlayer player;
	
	public int MOVE_SPEED, PROJECTILE_TYPE;
	
	public EntityPlayerProjectile(GameLevel level, EntityPlayer launcher, int xPos, int yPos, int drawX, int drawY, double theta, int moveSpeed, int type) 
	{
		super(level, xPos, yPos, drawX, drawY, theta);
		
		addHitboxes(new Hitbox[]{
				new Hitbox(0, 0, DRAWX / 4, DRAWY / 4, false)
		});
		
		textures = Visuals.cut(LEVEL.STATE.FRAME.ENGINE.UTIL.E_PLAYER_PROJECTILES, 64, 64);
		MOVE_SPEED = moveSpeed;
		PROJECTILE_TYPE = type;
		player = launcher;
	}

	public void onActivate() 
	{
		
	}
	
	public void onDeactivate()
	{
		
	}
		
	public void onRender(Visuals v) 
	{
		BufferedImage rotated = new BufferedImage(64, 64, 2);
		new AffineTransformOp(AffineTransform.getRotateInstance((Math.PI * 0.5) - THETA, 32, 32), AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(textures[PROJECTILE_TYPE % 4][PROJECTILE_TYPE / 4], rotated);
		LEVEL.renderOnAtPoint(v, rotated, XPOS, YPOS, DRAWX, DRAWY);
	}
	
	public void onTick() 
	{
		if(AGE > 300)
		{
			KILL_ON_NEXT = true;
		}
		
		XPOS += MOVE_SPEED * Math.sin(THETA);
		YPOS += MOVE_SPEED * Math.cos(THETA);
	}
	
	public void onCollide(GameEntity hit)
	{
		if(hit instanceof EntityPlayerProjectile)
		{
			return;
		}
		
		if(hit != player)
		{
			KILL_ON_NEXT = true;
		}
	}
}
