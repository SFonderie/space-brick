package com.avian.state;

import java.awt.image.BufferedImage;

import com.avian.engine.GameFrame;
import com.avian.engine.Settings;
import com.avian.engine.Utilities;
import com.avian.engine.Visuals;

public class SettingsState extends GameState
{
	private BufferedImage[][] background, buttons, bool, crosshair;
	private String[] bindingNames = {"FORWARD  - ", "BACKWARD - ", "LEFT     - ", "RIGHT    - ",
			"RELOAD   - ", "INTERACT - ", "GET GUNS - ", "NEXT GUN - ",
			"UNIMPLEM - ", "UNIMPLEM - ", "UNIMPLEM - ", "UNIMPLEM - "};
	private String[] buttonLabels = {"RESOLUTION", "DIFFICULTY", "CROSSHAIRS", "SOUND VOLUME", "MUSIC VOLUME", "MAIN MENU",
			"PARTICLES", "FULLSCREEN", "SUPERMAGIC", "HITBOXES", "VOICE LINES", "RESET TO DEFAULTS"};
	private String returnToGame = "RETURN TO GAME";
	private TutorialState tutorialFrom;
	
	public final int NUM_BUTTONS = 12;
	public int[] BUTTON_SEQ = new int[NUM_BUTTONS];
	public boolean[] BUTTON_ACT = new boolean[NUM_BUTTONS];
	public boolean[] MOUSE_OVER;
	public boolean EXIT_BIND = false;
	public int ORIGINAL_RESOLUTION;
	public boolean ORIGINAL_FULLSCREEN;
	public int SWITCH = 0;
	
	public SettingsState(GameFrame frame)
	{
		super(frame);
		background = Visuals.cut(FRAME.ENGINE.UTIL.G_MENU_BACKGROUND, 128, 72);
		buttons = Visuals.cut(FRAME.ENGINE.UTIL.G_MENU_BUTTONS, 72, 18);
		bool = Visuals.cut(FRAME.ENGINE.UTIL.G_GENERAL_BOOLEAN, 64, 64);
		crosshair = Visuals.cut(FRAME.ENGINE.UTIL.G_GENERAL_CROSSHAIR, 128, 128);
		MOUSE_OVER = new boolean[FRAME.ENGINE.SETTINGS.KEYS.length];
		
		ORIGINAL_RESOLUTION = FRAME.ENGINE.SETTINGS.RESOLUTION;
		ORIGINAL_FULLSCREEN = FRAME.ENGINE.SETTINGS.BOOL_OPTIONS[1];
	}
	
	public SettingsState(GameFrame frame, int age)
	{
		super(frame);
		background = Visuals.cut(FRAME.ENGINE.UTIL.G_MENU_BACKGROUND, 128, 72);
		buttons = Visuals.cut(FRAME.ENGINE.UTIL.G_MENU_BUTTONS, 72, 18);
		bool = Visuals.cut(FRAME.ENGINE.UTIL.G_GENERAL_BOOLEAN, 64, 64);
		crosshair = Visuals.cut(FRAME.ENGINE.UTIL.G_GENERAL_CROSSHAIR, 128, 128);
		MOUSE_OVER = new boolean[FRAME.ENGINE.SETTINGS.KEYS.length];
		AGE = age;
		
		ORIGINAL_RESOLUTION = FRAME.ENGINE.SETTINGS.RESOLUTION;
		ORIGINAL_FULLSCREEN = FRAME.ENGINE.SETTINGS.BOOL_OPTIONS[1];
	}
	
	public SettingsState(GameFrame frame, TutorialState state)
	{
		super(frame);
		background = Visuals.cut(FRAME.ENGINE.UTIL.G_MENU_BACKGROUND, 128, 72);
		buttons = Visuals.cut(FRAME.ENGINE.UTIL.G_MENU_BUTTONS, 72, 18);
		bool = Visuals.cut(FRAME.ENGINE.UTIL.G_GENERAL_BOOLEAN, 64, 64);
		crosshair = Visuals.cut(FRAME.ENGINE.UTIL.G_GENERAL_CROSSHAIR, 128, 128);
		MOUSE_OVER = new boolean[FRAME.ENGINE.SETTINGS.KEYS.length];
		
		ORIGINAL_RESOLUTION = FRAME.ENGINE.SETTINGS.RESOLUTION;
		ORIGINAL_FULLSCREEN = FRAME.ENGINE.SETTINGS.BOOL_OPTIONS[1];
		tutorialFrom = state;
	}
	
	public void onActivate() 
	{
		
	}
	
	public void onDeactivate()
	{
		
	}
	
	public void returnToPreviousState()
	{
		if(ORIGINAL_RESOLUTION != FRAME.ENGINE.SETTINGS.RESOLUTION || ORIGINAL_FULLSCREEN != FRAME.ENGINE.SETTINGS.BOOL_OPTIONS[1])
		{
			if(tutorialFrom != null)
			{
				FRAME.ENGINE.restartFrame(FRAME.ENGINE.SETTINGS.RESOLUTION * 320, FRAME.ENGINE.SETTINGS.RESOLUTION * 180, tutorialFrom, AGE);
			}
			else
			{
				FRAME.ENGINE.restartFrame(FRAME.ENGINE.SETTINGS.RESOLUTION * 320, FRAME.ENGINE.SETTINGS.RESOLUTION * 180, new MainmenuState(FRAME, false, AGE), AGE);
			}
		}
		else
		{
			if(tutorialFrom != null)
			{
				FRAME.setActiveState(tutorialFrom);
			}
			else
			{
				FRAME.setActiveState(new MainmenuState(FRAME, false, AGE));
			}
		}
	}
	
	public void onRender(Visuals v) 
	{
		v.tween(new BufferedImage[] {background[0][0], background[0][1]}, AGE, 255, 0, 0, 1920, 1080, false);
		v.drawText("OPTIONS AND SETTINGS", 200, 20, 960, 120);
		v.drawText("KEY BINDS", 1360, 20, 500, 120);
		
		for(int i = 0; i < NUM_BUTTONS; i++)
		{
			v.drawImage(buttons[0][BUTTON_SEQ[i]], 60 + ((i / 6) * 640), 170 + ((i % 6) * 144), 600, 120);
			
			if(BUTTON_ACT[i] && BUTTON_SEQ[i] >= 3)
			{
				if(i == 5)
				{
					v.drawText((tutorialFrom == null) ? buttonLabels[i] : returnToGame, 105 + ((i / 6) * 640), 195 + ((i % 6) * 144), 520, 70);
				}
				else if(i == 11)
				{
					v.drawText(buttonLabels[i], 105 + ((i / 6) * 640), 195 + ((i % 6) * 144), 520, 70);
				}
				else
				{
					v.drawText(buttonLabels[i], 100 + ((i / 6) * 640), 195 + ((i % 6) * 144), 300, 70);
				}
			}
			
			if(BUTTON_ACT[0] && BUTTON_SEQ[0] >= 3)
			{
				v.drawText((FRAME.ENGINE.SETTINGS.RESOLUTION * 320) + "x" + (FRAME.ENGINE.SETTINGS.RESOLUTION * 180), 420, 200, 200, 60);
			}
			
			if(BUTTON_ACT[1] && BUTTON_SEQ[1] >= 3)
			{
				v.drawColoredText(FRAME.ENGINE.SETTINGS.DIFFICULTY_TITLES[FRAME.ENGINE.SETTINGS.DIFFICULTY], 420, 344, 200, 60, 255, 252 - (FRAME.ENGINE.SETTINGS.DIFFICULTY * 42), 0);
			}
			
			if(BUTTON_ACT[2] && BUTTON_SEQ[2] >= 3)
			{
				v.drawImage(crosshair[FRAME.ENGINE.SETTINGS.CROSSHAIR][0], 460, 454, 128, 128);
			}
			
			if(BUTTON_ACT[3] && BUTTON_SEQ[3] >= 3)
			{
				String extraZero = "";
				
				if(FRAME.ENGINE.SETTINGS.SOUNDLEVEL < 10)
				{
					extraZero = "0";
				}
				
				v.drawText(" " + extraZero + FRAME.ENGINE.SETTINGS.SOUNDLEVEL + " ", 420, 632, 200, 60);
			}
			
			if(BUTTON_ACT[4] && BUTTON_SEQ[4] >= 3)
			{
				String extraZero = "";
				
				if(FRAME.ENGINE.SETTINGS.MUSICLEVEL < 10)
				{
					extraZero = "0";
				}
				
				v.drawText(" " + extraZero + FRAME.ENGINE.SETTINGS.MUSICLEVEL + " ", 420, 776, 200, 60);
			}
			
			if(i > 5 && i != 11)
			{
				if(BUTTON_ACT[i] && BUTTON_SEQ[i] >= 3)
				{
					v.drawImage(bool[FRAME.ENGINE.SETTINGS.BOOL_OPTIONS[i - 6] ? 0 : 1][0], 1120, 190 + ((i - 6) * 144), 80, 80);
				}
			}
		}
		
		//DRAWS THE BINDINGS COLLUMN
		for(int i = 0; i < MOUSE_OVER.length; i++)
		{
			if(MOUSE_OVER[i])
			{
				v.drawImage(FRAME.ENGINE.UTIL.G_SETTINGS_HOVERBOX, 1360, 160 + (i * 72), 500, 64);
			}
		}
		
		for(int i = 0; i < bindingNames.length; i++)
		{
			v.drawText(bindingNames[i] + Settings.getValueKey(FRAME.ENGINE.SETTINGS.KEYS[i]), 1380, 170 + (i * 72), 460, 48);
		}
		
		//DRAWS THE SWITCHING OVERLAY
		if(SWITCH > 0)
		{
			v.drawImage(FRAME.ENGINE.UTIL.G_SETTINGS_OVERLAY, 0, 0, 1920, 1080);
			v.drawText("SELECT A NEW BINDING", 450, 360, 1020, 90);
			v.drawText("CURRENT: " + bindingNames[Utilities.clampMin(SWITCH - 1, 0)] + Settings.getValueKey(FRAME.ENGINE.SETTINGS.KEYS[Utilities.clampMin(SWITCH - 1, 0)]), 450, 500, 1020, 90);
			
			if(EXIT_BIND)
			{
				v.drawImage(FRAME.ENGINE.UTIL.G_SETTINGS_HOVERBOX, 445, 699, 310, 50);
			}
			
			v.drawText("EXIT (ESC)", 450, 704, 300, 40);
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
}
