package com.avian.state;

import java.awt.Color;
import com.avian.engine.GameFrame;
import com.avian.engine.Visuals;

public class OvertureState extends GameState
{
	private int alpha;
	
	public OvertureState(GameFrame frame)
	{
		super(frame);
		alpha = 255;
	}
	
	public void onActivate()
	{
		
	}
	
	public void onDeactivate()
	{
		
	}
	
	public void onRender(Visuals v)
	{
		if(AGE < 60)
		{
			alpha = 255;
		}
		else if(AGE < 140)
		{
			alpha -= 3;
		}
		else if(AGE < 320)
		{
			alpha = 15;
		}
		else if(AGE < 400)
		{
			alpha += 3;
		}
		else if(AGE < 460)
		{
			alpha = 255;
		}
		else
		{
			cont();
		}
		
		v.drawImage(FRAME.ENGINE.UTIL.G_OVERTURE_SPLASH, 0, 0, 1920, 1080);
		v.GRAPHICS.setColor(new Color(0, 0, 0, alpha));
		v.GRAPHICS.fillRect(FRAME.BORDER_WIDTH, FRAME.BORDER_HEIGHT, FRAME.WIDTH, FRAME.HEIGHT);

	}
	
	public void onTick() 
	{
		
	}
	
	public void cont()
	{
		FRAME.setActiveState(new MainmenuState(FRAME, true));
	}
}
