package com.avian.entity;

public class Hitbox 
{
	public int X_DISP;
	public int Y_DISP;
	public int WIDTH;
	public int HEIGHT;
	public boolean IS_SOLID;
	
	public Hitbox(int xDisplacement, int yDisplacement, int width, int height, boolean solid)
	{
		X_DISP = xDisplacement;
		Y_DISP = yDisplacement;
		WIDTH = width;
		HEIGHT = height;
		IS_SOLID = solid;
	}
}
