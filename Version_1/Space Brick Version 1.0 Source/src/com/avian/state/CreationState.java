package com.avian.state;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.avian.engine.GameData;
import com.avian.engine.GameFrame;
import com.avian.engine.Utilities;
import com.avian.engine.Visuals;

public class CreationState extends GameState
{
	private BufferedImage[][] background, torso, head, arms;
	private boolean doFade, dir;
	private int fadeIn;
	
	public boolean[] MOUSE_OVER = new boolean[6];
	public boolean GO_BACK, GO_FORWARD, FADE_OUT = false;
	public int MAIN_CYCLE, ARMS_CYCLE, SPIN_CYCLE;
	public String NAME;
	
	public CreationState(GameFrame frame)
	{
		super(frame);
		
		background = Visuals.cut(FRAME.ENGINE.UTIL.G_MENU_BACKGROUND, 128, 72);
		torso = Visuals.cut(FRAME.ENGINE.UTIL.E_PLAYER_TORSO, 64, 64);
		head = Visuals.cut(FRAME.ENGINE.UTIL.E_PLAYER_HEAD, 64, 64);
		arms = Visuals.cut(FRAME.ENGINE.UTIL.E_PLAYER_ARMS, 64, 64);
		NAME = "";
		fadeIn = 240;
		FADE_OUT = false;
		doFade = true;
		dir = true;
	}
	
	public void onActivate() 
	{
		
	}
	
	public void onDeactivate() 
	{
		
	}
	
	public void onRender(Visuals v)
	{
		v.tween(new BufferedImage[]{background[0][0], background[0][1]}, AGE, 255, 0, 0, 1920, 1080, false);
		v.drawText("CHARACTER CREATION", 300, 50, 1320, 100);
		v.drawImage(FRAME.ENGINE.UTIL.G_SETTINGS_HOVERBOX, 80 + (FRAME.ENGINE.SETTINGS.DIFFICULTY * 300), 895, 260, 90);
		
		v.drawImage(FRAME.ENGINE.UTIL.G_MENU_TEXTBOX, 590, 590, 740, 100);
		v.drawText(NAME, 960 - (NAME.length() * 20), 600, 40 * NAME.length(), 80);
		
		if(GO_FORWARD)
		{
			if(NAME.length() == 0)
			{
				v.drawImage(Visuals.tintImage(FRAME.ENGINE.UTIL.G_SETTINGS_HOVERBOX, 255, 0, 0), 590, 590, 740, 100);
				v.drawText("PLEASE TYPE A NAME", 610, 610, 700, 60);
			}
			
			v.drawImage(FRAME.ENGINE.UTIL.G_SETTINGS_HOVERBOX, 1340, 600, 400, 80);
		}
		
		if(GO_BACK)
		{
			v.drawImage(FRAME.ENGINE.UTIL.G_SETTINGS_HOVERBOX, 180, 600, 400, 80);
		}
		
		for(int i = 0; i < FRAME.ENGINE.SETTINGS.DIFFICULTY_TITLES.length; i++)
		{
			if(MOUSE_OVER[i])
			{
				v.drawImage(FRAME.ENGINE.UTIL.G_SETTINGS_HOVERBOX, 80 + (i * 300), 895, 260, 90);
				v.drawText(FRAME.ENGINE.SETTINGS.DIFFICULTY_DESCRIPTIONS[i], 60, 800, 1800, 60);
			}
			
			v.drawColoredText(FRAME.ENGINE.SETTINGS.DIFFICULTY_TITLES[i], 90 + (i * 300), 900, 240, 80, 255, 252 - (i * 42), 0);
		}
		
		v.drawImage(torso[MAIN_CYCLE][SPIN_CYCLE], 768, 192, 384, 384);
		v.drawImage(head[(SPIN_CYCLE * 2) + MAIN_CYCLE][0], 768, 192, 384, 384);
		v.drawImage(arms[(SPIN_CYCLE * 4) + ARMS_CYCLE][0], 768, 192, 384, 384);
		
		v.drawImage(FRAME.ENGINE.UTIL.G_MENU_TEXTBOX, 180, 600, 400, 80);
		v.drawImage(FRAME.ENGINE.UTIL.G_MENU_TEXTBOX, 1340, 600, 400, 80);
		v.drawText("BACK TO MENU", 190, 610, 380, 60);
		v.drawText("PLAY THE GAME", 1350, 610, 380, 60);
		
		if(AGE <= 60 && doFade)
		{
			fadeIn -= 4;
			
			if(fadeIn < 0)
			{
				fadeIn = 0;
			}
		}
		
		if(FADE_OUT)
		{
			fadeIn = Utilities.clamp(fadeIn + 4, 0, 255);
			
			if(fadeIn == 255)
			{
				if(dir)
				{
					FRAME.setActiveState(new TutorialState(FRAME, new GameData(NAME)));
				}
				else
				{
					FRAME.setActiveState(new MainmenuState(FRAME, true));
				}
			}
		}
		
		v.GRAPHICS.setColor(new Color(0, 0, 0, fadeIn));
		v.GRAPHICS.fillRect(FRAME.BORDER_WIDTH, FRAME.BORDER_HEIGHT, FRAME.WIDTH, FRAME.HEIGHT);
	}
	
	public void onTick()
	{
		MAIN_CYCLE = (AGE / 30) % 2;
		ARMS_CYCLE = ((AGE + 10) / 30) % 2;
		
		switch((AGE / 45) % 4)
		{
		case 0:
			SPIN_CYCLE = 1;
			break;
		case 1:
			SPIN_CYCLE = 2;
			break;
		case 2:
			SPIN_CYCLE = 0;
			break;
		case 3:
			SPIN_CYCLE = 3;
			break;
		}
	}
	
	public void fadeOut(boolean direction)
	{
		FADE_OUT = true;
		doFade = false;
		dir = direction;
	}
}
