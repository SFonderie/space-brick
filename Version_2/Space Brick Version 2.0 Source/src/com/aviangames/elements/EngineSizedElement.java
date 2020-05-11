package com.aviangames.elements;

import com.aviangames.enginejava.EngineElement;
import com.aviangames.enginejava.EngineState;

public abstract class EngineSizedElement extends EngineElement
{
	protected int xPos;
	
	protected int yPos;
	
	protected int width;
	
	protected int height;
	
	public EngineSizedElement(EngineState parent, int x, int y, int w, int h)
	{
		super(parent);
		
		this.xPos = x;
		this.yPos = y;
		this.width = w;
		this.height = h;
	}
	
	public EngineSizedElement(EngineElement parent, int x, int y, int w, int h)
	{
		super(parent);
		
		this.xPos = x;
		this.yPos = y;
		this.width = w;
		this.height = h;
	}
}
