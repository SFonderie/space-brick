package com.avian.level;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.avian.engine.GameCore;
import com.avian.engine.Visuals;
import com.avian.entity.EntityPlayer;
import com.avian.entity.GameEntity;
import com.avian.entity.TileEntity;
import com.avian.state.GameState;

public abstract class GameLevel implements GameCore 
{
	public EntityPlayer PLAYER;
	public GameState STATE;
	public int XLOC, YLOC;
	public int AGE;
	
	public ArrayList<GameEntity> ACTORS = new ArrayList<GameEntity>();
	public ArrayList<TileEntity> ATILES = new ArrayList<TileEntity>();
	
	public GameLevel(GameState state)
	{
		STATE = state;
		XLOC = 0;
		YLOC = 0;
		AGE = 0;
		
		PLAYER = STATE.FRAME.ENGINE.UTIL.getPlayer(this);
		addActor(PLAYER);
	}
	
	public abstract void onEvent(int eventID);
	
	public final void tick()
	{
		for(int i = 0; i < ATILES.size(); i++)
		{
			ATILES.get(i).onTick();
		}
		
		checkForCollisions();
		
		for(int i = 0; i < ACTORS.size(); i++)
		{
			ACTORS.get(i).tick();
		}
		
		AGE++;
		onTick();
	}
	
	public final void render(Visuals v)
	{
		onRender(v);
		sortActors();
		
		for(int i = 0; i < ATILES.size(); i++)
		{
			ATILES.get(i).render(v);
		}
		
		for(int i = 0; i < ACTORS.size(); i++)
		{
			ACTORS.get(i).render(v);
		}
		
		for(int i = 0; i < ATILES.size(); i++)
		{
			ATILES.get(i).lateRender(v);
		}
	}
	
	public void renderOnAtPoint(Visuals v, BufferedImage texture, int x, int y, int w, int h)
	{
		v.drawImage(texture, XLOC + x - (w / 2), YLOC + y - (h / 2), w, h);
	}
	
	public final void addActor(GameEntity actor)
	{
		ACTORS.add(actor);
		actor.onActivate();
	}
	
	public final void removeActor(long id)
	{
		for(int i = 0; i < ACTORS.size(); i++)
		{
			if(ACTORS.get(i).SPAWN_ID == id)
			{
				ACTORS.remove(i);
				return;
			}
		}
	}
	
	public final EntityPlayer getPlayer()
	{
		return (EntityPlayer) getActor(PLAYER.SPAWN_ID);
	}
	
	public final GameEntity getActor(long id)
	{
		for(int i = 0; i < ACTORS.size(); i++)
		{
			if(ACTORS.get(i).SPAWN_ID == id)
			{
				return ACTORS.get(i);
			}
		}
		
		return null;
	}
	
	public final void checkForCollisions()
	{
		for(int i = 0; i < ACTORS.size(); i++)
		{	
			ACTORS.get(i).COL_UP = false;
			ACTORS.get(i).COL_DOWN = false;
			ACTORS.get(i).COL_LEFT = false;
			ACTORS.get(i).COL_RIGHT = false;
			ACTORS.get(i).COLLIDING = false;
			
			for(int j = 0; j < ACTORS.size(); j++)
			{
				if(ACTORS.size() <= i || ACTORS.size() <= j || !ACTORS.get(j).CAN_COLLIDE)
				{
					continue;
				}
				
				ACTORS.get(i).checkForCollisionWith(ACTORS.get(j));
			}
			
			for(int j = 0; j < ATILES.size(); j++)
			{
				ACTORS.get(i).checkForCollisionWith(ATILES.get(j));
			}
		}
	}
	
	public void sortActors()
	{
		for(int i = 0; i < ACTORS.size(); i++)
		{
			for(int j = i + 1; j < ACTORS.size(); j++)
			{
				if(ACTORS.get(j).YPOS < ACTORS.get(i).YPOS)
				{
					GameEntity placeholder = ACTORS.get(i);
					ACTORS.set(i, ACTORS.get(j));
					ACTORS.set(j, placeholder);
				}
			}
		}
	}
}
