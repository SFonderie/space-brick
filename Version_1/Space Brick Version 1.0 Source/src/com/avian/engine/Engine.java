package com.avian.engine;

import com.avian.state.GameState;

public class Engine implements Runnable
{
	private Thread thread;
	
	public int AGE;
	public boolean RUNNING;
	public boolean TICKING;
	public String[] LAUNCH_ARGS;
	public GameFrame ACTIVE_FRAME;
	public Settings SETTINGS;
	public Utilities UTIL;
	
	public static void main(String[] args) 
	{
		new Engine(args);
	}
	
	public Engine(String[] launcherArguments)
	{
		UTIL = new Utilities();
		SETTINGS = new Settings();
		LAUNCH_ARGS = launcherArguments;
		RUNNING = false;
		TICKING = false;
		AGE = 0;
		
		ACTIVE_FRAME = new GameFrame(this, SETTINGS.RESOLUTION * 320, SETTINGS.RESOLUTION * 180, SETTINGS.BOOL_OPTIONS[1]);
		start();
	}
	
	public synchronized void start()
	{
		if(RUNNING)
		{
			return;
		}
		
		RUNNING = true;
		TICKING = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop()
	{
		if(!RUNNING)
		{
			return;
		}
		
		RUNNING = false;
		TICKING = false;
		System.out.println(AGE);
		System.exit(0);
	}
	
	public void run() 
	{
		double ticktime = 1000000000 / 60;
		double delta = 0;
		long last = System.nanoTime();
		long now;
		
		while(RUNNING)
		{
			now = System.nanoTime();
			delta += (now - last) / ticktime;
			last = now;
			
			if(delta >= 1)
			{
				if(TICKING)
				{
					ACTIVE_FRAME.tick();
					ACTIVE_FRAME.render();
					AGE++;
				}
				
				delta--;
			}
		}
	}
	
	public void restartFrame(int newWidth, int newHeight, GameState toState, int age)
	{
		ACTIVE_FRAME.setVisible(false);
		ACTIVE_FRAME = new GameFrame(this, newWidth, newHeight, SETTINGS.BOOL_OPTIONS[1]);
		
		if(toState != null)
		{
			ACTIVE_FRAME.setActiveState(toState);
		}
	}
}
