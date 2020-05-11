package com.aviangames.elements;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.aviangames.enginejava.EngineElement;
import com.aviangames.enginejava.EngineState;
import com.aviangames.enginejava.EngineVisuals;
import com.aviangames.entity.EngineEntity;
import com.aviangames.entity.EntityBoss;
import com.aviangames.entity.EntityPlayer;
import com.aviangames.entity.Hitbox;
import com.aviangames.entity.TileEntity;
import com.aviangames.utilities.Weapon;

public class EngineScene extends EngineElement
{
	public ArrayList<EngineEntity> ACTORS = new ArrayList<EngineEntity>();
	
	public ArrayList<TileEntity> ATILES = new ArrayList<TileEntity>();
	
	public ArrayList<EngineEntity> ADDLIST = new ArrayList<EngineEntity>();
	
	public ArrayList<EngineEntity> PULLLIST = new ArrayList<EngineEntity>();

	public int XLOC = 0;

	public int YLOC = 0;
	
	public EntityPlayer player;
	
	private BufferedImage t_ammo;
	
	private BufferedImage[] t_reload, t_weapons;
	
	private boolean hitboxes = false;
	
	public EngineScene(EngineState parent)
	{
		super(parent);
		
		this.t_ammo = this.getState().getEngine().getAssetLibrary().getImage("AMMO");
		this.t_reload = this.getState().getEngine().getAssetLibrary().getImageArray("RELOAD");
		this.t_weapons = this.getState().getEngine().getAssetLibrary().getImageArray("WEAPONS");
		
		int size = (int) (TileEntity.SCALE * (2.0 / 3.0));
		this.player = new EntityPlayer(this, new Hitbox[] { new Hitbox(0, 0, size / 2, size, false), new Hitbox(0, 0, size / 2, size / 2, true) }, 480, 270, size, size);
		this.attemptAddition(this.player);
		
		this.attemptAddition(new EntityBoss(this, new Hitbox[] { new Hitbox(0, 0, 240, 240, true) }, 3072, 1024, 384, 384, 0.0));
		
		try 
		{
			this.loadTilesTo("station_dorms", 0, 0);
			this.loadTilesTo("station_arena", 1, 0);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onTick()
	{
		for(EngineEntity entity : this.ADDLIST)
		{
			if(!this.ACTORS.contains(entity))
			{
				this.ACTORS.add(entity);
			}
		}
		
		for(EngineEntity entity : this.PULLLIST)
		{
			if(this.ACTORS.contains(entity))
			{
				this.ACTORS.remove(entity);
			}
		}
		
		this.ACTORS = this.mergeSort(this.ACTORS);
		
		for (EngineEntity entity : this.ACTORS)
		{
			entity.tick();
		}

	}

	@Override
	public void onRender(EngineVisuals v)
	{
		for(TileEntity entity : this.ATILES)
		{
			entity.onRender(v);
		}
		
		for(EngineEntity entity : this.ACTORS)
		{
			entity.onRender(v);
		}
		
		for(TileEntity entity : this.ATILES)
		{
			entity.onLateRender(v);
			if(this.hitboxes) entity.renderHitboxes(v);
		}
		
		if(this.hitboxes) for(EngineEntity entity : this.ACTORS) entity.renderHitboxes(v);
		
		if(this.player.IS_RELOADING)
		{
			int progress = (int) (((this.getElapsedTime() - this.player.ageAtReloadStart) / Weapon.WEAPONS[this.player.WEAPON].RELOAD) * this.t_reload.length);
			v.drawImage(this.t_reload[progress], this.XLOC + this.player.XPOS - 32, this.YLOC + this.player.YPOS - 128, 64, 64);
		}
		
		v.drawImage(this.t_weapons[this.player.WEAPON], 0, 0, 128, 128);
		v.drawText(Weapon.WEAPONS[this.player.WEAPON].TITLE, 0, 144, 32, Weapon.WEAPONS[this.player.WEAPON].TITLE.length() * 12, 16);
		
		int clip = Weapon.WEAPONS[this.player.WEAPON].CLIPSIZE;
		int halfway = Weapon.WEAPONS[this.player.WEAPON].CLIPSIZE == 1 ? clip : clip / 2;
		
		for(int i = 0; i < this.player.magazines[this.player.WEAPON]; i++)
		{
			v.drawImage(this.t_ammo, 32 * (i / halfway + 1), 108 + 6 * (i % halfway), 16, 6);
		}
		
	}

	@Override
	public void onActivate()
	{

	}

	@Override
	public void onDeactivate()
	{

	}

	@Override
	public void onKeyEvent(KeyEvent event, boolean direction)
	{
		if(event.getKeyCode() == KeyEvent.VK_W)
		{
			this.player.FORWARD = direction;
			if(!this.player.MOVING) this.player.ageAtWalkStart = this.getElapsedTime();
			if(direction) this.player.BACK = false;
		}
		
		if(event.getKeyCode() == KeyEvent.VK_S)
		{
			this.player.BACK = direction;
			if(!this.player.MOVING) this.player.ageAtWalkStart = this.getElapsedTime();
			if(direction) this.player.FORWARD = false;
		}
		
		if(event.getKeyCode() == KeyEvent.VK_A)
		{
			this.player.LEFT = direction;
			if(!this.player.MOVING) this.player.ageAtWalkStart = this.getElapsedTime();
			if(direction) this.player.RIGHT = false;
		}
		
		if(event.getKeyCode() == KeyEvent.VK_D)
		{
			this.player.RIGHT = direction;
			if(!this.player.MOVING) this.player.ageAtWalkStart = this.getElapsedTime();
			if(direction) this.player.LEFT = false;
		}
		
		if(event.getKeyCode() == KeyEvent.VK_SPACE && direction)
		{
			this.player.switchWeapon();
		}
		
		if(event.getKeyCode() == KeyEvent.VK_R && !this.player.IS_RELOADING && this.player.magazines[this.player.WEAPON] < Weapon.WEAPONS[this.player.WEAPON].CLIPSIZE)
		{
			this.player.IS_RELOADING = true;
			this.player.ageAtReloadStart = this.getElapsedTime();
		}
		
		if(event.getKeyCode() == KeyEvent.VK_H && direction)
		{
			this.hitboxes = !this.hitboxes;
		}
	}

	@Override
	public void onMouseEvent(MouseEvent event, boolean direction)
	{
		if(event.getButton() == MouseEvent.BUTTON1)
		{
			this.player.IS_FIRING = direction;
		}
	}

	@Override
	public void onMouseMoveEvent(MouseEvent event, boolean dragged)
	{
		this.player.facePoint(this.getState().getEngine().convertMouseX(event.getX()), this.getState().getEngine().convertMouseY(event.getY()));
	}
	
	public void attemptAddition(EngineEntity entity)
	{
		this.ADDLIST.add(entity);
	}
	
	public void attemptRemoval(EngineEntity entity)
	{
		this.PULLLIST.add(entity);
	}
	
	/**
	 * MergeSort algorithm taken from Codexpedia.
	 */
	public ArrayList<EngineEntity> mergeSort(ArrayList<EngineEntity> whole)
	{
		ArrayList<EngineEntity> left = new ArrayList<EngineEntity>();
		ArrayList<EngineEntity> right = new ArrayList<EngineEntity>();
		int center;

		if (whole.size() == 1)
		{
			return whole;
		}
		else
		{
			center = whole.size() / 2;

			for (int i = 0; i < center; i++)
			{
				left.add(whole.get(i));
			}

			for (int i = center; i < whole.size(); i++)
			{
				right.add(whole.get(i));
			}
			
			left = this.mergeSort(left);
			right = this.mergeSort(right);
			
			this.merge(left, right, whole);
		}
		
		return whole;
	}
	
	/**
	 * MergeSort algorithm submethod taken from Codexpedia.
	 */
	private void merge(ArrayList<EngineEntity> left, ArrayList<EngineEntity> right, ArrayList<EngineEntity> whole)
	{
		int leftIndex = 0;
		int rightIndex = 0;
		int wholeIndex = 0;
		
		while (leftIndex < left.size() && rightIndex < right.size())
		{
			if (left.get(leftIndex).YPOS < right.get(rightIndex).YPOS)
			{
				whole.set(wholeIndex, left.get(leftIndex));
				leftIndex++;
			}
			else
			{
				whole.set(wholeIndex, right.get(rightIndex));
				rightIndex++;
			}
			
			wholeIndex++;
		}

		ArrayList<EngineEntity> rest;
		int restIndex;
		
		if (leftIndex >= left.size())
		{
			rest = right;
			restIndex = rightIndex;
		}
		else
		{
			rest = left;
			restIndex = leftIndex;
		}
		
		for (int i = restIndex; i < rest.size(); i++)
		{
			whole.set(wholeIndex, rest.get(i));
			wholeIndex++;
		}
	}
	
	public void loadTilesTo(String fileloc, int x, int y) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(EngineScene.class.getResourceAsStream("/level/" + fileloc + ".dlf")));
		
		int dX = x * TileEntity.SCALE * 16;
		int dY = y * TileEntity.SCALE * 16;
		
		for(int j = 0; j < 16; j++)
		{
			String[] tiles = reader.readLine().split(",");
			
			for(int i = 0; i < 16; i++)
			{
				this.ATILES.add(new TileEntity(this, Integer.parseInt(tiles[i]), (i * TileEntity.SCALE) + dX, (j * TileEntity.SCALE) + dY));
			}
		}
	}
}
