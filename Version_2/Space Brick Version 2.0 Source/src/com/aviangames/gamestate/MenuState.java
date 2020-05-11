package com.aviangames.gamestate;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.aviangames.elements.EngineFadeVeil;
import com.aviangames.elements.EngineSlidingButton;
import com.aviangames.enginejava.EngineCore;
import com.aviangames.enginejava.EngineState;
import com.aviangames.enginejava.EngineVisuals;

public class MenuState extends EngineState
{
	private EngineSlidingButton[] buttons = new EngineSlidingButton[4];

	private BufferedImage[][] t_background;

	private BufferedImage[][] t_buttons;
	
	private EngineFadeVeil fader;

	private final String[] labels =
	{
			"PLAY GAME", "CONTROLS", " CREDITS ", "QUIT GAME"
	};
	
	private int buttonPressed = 0;

	public MenuState(EngineCore engine)
	{
		super(engine);

		this.t_background = this.getEngine().getAssetLibrary().getImageDoubleArray("BACKING");
		this.t_buttons = this.getEngine().getAssetLibrary().getImageDoubleArray("SLIDERS");

		for (int i = 0; i < this.buttons.length; i++)
		{
			this.buttons[i] = new EngineSlidingButton(this, 108, 140 + (i * 81), 288, 72, this.t_buttons[0], 2);
			this.buttons[i].setLabel(this.labels[i], 0, 0.75);
			this.activateElement(this.buttons[i]);
		}
		
		this.fader = new EngineFadeVeil(this, 2.0);
		this.activateElement(this.fader);
	}

	@Override
	public void onTick()
	{
		if(this.fader.allFade() && this.buttonPressed > 0)
		{
			if(this.buttonPressed == 1)
			{
				this.getEngine().setActiveState(new GameState(this.getEngine()));
			}
			else if(this.buttonPressed == 2)
			{
				this.getEngine().setActiveState(new CreditState(this.getEngine(), this, false));
			}
			else if(this.buttonPressed == 3)
			{
				this.getEngine().setActiveState(new CreditState(this.getEngine(), this, true));
			}
			else if(this.buttonPressed == 4)
			{
				this.getEngine().stop();
			}
		}
	}

	@Override
	public void onRender(EngineVisuals v)
	{
		v.drawImageFade(new BufferedImage[]{this.t_background[1][0], this.t_background[1][1]}, this, 5, 0, 0, v.WIDTH, v.HEIGHT);
		v.drawText("SPACE BRICK VERSION 2.0", 0, 160, 40, 600, 40);
	}

	@Override
	public void onActivate()
	{
		if(!this.fader.fadeDir())
		{
			this.fader.toggle();
		}
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
		if(this.fader.fadeDir())
		{
			for(int i = 0; i < this.buttons.length; i++)
			{
				if(this.buttons[i].active())
				{
					this.fader.toggle();
					this.buttonPressed = i + 1;
				}
			}
		}
	}

	@Override
	public void onMouseMoveEvent(MouseEvent event, boolean dragged)
	{

	}
}
