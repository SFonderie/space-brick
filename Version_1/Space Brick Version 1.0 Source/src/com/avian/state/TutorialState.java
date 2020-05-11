package com.avian.state;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.avian.engine.GameData;
import com.avian.engine.GameFrame;
import com.avian.engine.Utilities;
import com.avian.engine.Visuals;
import com.avian.level.GameLevel;
import com.avian.level.OpeningLevel;

public class TutorialState extends GameState
{
	private String[] buttonLabels = {" RESUME ", "SETTINGS", "MAIN MENU"};
	private BufferedImage[][] buttons;
	private int fadeIn;
	
	public GameLevel ACTIVE_LEVEL;
	public final int NUM_BUTTONS = 3;
	public boolean INTRO_OVER;
	public boolean ESC_SCREEN;
	public boolean[] BUTTON_ACT = new boolean[NUM_BUTTONS];
	public int[] BUTTON_SEQ = new int[NUM_BUTTONS];
	
	public TutorialState(GameFrame frame, GameData data) 
	{
		super(frame);
		
		ACTIVE_LEVEL = new OpeningLevel(this);
		FRAME.getContentPane().setCursor(FRAME.ENGINE.UTIL.CURSORS[1]);
		buttons = Visuals.cut(FRAME.ENGINE.UTIL.G_MENU_BUTTONS, 72, 18);
		INTRO_OVER = false;
		ESC_SCREEN = false;
		fadeIn = 255;
	}
	
	public void onActivate()
	{
		
	}
	
	public void onDeactivate() 
	{
		
	}
	
	public void onRender(Visuals v)
	{
		if(AGE < 200)
		{
			fadeIn = Utilities.clamp(fadeIn - 2, 0, 255);
		}
		else
		{
			INTRO_OVER = true;
			ACTIVE_LEVEL.PLAYER.CROSSHAIR = true;
		}
		
		ACTIVE_LEVEL.render(v);
		
		if(ESC_SCREEN)
		{
			v.GRAPHICS.setColor(new Color(0, 200, 240, 127));
			v.GRAPHICS.fillRect(FRAME.BORDER_WIDTH, FRAME.BORDER_HEIGHT, FRAME.WIDTH, FRAME.HEIGHT);
			v.drawText("GAME PAUSED", 400, 60, 1120, 120);
			
			for(int i = 0; i < NUM_BUTTONS; i++)
			{
				if(BUTTON_ACT[i] && BUTTON_SEQ[i] >= 3)
				{
					v.drawText(buttonLabels[i], 150, 320 + (i * 240), 500, 80);
				}
				
				v.drawImage(buttons[0][BUTTON_SEQ[i]], 100, 300 + (i * 240), 600, 120);
			}
		}
		else
		{
			if(INTRO_OVER)
			{
				v.drawHUD(ACTIVE_LEVEL.PLAYER);
			}
		}
		
		v.GRAPHICS.setColor(new Color(0, 0, 0, fadeIn));
		v.GRAPHICS.fillRect(FRAME.BORDER_WIDTH, FRAME.BORDER_HEIGHT, FRAME.WIDTH, FRAME.HEIGHT);
	}
	
	public void onTick()
	{
		if(!ESC_SCREEN)
		{
			ACTIVE_LEVEL.tick();
		}
		else
		{
			for(int i = 0; i < NUM_BUTTONS; i++)
			{
				if(BUTTON_ACT[i])
				{
					if(BUTTON_SEQ[i] < 4)
					{
						BUTTON_SEQ[i]++;
					}
					else
					{
						continue;
					}
				}
				else
				{
					if(BUTTON_SEQ[i] > 0)
					{
						BUTTON_SEQ[i]--;
					}
					else
					{
						continue;
					}
				}
			}
		}
	}
}
