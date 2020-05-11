package com.aviangames.gamestate;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.aviangames.elements.EngineFadeVeil;
import com.aviangames.elements.EnginePauseScreen;
import com.aviangames.elements.EngineScene;
import com.aviangames.enginejava.EngineCore;
import com.aviangames.enginejava.EngineState;
import com.aviangames.enginejava.EngineVisuals;

public class GameState extends EngineState
{
	private EnginePauseScreen pauseScreen;
	
	private EngineFadeVeil fader;
	
	private EngineScene scene;
	
	public GameState(EngineCore engine)
	{
		super(engine);
		
		this.scene = new EngineScene(this);
		this.activateElement(this.scene);
		
		this.fader = new EngineFadeVeil(this, 2.0);
		this.activateElement(this.fader);
		
		this.pauseScreen = new EnginePauseScreen(this);
		this.activateElement(this.pauseScreen);
		this.pauseScreen.setInactive(true);
	}

	@Override
	public void onTick()
	{
		if(this.pauseScreen.isInactive())
		{
			
		}
	}

	@Override
	public void onRender(EngineVisuals v)
	{
		v.fillRect(0, 0, v.WIDTH, v.HEIGHT, 0.2, 0.2, 0.8, 1.0);
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
		if(event.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			this.pauseScreen.setInactive(false);
		}
	}

	@Override
	public void onMouseEvent(MouseEvent event, boolean direction)
	{
		if(this.pauseScreen.isInactive())
		{
			
		}
	}

	@Override
	public void onMouseMoveEvent(MouseEvent event, boolean dragged)
	{
		if(this.pauseScreen.isInactive())
		{
			
		}
	}
}
