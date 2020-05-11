package com.aviangames.entity;

import java.awt.image.BufferedImage;

import com.aviangames.elements.EngineScene;
import com.aviangames.enginejava.EngineVisuals;

public class TileEntity extends EngineEntity
{
	public static final int SCALE = 128;
	
	private BufferedImage[] t_tiles;
	
	private int id;
	
	public TileEntity(EngineScene parent, int id, int x, int y)
	{
		super(parent, TileEntity.getHitbox(id), x, y, TileEntity.SCALE, TileEntity.SCALE, 0.0);
		
		this.t_tiles = this.level.getState().getEngine().getAssetLibrary().getImageArray("STATION");
		this.id = id;
	}

	@Override
	public void onTick()
	{
		
	}

	@Override
	public void onRender(EngineVisuals v) 
	{
		if(this.id == 5 || this.id == 10 || this.id == 11 || this.id == 16)
		{
			v.drawImage(this.t_tiles[1], this.level.XLOC + this.XPOS - this.dWidth / 2, this.level.YLOC + this.YPOS - this.dHeight / 2, this.dWidth, this.dHeight);
		}
		else
		{
			v.drawImage(this.t_tiles[this.id], this.level.XLOC + this.XPOS - this.dWidth / 2, this.level.YLOC + this.YPOS - this.dHeight / 2, this.dWidth, this.dHeight);
		}
	}
	
	public void onLateRender(EngineVisuals v)
	{
		if(this.id == 5 || this.id == 10 || this.id == 11 || this.id == 16)
		{
			v.drawImage(this.t_tiles[this.id], this.level.XLOC + this.XPOS - this.dWidth / 2, this.level.YLOC + this.YPOS - this.dHeight / 2, this.dWidth, this.dHeight);
		}
	}

	@Override
	public void onCollide(EngineEntity entity) 
	{
		return;
	}
	
	public static Hitbox[] getHitbox(int id)
	{
		if(id < 2)
		{
			return new Hitbox[] {};
		}
		
		if(id == 2 || id == 12 || id == 13)
		{
			return new Hitbox[] { new Hitbox(
					(int) (TileEntity.SCALE * (0.0 / 1.0)), 
					(int) (TileEntity.SCALE * (-1.0 / 4.0)), 
					(int) (TileEntity.SCALE * (1.0 / 1.0)), 
					(int) (TileEntity.SCALE * (7.0 / 8.0)), 
					true) };
		}
		
		if(id == 3 || id == 4 || id == 6 || id == 7 || id == 8 || id == 9 || id == 14 || id == 15 || id > 16)
		{
			return new Hitbox[] { new Hitbox(
					(int) (TileEntity.SCALE * (0.0 / 1.0)), 
					(int) (TileEntity.SCALE * (0.0 / 1.0)), 
					(int) (TileEntity.SCALE * (1.0 / 1.0)), 
					(int) (TileEntity.SCALE * (1.0 / 1.0)), 
					true) };
		}
		
		if(id == 5 || id == 10 || id == 11 || id == 16)
		{
			return new Hitbox[] { new Hitbox(
					(int) (TileEntity.SCALE * (0.0 / 1.0)), 
					(int) (TileEntity.SCALE * (5.0 / 16.0)), 
					(int) (TileEntity.SCALE * (1.0 / 1.0)), 
					(int) (TileEntity.SCALE * (1.0 / 16.0)), 
					true) };
		}
		
		return new Hitbox[] {};
	}
}
