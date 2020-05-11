package com.avian.engine;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import com.avian.entity.EntityPlayer;
import com.avian.level.GameLevel;

public class Utilities
{
	public BufferedImage E_FREEZE_BOX;
	public BufferedImage E_PLAYER_ARMS;
	public BufferedImage E_PLAYER_HEAD;
	public BufferedImage E_PLAYER_PROJECTILES;
	public BufferedImage E_PLAYER_TORSO;
	public BufferedImage G_GENERAL_BOOLEAN;
	public BufferedImage G_GENERAL_CROSSHAIR;
	public BufferedImage G_GENERAL_CURSOR;
	public BufferedImage G_GENERAL_FONT;
	public BufferedImage G_HUD_AMMOICON;
	public BufferedImage G_HUD_HEALTHBAR;
	public BufferedImage G_HUD_RELOADING;
	public BufferedImage G_HUD_WEAPONS;
	public BufferedImage G_MENU_BACKGROUND;
	public BufferedImage G_MENU_BUTTONS;
	public BufferedImage G_MENU_LOGO;
	public BufferedImage G_MENU_TEXTBOX;
	public BufferedImage G_OVERTURE_SPLASH;
	public BufferedImage G_SETTINGS_HOVERBOX;
	public BufferedImage G_SETTINGS_OVERLAY;
	public BufferedImage L_STATION_TILES;
	
	public BufferedImage[][] V_CROSSHAIR, V_RELOADING, V_WEAPONS, V_HEALTHBAR;
	
	public Cursor[] CURSORS = new Cursor[2];
	
	public Utilities()
	{
		E_FREEZE_BOX = Visuals.loadImage("e_freeze_box");
		E_PLAYER_ARMS = Visuals.loadImage("e_player_arms");
		E_PLAYER_HEAD = Visuals.loadImage("e_player_head");
		E_PLAYER_PROJECTILES = Visuals.loadImage("e_player_projectiles");
		E_PLAYER_TORSO = Visuals.loadImage("e_player_torso");
		G_GENERAL_BOOLEAN = Visuals.loadImage("g_general_boolean");
		G_GENERAL_CROSSHAIR = Visuals.loadImage("g_general_crosshair");
		G_GENERAL_CURSOR = Visuals.loadImage("g_general_cursor");
		G_GENERAL_FONT = Visuals.loadImage("g_general_font");
		G_HUD_AMMOICON = Visuals.loadImage("g_hud_ammoicon");
		G_HUD_HEALTHBAR = Visuals.loadImage("g_hud_healthbar");
		G_HUD_RELOADING = Visuals.loadImage("g_hud_reloading");
		G_HUD_WEAPONS = Visuals.loadImage("g_hud_weapons");
		G_MENU_BACKGROUND = Visuals.loadImage("g_menu_background");
		G_MENU_BUTTONS = Visuals.loadImage("g_menu_buttons");
		G_MENU_LOGO = Visuals.loadImage("g_menu_logo");
		G_MENU_TEXTBOX = Visuals.loadImage("g_menu_textbox");
		G_OVERTURE_SPLASH = Visuals.loadImage("g_overture_splash");
		G_SETTINGS_HOVERBOX = Visuals.loadImage("g_settings_hoverbox");
		G_SETTINGS_OVERLAY = Visuals.loadImage("g_settings_overlay");
		L_STATION_TILES = Visuals.loadImage("l_station_tiles");
		
		for(int i = 0; i < CURSORS.length; i++)
		{
			CURSORS[i] = Toolkit.getDefaultToolkit().createCustomCursor(Visuals.cut(G_GENERAL_CURSOR, 64, 64)[i][0], new Point(0, 0), "CURSOR" + i);
		}
		
		V_CROSSHAIR = Visuals.cut(G_GENERAL_CROSSHAIR, 128, 128);
		V_RELOADING = Visuals.cut(G_HUD_RELOADING, 32, 32);
		V_WEAPONS = Visuals.cut(G_HUD_WEAPONS, 64, 64);
		V_HEALTHBAR = Visuals.cut(G_HUD_HEALTHBAR, 384, 32);
	}
	
	public static int clampMin(int toClamp, int min)
	{
		if(toClamp < min)
		{
			toClamp = min;
		}
		
		return toClamp;
	}
	
	public static int clampMax(int toClamp, int max)
	{
		if(toClamp > max)
		{
			toClamp = max;
		}
		
		return toClamp;
	}
	
	public static int clamp(int toClamp, int min, int max)
	{
		toClamp = clampMin(toClamp, min);
		toClamp = clampMax(toClamp, max);
		return toClamp;
	}
	
	public static int wrap(int toWrap, int min, int max)
	{
		if(toWrap > max)
		{
			toWrap = min;
		}
		
		if(toWrap < min)
		{
			toWrap = max;
		}
		
		return toWrap;
	}
	
	public static int translate(int toTranslate, GameFrame basis, boolean isX)
	{
		return isX ? (int)(toTranslate * (1f / basis.NATIVE_WIDTH) * basis.WIDTH) : (int)(toTranslate * (1f / basis.NATIVE_HEIGHT) * basis.HEIGHT);
	}
	
	public EntityPlayer getPlayer(GameLevel toApplyTo)
	{
		return new EntityPlayer(toApplyTo, 960, 540, 128, 128, 0.0);
	}
}
