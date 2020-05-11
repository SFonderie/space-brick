package com.aviangames.elements;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.aviangames.enginejava.EngineElement;
import com.aviangames.enginejava.EngineVisuals;
import com.aviangames.gamestate.GameState;
import com.aviangames.gamestate.MenuState;

public class EnginePauseScreen extends EngineElement
{
	private BufferedImage[][] t_buttons;
	
	private EngineSlidingButton[] buttons = new EngineSlidingButton[2];
	
	private String[] labels =
	{
			"RESUME GAME", "LEAVE GAME"
	};
	
	public EnginePauseScreen(GameState parent)
	{
		super(parent);
		
		this.t_buttons = this.getState().getEngine().getAssetLibrary().getImageDoubleArray("SLIDERS");
		
		for(int i = 0; i < buttons.length; i++)
		{
			this.buttons[i] = new EngineSlidingButton(this.getState(), 100, 160 + (i * 170), 400, 100, this.t_buttons[0], 2);
			this.buttons[i].setLabel(this.labels[i], 0, 0.75);
			this.activateElement(this.buttons[i]);
		}
	}
	
	public EnginePauseScreen(EngineElement parent)
	{
		super(parent);
	}

	@Override
	public void onTick()
	{
		
	}

	@Override
	public void onRender(EngineVisuals v)
	{
		v.fillRect(0, 0, v.WIDTH, v.HEIGHT, 0.0, 0.5, 1.0, 0.75);
		v.drawText("SPACE BRICK PAUSED", 0, 100, 40, 760, 80);
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
		if(this.buttons[0].active())
		{
			this.setInactive(true);
		}
		else if(this.buttons[1].active())
		{
			this.getState().getEngine().setActiveState(new MenuState(this.getState().getEngine()));
		}
	}

	@Override
	public void onMouseMoveEvent(MouseEvent event, boolean dragged)
	{
		
	}
}
