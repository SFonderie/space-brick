package com.aviangames.enginejava;

/**
 * Ruler of all EngineElements. Use extensions of this to encapsulate broad rule sets for your game.
 * 
 * @author Sydney Fonderie
 *
 */
public abstract class EngineState extends EngineObject
{
	private final EngineCore parentEngine;
	
	public EngineState(EngineCore engine)
	{
		this.parentEngine = engine;
	}
	
	//====================================================================================================
	//========================================GETTERS AND SETTERS=========================================
	//====================================================================================================
	
	/**
	 * Gets the EngineCore serving as the state's parent.
	 * 
	 * @return The engine object serving this state.
	 */
	public EngineCore getEngine()
	{
		return this.parentEngine;
	}
}
