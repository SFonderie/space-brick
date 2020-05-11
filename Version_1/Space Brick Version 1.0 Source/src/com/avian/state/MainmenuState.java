package com.avian.state;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.avian.engine.GameFrame;
import com.avian.engine.Utilities;
import com.avian.engine.Visuals;

public class MainmenuState extends GameState
{
	private BufferedImage[][] background, buttons;
	private int fadeIn = 240;
	private boolean doFade;
	
	public final int NUM_BUTTONS = 5;
	public int[] BUTTON_SEQ = new int[NUM_BUTTONS];
	public boolean[] BUTTON_ACT = new boolean[NUM_BUTTONS];
	public String[] BUTTON_STR = {" NEW GAME ", " CONTINUE ", " ENDLESS ", " SETTINGS ", " QUIT GAME "};
	public boolean FADE_OUT;
	
	public MainmenuState(GameFrame frame, boolean fade)
	{
		super(frame);
		
		background = Visuals.cut(FRAME.ENGINE.UTIL.G_MENU_BACKGROUND, 128, 72);
		buttons = Visuals.cut(FRAME.ENGINE.UTIL.G_MENU_BUTTONS, 72, 18);
		doFade = fade;
		FADE_OUT = false;
	}
	
	public MainmenuState(GameFrame frame, boolean fade, int age)
	{
		super(frame);
		
		background = Visuals.cut(FRAME.ENGINE.UTIL.G_MENU_BACKGROUND, 128, 72);
		buttons = Visuals.cut(FRAME.ENGINE.UTIL.G_MENU_BUTTONS, 72, 18);
		doFade = fade;
		AGE = age;
		FADE_OUT = false;
	}
	
	public void onActivate()
	{
		
	}
	
	public void onDeactivate()
	{
		
	}
	
	public void onRender(Visuals v)
	{
		v.tween(new BufferedImage[] {background[0][0], background[0][1]}, AGE, 255, 0, 0, 1920, 1080, false);
		v.drawImage(FRAME.ENGINE.UTIL.G_MENU_LOGO, 860, 270, 960, 540);
		v.drawText("ENGINE PROGRAMMED BY SYDNEY FONDERIE", 940, 810, 800, 40);
		v.drawText("ORIGINAL ARTWORK BY SYDNEY FONDERIE", 940, 870, 800, 40);
		v.drawText("GAME CONSTRUCTED BY SYDNEY FONDERIE", 940, 930, 800, 40);
		
		for(int i = 0; i < NUM_BUTTONS; i++)
		{
			if(BUTTON_ACT[i] && BUTTON_SEQ[i] >= 3)
			{
				v.drawText(BUTTON_STR[i], 275, 240 + (160 * i), 480, 60);
			}
			
			v.drawImage(buttons[0][BUTTON_SEQ[i]], 225, 210 + (i * 160), 580, 120);
		}
		
		v.drawText("SPACE BRICK CLASSIC", 160, 20, 1600, 100);
		v.drawText("THE OLDEST VERSION OF SPACE BRICK, BUILT FROM SCRATCH", 160, 120, 1600, 60);
		
		
		if(AGE <= 60 && doFade)
		{
			fadeIn -= 4;
			
			if(fadeIn < 0)
			{
				fadeIn = 0;
			}
			
			v.GRAPHICS.setColor(new Color(0, 0, 0, fadeIn));
			v.GRAPHICS.fillRect(FRAME.BORDER_WIDTH, FRAME.BORDER_HEIGHT, FRAME.WIDTH, FRAME.HEIGHT);
		}
		
		if(FADE_OUT)
		{
			fadeIn = Utilities.clamp(fadeIn + 4, 0, 255);
			
			v.GRAPHICS.setColor(new Color(0, 0, 0, fadeIn));
			v.GRAPHICS.fillRect(FRAME.BORDER_WIDTH, FRAME.BORDER_HEIGHT, FRAME.WIDTH, FRAME.HEIGHT);
			
			if(fadeIn == 255)
			{
				startGame();
			}
		}
	}
	
	public void onTick()
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
	
	public void fadeOut()
	{
		FADE_OUT = true;
		doFade = false;
		fadeIn = 15;
	}
	
	public void startGame()
	{
		FRAME.setActiveState(new CreationState(FRAME));
	}
}
