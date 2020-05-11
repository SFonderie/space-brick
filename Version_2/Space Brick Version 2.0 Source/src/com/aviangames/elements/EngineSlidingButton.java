package com.aviangames.elements;

import java.awt.image.BufferedImage;

import com.aviangames.enginejava.EngineCore;
import com.aviangames.enginejava.EngineElement;
import com.aviangames.enginejava.EngineState;
import com.aviangames.enginejava.EngineVisuals;

public class EngineSlidingButton extends EngineButton
{
	private int buttonFrame = 0;
	
	private int speed;
	
	public EngineSlidingButton(EngineState parent, int x, int y, int w, int h, BufferedImage[] textures, int speed)
	{
		super(parent, x, y, w, h, textures);
		
		this.speed = speed;
	}

	public EngineSlidingButton(EngineElement parent, int x, int y, int w, int h, BufferedImage[] textures, int speed)
	{
		super(parent, x, y, w, h, textures);
		
		this.speed = speed;
	}
	
	@Override
	public void onTick()
	{
		if(this.buttonActive)
		{
			this.buttonFrame = (int) EngineCore.forceInvariance(this.buttonFrame + this.speed, 0, this.textures.length - 1);
		}
		else
		{
			this.buttonFrame = (int) EngineCore.forceInvariance(this.buttonFrame - this.speed, 0, this.textures.length - 1);
		}
	}
	
	@Override
	public void onRender(EngineVisuals v)
	{
		v.drawImage(this.textures[this.buttonFrame], this.xPos, this.yPos, this.width, this.height);
		
		if(this.renderLabel && this.buttonFrame >= this.textures.length / 2)
		{
			this.renderLabel(v);
		}
	}
}
