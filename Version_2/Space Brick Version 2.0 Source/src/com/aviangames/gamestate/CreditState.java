package com.aviangames.gamestate;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.aviangames.elements.EngineFadeVeil;
import com.aviangames.elements.EngineSlidingButton;
import com.aviangames.enginejava.EngineCore;
import com.aviangames.enginejava.EngineState;
import com.aviangames.enginejava.EngineVisuals;

public class CreditState extends EngineState
{
	private boolean credits;
	
	private BufferedImage[][] t_background;

	private BufferedImage[][] t_buttons;

	private String[] lines =
	{
			"Code Developer     - Sydney Fonderie", 
			"Engine Design      - Sydney Fonderie", 
			"Music Composition  - Sydney Fonderie",
			"Artwork / Sprites  - Sydney Fonderie",
			"Bad Joke Design    - Sydney Fonderie"
	};
	
	private String[] controls =
	{
			"Movement Axis      - W A S D        ", 
			"Switch Weapons     - SPACE          ", 
			"Fire Weapon        - LMB            ",
			"Reload Weapon      - R              ",
			"Show Hitboxes      - H              "
	};

	private EngineSlidingButton button;

	private EngineFadeVeil fader;
	
	private final MenuState root;

	public CreditState(EngineCore engine, MenuState menu, boolean credits)
	{
		super(engine);
		
		this.root = menu;
		
		this.credits = credits;

		this.t_background = this.getEngine().getAssetLibrary().getImageDoubleArray("BACKING");
		this.t_buttons = this.getEngine().getAssetLibrary().getImageDoubleArray("SLIDERS");
		
		this.button = new EngineSlidingButton(this, 90, 465, 200, 50, this.t_buttons[0], 2);
		this.button.setLabel("MAIN MENU", 0, 0.75);
		this.activateElement(this.button);
		
		this.fader = new EngineFadeVeil(this, 2.0);
		this.activateElement(this.fader);
	}

	@Override
	public void onTick()
	{
		if(this.fader.allFade())
		{
			this.getEngine().setActiveState(this.root);
		}
	}

	@Override
	public void onRender(EngineVisuals v)
	{
		v.drawImageFade(new BufferedImage[]{this.t_background[1][0], this.t_background[1][1]}, this, 5, 0, 0, v.WIDTH, v.HEIGHT);
		
		String title = this.credits ? "DEVELOPER CREDITS" : "  GAME CONTROLS  ";
		v.drawText(title, 0, 160, 40, 600, 40);
		
		int val = v.HEIGHT / (this.lines.length + 2);
		
		for(int i = 0; i < this.lines.length; i++)
		{
			String[] lines = this.credits ? this.lines : this.controls;
			v.drawText(lines[i], 0, val, val + 20 + (i * val), 800, 40);
		}
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

	}

	@Override
	public void onMouseEvent(MouseEvent event, boolean direction)
	{
		if(this.button.active() && this.fader.fadeDir())
		{
			this.fader.toggle();
		}
	}

	@Override
	public void onMouseMoveEvent(MouseEvent event, boolean dragged)
	{

	}
}
