package com.aviangames.utilities;

import java.awt.Toolkit;

import com.aviangames.enginejava.EngineCore;
import com.aviangames.enginejava.EngineVisuals;
import com.aviangames.gamestate.MenuState;

public class Launcher
{
	public static void main(String[] args)
	{
		EngineCore engine = new EngineCore(30, "WHATEVER");
		
		engine.getAssetLibrary().loadImage("/texture/interface_ammoicon.png", "AMMO");
		engine.getAssetLibrary().loadImageArray("/texture/interface_fontset.png", "FONT", 13, 6);
		engine.getAssetLibrary().loadImageArray("/texture/interface_reload.png", "RELOAD", 6, 4);
		engine.getAssetLibrary().loadImageArray("/texture/interface_weapon.png", "WEAPONS", 4, 4);
		engine.getAssetLibrary().loadImageDoubleArray("/texture/interface_button.png", "SLIDERS", 3, 10);
		engine.getAssetLibrary().loadImageDoubleArray("/texture/interface_texture.png", "BACKING", 2, 2);
		
		engine.getAssetLibrary().loadImageDoubleArray("/texture/entity_bossmob.png", "BOSS", 4, 4);
		engine.getAssetLibrary().loadImageDoubleArray("/texture/entity_player_arms.png", "ARMS", 16, 16);
		engine.getAssetLibrary().loadImageDoubleArray("/texture/entity_player_head.png", "HEAD", 5, 4);
		engine.getAssetLibrary().loadImageDoubleArray("/texture/entity_player_legs.png", "LEGS", 8, 4);
		engine.getAssetLibrary().loadImageArray("/texture/entity_player_shot.png", "SHOT", 4, 4);
		
		engine.getAssetLibrary().loadImageArray("/texture/level_station.png", "STATION", 8, 8);
		
		EngineVisuals.setFont("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+&%.!?,[]{}()/- ");
		EngineVisuals.addFont(engine.getAssetLibrary().getImageArray("FONT"), 1.0, 1.0, 1.0);
		
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		engine.setActiveFrame(width, height, 960, 540, true);
		engine.setActiveState(new MenuState(engine));
		
		engine.start();
	}
}
