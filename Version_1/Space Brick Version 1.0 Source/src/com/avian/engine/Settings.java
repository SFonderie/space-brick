package com.avian.engine;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class Settings
{
	public int[] KEYS = new int[12];
	public int RESOLUTION;
	public int DIFFICULTY;
	public int SOUNDLEVEL;
	public int MUSICLEVEL;
	public int CROSSHAIR;
	
	public boolean[] BOOL_OPTIONS = new boolean[5];
	
	public String[] DIFFICULTY_TITLES = {"IMMORTAL", "CAKEWALK", "STANDARD", "ROGUELIKE", "MASOCHIST", "POURQUOI"};
	public String[] DIFFICULTY_DESCRIPTIONS = {
			"ONE DOES NOT SIMPLY DIE WHILE PLAYING THIS. IF YOU DO... WELL...",
			"FOR THOSE LOOKING TO GET A FEEL FOR THE GAME SANS ANY PRESSURE",
			"ENJOY THE GAME WITHOUT HAVING TO DEAL WITH ANYTHING RAGE-INDUCING",
			"MORE BULLETS AND BADDIES FOR A MORE TRADITIONAL TOP-DOWN EXPERIENCE",
			"NASTY ENEMIES, AMMO LIMITATIONS, AND MORE HAZARDS. YOU MUST LIKE PAIN...",
			"WHY? WHY WOULD ANYONE EVEN DO THIS TO THEMSELVES? CE N'EST PAS HUMAIN!"
	};
	
	public Settings()
	{
		KEYS[0] = KeyEvent.VK_W;
		KEYS[1] = KeyEvent.VK_S;
		KEYS[2] = KeyEvent.VK_A;
		KEYS[3] = KeyEvent.VK_D;
		KEYS[4] = KeyEvent.VK_R;
		KEYS[5] = KeyEvent.VK_E;
		KEYS[6] = KeyEvent.VK_G;
		KEYS[7] = KeyEvent.VK_SPACE;
		KEYS[8] = KeyEvent.VK_Z;
		KEYS[9] = KeyEvent.VK_X;
		KEYS[10] = KeyEvent.VK_C;
		KEYS[11] = KeyEvent.VK_H;
		
		RESOLUTION = Toolkit.getDefaultToolkit().getScreenSize().width / 320;
		DIFFICULTY = 2;
		SOUNDLEVEL = 10;
		MUSICLEVEL = 10;
		CROSSHAIR = 0;
		
		BOOL_OPTIONS[0] = true;
		BOOL_OPTIONS[1] = true;
		BOOL_OPTIONS[2] = false;
		BOOL_OPTIONS[3] = false;
		BOOL_OPTIONS[4] = true;
	}
	
	public void switchKey(int id, int newVal)
	{
		KEYS[id] = newVal;
	}
	
	public void cycleResolution(boolean direction)
	{
		RESOLUTION = Utilities.wrap(RESOLUTION + (direction ? 1 : -1), 2, 12);
		
		if(RESOLUTION == 8 || (RESOLUTION > 9 && RESOLUTION < 12))
		{
			cycleResolution(direction);
		}
	}
	
	public void cycleDifficulty(boolean direction)
	{
		DIFFICULTY = Utilities.wrap(DIFFICULTY + (direction ? 1 : -1), 0, 5);
	}
	
	public void cycleCrosshair(boolean direction)
	{
		CROSSHAIR = Utilities.wrap(CROSSHAIR + (direction ? 1 : -1), 0, 3);
	}
	
	public void cycleSoundLevel(boolean direction)
	{
		SOUNDLEVEL = Utilities.wrap(SOUNDLEVEL + (direction ? 1 : -1), 0, 20);
	}
	
	public void cycleMusicLevel(boolean direction)
	{
		MUSICLEVEL = Utilities.wrap(MUSICLEVEL + (direction ? 1 : -1), 0, 20);
	}
	
	public static String getKey(int id)
	{
		switch(id)
		{
		case KeyEvent.VK_A:
			return "A";
		case KeyEvent.VK_B:
			return "B";
		case KeyEvent.VK_C:
			return "C";
		case KeyEvent.VK_D:
			return "D";
		case KeyEvent.VK_E:
			return "E";
		case KeyEvent.VK_F:
			return "F";
		case KeyEvent.VK_G:
			return "G";
		case KeyEvent.VK_H:
			return "H";
		case KeyEvent.VK_I:
			return "I";
		case KeyEvent.VK_J:
			return "J";
		case KeyEvent.VK_K:
			return "K";
		case KeyEvent.VK_L:
			return "L";
		case KeyEvent.VK_M:
			return "M";
		case KeyEvent.VK_N:
			return "N";
		case KeyEvent.VK_O:
			return "O";
		case KeyEvent.VK_P:
			return "P";
		case KeyEvent.VK_Q:
			return "Q";
		case KeyEvent.VK_R:
			return "R";
		case KeyEvent.VK_S:
			return "S";
		case KeyEvent.VK_T:
			return "T";
		case KeyEvent.VK_U:
			return "U";
		case KeyEvent.VK_V:
			return "V";
		case KeyEvent.VK_W:
			return "W";
		case KeyEvent.VK_X:
			return "X";
		case KeyEvent.VK_Y:
			return "Y";
		case KeyEvent.VK_Z:
			return "Z";
		case KeyEvent.VK_SPACE:
			return " ";
		default:
			return "";
		}
	}
	
	public static String getValueKey(int id)
	{
		switch(id)
		{
		case KeyEvent.VK_A:
			return "  A  ";
		case KeyEvent.VK_B:
			return "  B  ";
		case KeyEvent.VK_C:
			return "  C  ";
		case KeyEvent.VK_D:
			return "  D  ";
		case KeyEvent.VK_E:
			return "  E  ";
		case KeyEvent.VK_F:
			return "  F  ";
		case KeyEvent.VK_G:
			return "  G  ";
		case KeyEvent.VK_H:
			return "  H  ";
		case KeyEvent.VK_I:
			return "  I  ";
		case KeyEvent.VK_J:
			return "  J  ";
		case KeyEvent.VK_K:
			return "  K  ";
		case KeyEvent.VK_L:
			return "  L  ";
		case KeyEvent.VK_M:
			return "  M  ";
		case KeyEvent.VK_N:
			return "  N  ";
		case KeyEvent.VK_O:
			return "  O  ";
		case KeyEvent.VK_P:
			return "  P  ";
		case KeyEvent.VK_Q:
			return "  Q  ";
		case KeyEvent.VK_R:
			return "  R  ";
		case KeyEvent.VK_S:
			return "  S  ";
		case KeyEvent.VK_T:
			return "  T  ";
		case KeyEvent.VK_U:
			return "  U  ";
		case KeyEvent.VK_V:
			return "  V  ";
		case KeyEvent.VK_W:
			return "  W  ";
		case KeyEvent.VK_X:
			return "  X  ";
		case KeyEvent.VK_Y:
			return "  Y  ";
		case KeyEvent.VK_Z:
			return "  Z  ";
		case KeyEvent.VK_SPACE:
			return "SPACE";
		case KeyEvent.VK_SHIFT:
			return "SHIFT";
		case KeyEvent.VK_CONTROL:
			return "CNTRL";
		case KeyEvent.VK_ALT:
			return "ALT";
		default:
			return " N/A ";
		}
	}
}
