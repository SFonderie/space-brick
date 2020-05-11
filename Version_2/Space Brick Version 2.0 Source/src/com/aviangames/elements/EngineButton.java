package com.aviangames.elements;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.aviangames.enginejava.EngineElement;
import com.aviangames.enginejava.EngineState;
import com.aviangames.enginejava.EngineVisuals;

public class EngineButton extends EngineSizedElement
{
	protected final BufferedImage[] textures;

	protected boolean buttonActive = false;

	private String label = "";

	private int labelFont = 0;

	private double labelRatio = 1.0;

	protected boolean renderLabel = false;

	public EngineButton(EngineState parent, int x, int y, int w, int h, BufferedImage[] textures)
	{
		super(parent, x, y, w, h);

		this.textures = textures;
	}

	public EngineButton(EngineElement parent, int x, int y, int w, int h, BufferedImage[] textures)
	{
		super(parent, x, y, w, h);

		this.textures = textures;
	}

	@Override
	public void onTick()
	{

	}

	@Override
	public void onRender(EngineVisuals v)
	{
		if (this.buttonActive)
		{
			v.drawImage(this.textures[1], this.xPos, this.yPos, this.width, this.height);
		}
		else
		{
			v.drawImage(this.textures[0], this.xPos, this.yPos, this.width, this.height);
		}
		
		if(this.renderLabel)
		{
			this.renderLabel(v);
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

	}

	@Override
	public void onMouseEvent(MouseEvent event, boolean direction)
	{

	}

	@Override
	public void onMouseMoveEvent(MouseEvent event, boolean dragged)
	{
		int mX = this.getState().getEngine().convertMouseX(event.getX());
		int mY = this.getState().getEngine().convertMouseY(event.getY());

		boolean xDir = mX >= this.xPos && mX <= this.xPos + this.width;
		boolean yDir = mY >= this.yPos && mY <= this.yPos + this.height;

		this.buttonActive = xDir && yDir;
	}
	
	public void setLabel(String label, int font, double ratio)
	{
		this.label = label;
		this.labelFont = font;
		this.labelRatio = ratio;
		this.renderLabel = true;
	}
	
	protected void renderLabel(EngineVisuals v)
	{
		int width = (int)(this.width * this.labelRatio);
		int height = (int)(this.height * this.labelRatio);
		int dX = (this.width - width) / 2;
		int dY = (this.height - height) / 2;
		
		v.drawText(this.label, this.labelFont, this.xPos + dX, this.yPos + dY, width, height);
	}
	
	public boolean active()
	{
		return this.buttonActive;
	}
}
