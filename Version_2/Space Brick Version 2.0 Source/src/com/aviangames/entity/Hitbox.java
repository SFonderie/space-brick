package com.aviangames.entity;

public class Hitbox 
{
	protected int xDisp;
	
	protected int yDisp;
	
	protected int width;
	
	protected int height;
	
	protected boolean solid;
	
	public Hitbox(int x, int y, int w, int h, boolean s)
	{
		this.xDisp = x;
		this.yDisp = y;
		this.width = w;
		this.height = h;
		this.solid = s;
	}
}