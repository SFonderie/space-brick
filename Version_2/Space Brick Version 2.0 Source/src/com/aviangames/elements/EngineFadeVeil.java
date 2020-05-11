package com.aviangames.elements;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.aviangames.enginejava.EngineCore;
import com.aviangames.enginejava.EngineElement;
import com.aviangames.enginejava.EngineState;
import com.aviangames.enginejava.EngineVisuals;

public class EngineFadeVeil extends EngineElement
{
	private double fadeLevel = 1.0;
	
	private double fadeSpeed = 0.01;
	
	private boolean fadeDir = true;
	
	public EngineFadeVeil(EngineState parent, double time)
	{
		super(parent);
		
		this.fadeSpeed = 1 / (this.getState().getEngine().getUpdateFrequency() * time);
	}
	
	public EngineFadeVeil(EngineElement parent, double time)
	{
		super(parent);
		
		this.fadeSpeed = 1 / (this.getState().getEngine().getUpdateFrequency() * time);
	}

	@Override
	public void onTick()
	{
		if(fadeDir)
		{
			this.fadeLevel = EngineCore.forceInvariance(this.fadeLevel - this.fadeSpeed, 0.0, 1.0);
		}
		else
		{
			this.fadeLevel = EngineCore.forceInvariance(this.fadeLevel + this.fadeSpeed, 0.0, 1.0);
		}
	}

	@Override
	public void onRender(EngineVisuals v)
	{
		v.fillRect(0, 0, v.WIDTH, v.HEIGHT, 0.0, 0.0, 0.0, this.fadeLevel);
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
		
	}

	@Override
	public void onMouseEvent(MouseEvent event, boolean direction)
	{
		
	}

	@Override
	public void onMouseMoveEvent(MouseEvent event, boolean dragged)
	{
		
	}
	
	public boolean allFade()
	{
		return !this.fadeDir && this.fadeLevel >= 1.0;
	}
	
	public boolean noFade()
	{
		return this.fadeDir && this.fadeLevel <= 0.0;
	}
	
	public boolean fadeDir()
	{
		return this.fadeDir;
	}
	
	public void toggle()
	{
		this.fadeDir = !this.fadeDir;
	}
}
