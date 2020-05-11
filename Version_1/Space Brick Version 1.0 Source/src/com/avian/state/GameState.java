package com.avian.state;

import java.awt.Color;

import com.avian.engine.GameFrame;
import com.avian.engine.GameCore;
import com.avian.engine.Visuals;

public abstract class GameState implements GameCore
{
	public GameFrame FRAME;
	public int AGE;
	
	public GameState(GameFrame frame)
	{
		FRAME = frame;
		AGE = 0;
	}
	
	public final void tick()
	{
		onTick();
		AGE++;
	}
	
	public final void render(Visuals v)
	{
		v.GRAPHICS.setColor(new Color(0, 0, 0, 255));
		v.GRAPHICS.fillRect(0, 0, FRAME.SCREEN_WIDTH, FRAME.SCREEN_HEIGHT);
		onRender(v);
	}
}
