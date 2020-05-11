package com.avian.level;

import com.avian.engine.Visuals;
import com.avian.entity.EntityFreezer;
import com.avian.state.GameState;

public class OpeningLevel extends GameLevel
{
	private EntityFreezer capsule;
	
	public OpeningLevel(GameState state) 
	{
		super(state);
		
		capsule = new EntityFreezer(this, 960, 508, 256, 256, 0.0);
		addActor(capsule);
		new GameTile("Tutorial", this, 192, -252);
	}
	
	public void onEvent(int eventID)
	{
		
	}
	
	public void onActivate()
	{
		
	}
	
	public void onDeactivate() 
	{
	}
	
	public void onRender(Visuals v)
	{
		
	}
	
	public void onTick() 
	{
		
	}
}
