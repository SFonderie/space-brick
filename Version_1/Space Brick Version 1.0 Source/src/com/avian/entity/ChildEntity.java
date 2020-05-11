package com.avian.entity;

import com.avian.engine.GameCore;

public abstract class ChildEntity implements GameCore
{
	public GameEntity PARENT;
	public int RELATIVE_X, RELATIVE_Y;
	public int DRAWX, DRAWY;
	
	public ChildEntity(GameEntity parent, int relativeX, int relativeY, int drawX, int drawY)
	{
		PARENT = parent;
		RELATIVE_X = relativeX;
		RELATIVE_Y = relativeY;
		DRAWX = drawX;
		DRAWY = drawY;
	}
}
