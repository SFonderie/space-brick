package com.aviangames.enginejava;

/**
 * Extend to create non-state objects for your game.
 * 
 * @author Sydney Fonderie
 *
 */
public abstract class EngineElement extends EngineObject
{
	private final EngineState parentState;
	
	public EngineElement(EngineState parent)
	{
		this.parentState = parent;
	}
	
	public EngineElement(EngineElement parent)
	{
		this.parentState = parent.getState();
	}
	
	//====================================================================================================
	//========================================GETTERS AND SETTERS=========================================
	//====================================================================================================
	
	/**
	 * Gets the EngineState serving as the element's parent.
	 * 
	 * @return The state object ultimately serving this element.
	 */
	public EngineState getState()
	{
		return this.parentState;
	}
}
