package com.aviangames.enginejava;

import java.awt.image.BufferedImage;

/**
 * Create an instance of this class and call the start method to initialize a clock that updates at the given frequency.
 * 
 * @author Sydney Fonderie
 *
 */
public final class EngineCore implements Runnable
{	
	private final String instanceTitle;
	
	private final int updateFrequency;
	
	private EngineFrame activeFrame;
	
	private EngineLibrary activeLibrary;
	
	private EngineState activeState;
	
	private double elapsedTime = 0.0D;
	
	private int elapsedUpdates = 0;
	
	private boolean running = false;
	
	/**
	 * Creates a dormant EngineCore object in preparation for initialization. The initialization period ends (and the 
	 * running period begins) with the calling of the start method.
	 * 
	 * @param updateFrequency The frequency of engine updates once the engine starts.
	 * @param title The name of the engine to the Operating System.
	 */
	public EngineCore(int updateFrequency, String title)
	{
		this.activeLibrary = new EngineLibrary();
		this.updateFrequency = updateFrequency;
		this.instanceTitle = title;
	}
	
	//====================================================================================================
	//========================================OVERRIDES AND IMPLEMENTS====================================
	//====================================================================================================
	
	@Override
	public void run()
	{
		if(this.running)
		{
			return;
		}
		
		this.running = true;
		
		long last = System.nanoTime();
		long now;
		
		double duration = 1000000000.0D / this.updateFrequency;
		double delta = 0;
		
		while(this.running)
		{
			now = System.nanoTime();
			delta += (now - last) / duration;
			last = now;
			
			if(delta >= 1)
			{
				this.update(delta / this.updateFrequency);
				this.elapsedTime += delta / this.updateFrequency;
				this.elapsedUpdates++;
				delta = 0;
			}
		}
		
		System.out.println("Engine closing by system exit...");
		System.exit(0);
	}
	
	@Override
	public String toString()
	{
		return this.instanceTitle;
	}
	
	//====================================================================================================
	//========================================STATIC METHODS AND FUNCTIONS================================
	//====================================================================================================
	
	/**
	 * Forces the provided value to fall somewhere on the interval [minimum, maximum] as provided in the 
	 * parameters. Values that are too low will instead simply be set to the minimum, with values that are too high will 
	 * be set to the maximum.
	 * 
	 * @param value The value to force invariance on.
	 * @param minimum The minimum the value is allowed to be.
	 * @param maximum The maximum the value is allowed to be.
	 * 
	 * @return The value, modified so that it stays on [minimum, maximum].
	 */
	public static double forceInvariance(double value, double minimum, double maximum)
	{
		if(value < minimum)
		{
			value = minimum;
		}
		
		if(value > maximum)
		{
			value = maximum;
		}
		
		return value;
	}
	
	/**
	 * Letting (x1, y1) be the origin, this method calculates the angle made with the x-axis between the two points on 
	 * the interval [0, 2PI).
	 * 
	 * @param x1 The x-coordinate of the first point (origin).
	 * @param y1 The y-coordinate of the first point (origin).
	 * @param x2 The x-coordinate of the second point.
	 * @param y2 The y-coordinate of the second point.
	 * 
	 * @return The angle formed between the two points with respect to the x-axis.
	 */
	public static double getAngle(int x1, int y1, int x2, int y2)
	{
		int deltaX = x2 - x1;
		int deltaY = y2 - y1;
		
		double dir = Math.asin(deltaX / Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
		
		// Quadrant I or Quadrant IV
		if(deltaX >= 0)
		{
			//Quadrant I
			if(deltaY >= 0)
			{
				return dir;
			}
			//Quadrant IV
			else
			{
				return Math.PI - dir;
			}
		}
		//Quadrant II or Quadrant III
		else
		{
			//Quadrant II
			if(deltaY >= 0)
			{
				return (Math.PI * 2) + dir;
			}
			//Quadrant III
			else
			{
				return Math.PI - dir;
			}
		}
	}
	
	//====================================================================================================
	//========================================PUBLIC METHODS AND FUNCTIONS================================
	//====================================================================================================
	
	/**
	 * Starts the engine loop by creating a new thread. Once this is called, the engine will become active and start 
	 * updating itself at the rate provided in its constructor. Note that this method must be called in order for the 
	 * engine to actually do anything.
	 */
	public synchronized void start()
	{
		if(this.running)
		{
			return;
		}
		
		new Thread(this).start();
	}
	
	/**
	 * Stops the engine loop by turning off the boolean it uses to continue running. This will trigger a system exit in the 
	 * same way closing works for the frame close button.
	 */
	public synchronized void stop()
	{
		if(!this.running)
		{
			return;
		}
		
		this.running = false;
	}
	
	//====================================================================================================
	//========================================GETTERS AND SETTERS=========================================
	//====================================================================================================
	
	public int convertMouseX(int mouseX)
	{
		return this.activeFrame.convertMouseX(mouseX);
	}
	
	public int convertMouseY(int mouseY)
	{
		return this.activeFrame.convertMouseY(mouseY);
	}
	
	/**
	 * Gets the EngineState currently being supported by the engine.
	 * 
	 * @return The currently active state.
	 */
	public EngineState getActiveState()
	{
		return this.activeState;
	}
	
	/**
	 * Gets the EngineLibrary that the engine can use for image loading.
	 * 
	 * @return The default library attached to the engine.
	 */
	public EngineLibrary getAssetLibrary()
	{
		return this.activeLibrary;
	}
	
	/**
	 * Gets the time that has passed since the engine began updating itself in seconds. This value will always continue 
	 * increasing until the engine is forcibly stopped, and serves mainly as a root value for state updates rather than 
	 * something to be used by a programmer for in-game effects.
	 * 
	 * @return The age of the engine, expressed in seconds.
	 */
	public double getElapsedTime()
	{
		return this.elapsedTime;
	}
	
	/**
	 * Gets the number of update cycles that have passed since the engine began updating itself. This value will always 
	 * continue increasing until the engine is forcibly stopped, and serves mainly as a root value for state updates 
	 * rather than something to be used by a programmer for in-game effects. 
	 * 
	 * @return The number of updates that have occurred since the engine's start.
	 */
	public int getElapsedUpdates()
	{
		return this.elapsedUpdates;
	}
	
	/**
	 * Gets the finalized tick rate of the engine. This number represents the number of ticks and renders, together 
	 * called updates, that the engine will attempt to perform every second. This can be used as a conversion ratio 
	 * between elapsed updates and elapsed time.
	 * 
	 * @return The number of attempted updates per second.
	 */
	public int getUpdateFrequency()
	{
		return this.updateFrequency;
	}
	
	/**
	 * Whether or not the engine start method has already been called, representing engine looping.
	 * 
	 * @return Whether the engine start method has been called.
	 */
	public boolean isRunning()
	{
		return this.running;
	}
	
	/**
	 * Creates a new {@link EngineFrame} object with which the engine can render based on the given dimensions. Note 
	 * that rendering with the engine works by creating a temporary "surrogate" image with the given "render" 
	 * dimensions and then rendering everything relative to that so programmers won't have to worry about 
	 * alternating screen sizes and resolutions when coding in render commands.
	 * 
	 * @param width The width of the new frame and render space.
	 * @param height The height of the new frame and render space.
	 * @param renderWidth The width of the image used for relative rendering.
	 * @param renderHeight The height of the image used for relative rendering.
	 * @param fullscreen Whether or not the frame should be full-screen.
	 */
	public void setActiveFrame(int width, int height, int renderWidth, int renderHeight, boolean fullscreen)
	{
		if(this.activeFrame != null)
		{
			this.activeFrame.setVisible(false);
		}
		
		this.activeFrame = new EngineFrame(this, width, height, renderWidth, renderHeight, fullscreen);
	}
	
	/**
	 * Sets the EngineState that the engine will support with update (tick and render) cycles.
	 * 
	 * @param state The new active state to support.
	 */
	public void setActiveState(EngineState state)
	{
		if(this.activeState != null)
		{
			this.activeState.deactivate();
		}
		
		this.activeState = state;
		
		if(this.activeState != null)
		{
			this.activeState.activate();
		}
	}
	
	/**
	 * Sets the active frame's icon to the specified image.
	 * 
	 * @param icon The image to set as the icon.
	 */
	public void setFrameIcon(BufferedImage icon)
	{
		if(this.activeFrame != null)
		{
			this.activeFrame.setIconImage(icon);
		}
	}
	
	//====================================================================================================
	//========================================PRIVATE METHODS AND OPERATIONS==============================
	//====================================================================================================
	
	void update(double delta)
	{
		if(this.activeState != null)
		{
			this.activeState.tick();
		}
		
		if(this.activeFrame != null)
		{
			this.activeFrame.update();
		}
		
		if(this.activeState != null)
		{
			this.activeState.age(delta);
		}
	}
}
