package com.avian.engine;

public interface GameCore
{
	public abstract void onActivate();
	
	public abstract void onDeactivate();
	
	public abstract void onRender(Visuals v);
	
	public abstract void onTick();
}
