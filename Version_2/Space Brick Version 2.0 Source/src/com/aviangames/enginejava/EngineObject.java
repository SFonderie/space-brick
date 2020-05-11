package com.aviangames.enginejava;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Parent of EngineElement and EngineState.
 * 
 * @author Sydney Fonderie
 *
 */
abstract class EngineObject
{
	private final List<EngineElement> activeElements = new ArrayList<EngineElement>();
	
	private double elapsedTime = 0.0D;
	
	private boolean inactive = false;
	
	private boolean paused = false;
	
	private boolean silenced = false;
	
	//====================================================================================================
	//========================================ABSTRACT METHODS============================================
	//====================================================================================================
	
	/**
	 * Called once every time the engine ticks its members.
	 */
	public abstract void onTick();
	
	/**
	 * Called once every time the engine renders its members.
	 * 
	 * @param v The EngineVisuals object to draw with.
	 */
	public abstract void onRender(EngineVisuals v);
	
	/**
	 * Called when the object is switched on.
	 */
	public abstract void onActivate();
	
	/**
	 * Called when the object is switched off.
	 */
	public abstract void onDeactivate();
	
	/**
	 * Called when a key is pressed or released.
	 * 
	 * @param event The details of the key event.
	 * @param direction Whether the event was a press (true) or release (false).
	 */
	public abstract void onKeyEvent(KeyEvent event, boolean direction);
	
	/**
	 * Called when a mouse button is pressed or released.
	 * 
	 * @param event The details of the mouse button event.
	 * @param direction Whether the event was a press (true) or release (false).
	 */
	public abstract void onMouseEvent(MouseEvent event, boolean direction);
	
	/**
	 * Called when the mouse is moved or dragged (moved with a button down).
	 * 
	 * @param event The details of the mouse movement event.
	 * @param dragged Whether the mouse had a button down when moved.
	 */
	public abstract void onMouseMoveEvent(MouseEvent event, boolean dragged);
	
	//====================================================================================================
	//========================================PUBLIC METHODS AND FUNCTIONS================================
	//====================================================================================================
	
	/**
	 * Activates the provided element, causing updates to be passed to it and its members.
	 * 
	 * @param element The element to activate.
	 */
	public void activateElement(EngineElement element)
	{
		this.activeElements.add(element);
	}
	
	/**
	 * Deactivates the provided element, causing updates to cease being passed to it and its members.
	 * 
	 * @param element The element to activate.
	 */
	public void deactivateElement(EngineElement element)
	{
		this.activeElements.remove(element);
	}
	
	//====================================================================================================
	//========================================GETTERS AND SETTERS=========================================
	//====================================================================================================
	
	/**
	 * Gets the time since the object was last activated, updated once per update cycle. This value does not increment if 
	 * the object is paused or made inactive.
	 * 
	 * @return The time since the object was activated.
	 */
	public double getElapsedTime()
	{
		return this.elapsedTime;
	}
	
	/**
	 * Gets whether or not the object is inactive. When the object is inactive, it will not update itself or its members. This 
	 * means that the object will cease to tick and render.
	 * 
	 * @return Whether or not the object is inactive.
	 */
	public boolean isInactive()
	{
		return this.inactive;
	}
	
	/**
	 * Gets whether or not the object is paused. When the object is paused, it will not tick itself or its members. It will, 
	 * however, continue to render itself and its members.
	 * 
	 * @return Whether or not the object is paused.
	 */
	public boolean isPaused()
	{
		return this.paused;
	}
	
	/**
	 * Gets whether or not the object is silenced. When the object is silenced, it will not render itself or its members. It 
	 * will, however, continue to tick itself and its members.
	 * 
	 * @return Whether or not the object is silenced.
	 */
	public boolean isSilenced()
	{
		return this.silenced;
	}
	
	/**
	 * Sets whether or not the object is inactive. When the object is inactive, it will not update itself or its members. This 
	 * means that the object will cease to tick and render.
	 * 
	 * @param inactive Whether or not to set the object as inactive.
	 */
	public void setInactive(boolean inactive)
	{
		this.inactive = inactive;
	}
	
	/**
	 * Sets whether or not the object is paused. When the object is paused, it will not tick itself or its members. It will, 
	 * however, continue to render itself and its members.
	 * 
	 * @param paused Whether or not to pause the object.
	 */
	public void setPaused(boolean paused)
	{
		this.paused = paused;
	}
	
	/**
	 * Sets whether or not the object is silenced. When the object is silenced, it will not render itself or its members. It 
	 * will, however, continue to tick itself and its members.
	 * 
	 * @param silenced Whether or not to silence the object.
	 */
	public void setSilenced(boolean silenced)
	{
		this.silenced = silenced;
	}
	
	//====================================================================================================
	//========================================PRIVATE METHODS AND OPERATIONS==============================
	//====================================================================================================
	
	void tick()
	{
		if(!this.paused && !this.inactive)
		{
			this.onTick();
			
			for(EngineElement e : this.activeElements)
			{
				e.tick();
			}
		}
	}
	
	void render(EngineVisuals v)
	{
		if(!this.silenced && !this.inactive)
		{
			this.onRender(v);
			
			for(EngineElement e : this.activeElements)
			{
				e.render(v);
			}
		}
	}
	
	void activate()
	{
		this.elapsedTime = 0.0D;
		this.onActivate();
	}
	
	void deactivate()
	{
		this.onDeactivate();
	}
	
	void age(double delta)
	{
		if(!this.paused || !this.inactive)
		{
			this.elapsedTime += delta;
			
			for(EngineElement e : this.activeElements)
			{
				e.age(delta);
			}
		}
	}
	
	void keyEvent(KeyEvent event, boolean direction)
	{
		if(this.inactive)
		{
			return;
		}
		
		this.onKeyEvent(event, direction);
		
		for(EngineElement e : this.activeElements)
		{
			e.keyEvent(event, direction);
		}
	}
	
	void mouseEvent(MouseEvent event, boolean direction)
	{
		if(this.inactive)
		{
			return;
		}
		
		this.onMouseEvent(event, direction);
		
		for(EngineElement e : this.activeElements)
		{
			e.mouseEvent(event, direction);
		}
	}
	
	void mouseMoveEvent(MouseEvent event, boolean dragged)
	{
		if(this.inactive)
		{
			return;
		}
		
		this.onMouseMoveEvent(event, dragged);
		
		for(EngineElement e : this.activeElements)
		{
			e.mouseMoveEvent(event, dragged);
		}
	}
}
